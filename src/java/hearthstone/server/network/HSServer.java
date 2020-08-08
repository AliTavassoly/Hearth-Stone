package hearthstone.server.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hearthstone.models.Account;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.card.spell.spells.WeaponSteal;
import hearthstone.models.player.Player;
import hearthstone.server.data.DataBase;
import hearthstone.server.data.ServerData;
import hearthstone.server.logic.Game;
import hearthstone.server.logic.Market;
import hearthstone.server.model.updaters.AccountUpdater;
import hearthstone.server.model.ClientDetails;
import hearthstone.server.model.updaters.MarketCardsUpdater;
import hearthstone.server.model.UpdateWaiter;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HSServer extends Thread {
    private static HSServer server;

    private ServerSocket serverSocket;

    private Map<String, ClientDetails> clients;

    private Map<Integer, Player> players;
    private final Object playersLock = new Object();

    private ArrayList<UpdateWaiter> updaterWaiters;

    private ArrayList<String> waitingForGame;
    private final Object waitingForGameLock = new Object();

    public static Market market = new Market();

    private HSServer(int serverPort) {
        try {
            this.serverSocket = new ServerSocket(serverPort);

            System.out.println("Server Started at: " + serverPort);

            configServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configServer() {
        clients = new HashMap<>();
        updaterWaiters = new ArrayList<>();
        waitingForGame = new ArrayList<>();
        players = new HashMap<>();
    }

    public static HSServer makeNewInstance(int serverPort) {
        return server = new HSServer(serverPort);
    }

    public static HSServer getInstance() {
        return server;
    }

    @Override
    public void start() {
        while (!isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();

                System.out.println("New Client Added :" + socket.getRemoteSocketAddress().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createBaseCards(ClientHandler clientHandler) {
        ServerMapper.createBaseCardsResponse(ServerData.baseCards, clientHandler);
    }

    public void createBaseHeroes(ClientHandler clientHandler) {
        ServerMapper.createBaseHeroesResponse(ServerData.baseHeroes, clientHandler);
    }

    public void createBasePassives(ClientHandler clientHandler) {
        ServerMapper.createBasePassivesResponse(ServerData.basePassives, clientHandler);
    }

    private void accountConnected(String username, ClientHandler clientHandler) {
        ServerData.getClientDetails(username).setClientHandler(clientHandler);

        clientHandler.clientSignedIn(username);

        updaterWaiters.add(new AccountUpdater(clientHandler.getUsername(), UpdateWaiter.UpdaterType.ACCOUNT, clientHandler));

        clients.put(username, ServerData.getClientDetails(username));

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
    }

    private void accountDisconnected(ClientHandler clientHandler) {
        removeUpdater(clientHandler.getUsername(), UpdateWaiter.UpdaterType.ACCOUNT);

        String username = clientHandler.getUsername();
        clientHandler.clientSignedOut();
        ServerData.getClientDetails(username).setClientHandler(null);
        ServerData.getClientDetails(username).setCurrentGame(null);

        //removeGameWaiter(username);
    }

    public void clientHandlerDisconnected(ClientHandler clientHandler) {
        String username = clientHandler.getUsername();
        if (username != null) {
            onlineGameCancelRequest(clientHandler);
            accountDisconnected(clientHandler);
        }
    }

    public void login(String username, String password, ClientHandler clientHandler) throws HearthStoneException {
        ServerData.checkAccountCredentials(username, password);

        HSServer.getInstance().accountConnected(username, clientHandler);

        ServerMapper.loginResponse(username, clientHandler);
    }

    public void register(String name, String username, String password, ClientHandler clientHandler) throws HearthStoneException {
        ServerData.addAccountCredentials(username, password);
        Account account = new Account(ServerData.getAccountId(username), name, username);
        ServerData.addNewClientDetails(username, account);

        DataBase.save(account);

        HSServer.getInstance().accountConnected(username, clientHandler);

        ServerMapper.registerResponse(username, clientHandler);
    }

    public void logout(ClientHandler clientHandler) {
        String username = clientHandler.getUsername();
        DataBase.save(ServerData.getClientDetails(username).getAccount());

        HSServer.getInstance().accountDisconnected(clientHandler);
        ServerMapper.logoutResponse(clientHandler);
    }

    public void deleteAccount(ClientHandler clientHandler) {
        String username = clientHandler.getUsername();

        ServerData.deleteAccount(username);

        DataBase.save(DataBase.getAccount(username));

        ServerMapper.deleteAccountResponse(clientHandler);
    }

    public void onlineGameRequest(ClientHandler clientHandler) {
        String username0 = null, username1 = null;

        synchronized (waitingForGameLock) {
            waitingForGame.add(clientHandler.getUsername());
            if (waitingForGame.size() >= 2) {
                username1 = waitingForGame.remove(waitingForGame.size() - 1);
                username0 = waitingForGame.remove(0);
            }
        }

        if (username0 != null && username1 != null)
            makeNewOnlineGame(username0, username1);
    }

    public void onlineGameCancelRequest(ClientHandler clientHandler) {
        synchronized (waitingForGameLock) {
            waitingForGame.remove(clientHandler.getUsername());
        }
    }

    public void makeNewOnlineGame(String username0, String username1) {
        Player player0 = clients.get(username0).getAccount().getPlayer();
        Player player1 = clients.get(username1).getAccount().getPlayer();

        Game game = new Game(player0, player1, makeNewPlayerId(player0), makeNewPlayerId(player1));

        clients.get(username0).setCurrentGame(game);
        clients.get(username1).setCurrentGame(game);

        clients.get(player0.getUsername()).getClientHandler().setGame(game);
        clients.get(player1.getUsername()).getClientHandler().setGame(game);

        clients.get(player0.getUsername()).getClientHandler().setPlayer(player0);
        clients.get(player1.getUsername()).getClientHandler().setPlayer(player1);

        ServerMapper.onlineGameResponse(player0, player1, clients.get(player0.getUsername()).getClientHandler());
        ServerMapper.onlineGameResponse(player1, player0, clients.get(player1.getUsername()).getClientHandler());

        game.start();
    }

    public void buyCard(int cardId, ClientHandler clientHandler) throws HearthStoneException {
        Card card = ServerData.getCardById(cardId);
        clients.get(clientHandler.getUsername()).getAccount().buyCards(card, 1);
        HSServer.market.removeCard(card, 1);

        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.buyCardResponse(card, clientHandler);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT, UpdateWaiter.UpdaterType.MARKET_CARDS});
    }

    public void sellCard(int cardId, ClientHandler clientHandler) throws HearthStoneException {
        Card card = ServerData.getCardById(cardId);
        clients.get(clientHandler.getUsername()).getAccount().sellCards(card, 1);
        HSServer.market.addCard(card.copy(), 1);

        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.sellCardResponse(card, clientHandler);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT, UpdateWaiter.UpdaterType.MARKET_CARDS});
    }

    public void removeUpdater(String username, UpdateWaiter.UpdaterType updaterType) {
        for (UpdateWaiter updateWaiter : updaterWaiters) {
            if (updateWaiter.getUsername().equals(username) && updateWaiter.updaterType() == updaterType) {
                updaterWaiters.remove(updateWaiter);
                return;
            }
        }
    }

    public void updateWaiters(UpdateWaiter.UpdaterType[] updaterTypes) {
        if (updaterTypes == null)
            return;
        for (UpdateWaiter updateWaiter : this.updaterWaiters) {
            for (int i = 0; i < updaterTypes.length; i++) {
                if (updateWaiter.updaterType() == updaterTypes[i]) {
                    updateWaiter.update();
                }
            }
        }
    }

    public void updateAccount(ClientHandler clientHandler) {
        ServerMapper.updateAccount(ServerData.getClientDetails(clientHandler.getUsername()).getAccount(), clientHandler);
    }

    public void marketCardsInitialCard(ClientHandler clientHandler) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(market.getCards());

        ServerMapper.marketCardsResponse(cards, clientHandler);
    }

    public void startUpdateMarketCards(ClientHandler clientHandler) {
        updaterWaiters.add(new MarketCardsUpdater(clientHandler.getUsername(),
                UpdateWaiter.UpdaterType.MARKET_CARDS,
                clientHandler));
    }

    public void updateMarketCards(ClientHandler clientHandler) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(market.getCards());

        ServerMapper.updateMarketCards(cards, clientHandler);
    }

    public void stopUpdateMarketCards(ClientHandler clientHandler) {
        removeUpdater(clientHandler.getUsername(), UpdateWaiter.UpdaterType.MARKET_CARDS);
    }

    public void statusBestDecks(ClientHandler clientHandler) {
        ServerMapper.statusDecksResponse(clients.get(clientHandler.getUsername()).getAccount().getBestDecks(10), clientHandler);
    }

    public void selectHero(String heroName, ClientHandler clientHandler) {
        clients.get(clientHandler.getUsername()).getAccount().selectHero(heroName);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.selectHeroResponse(clientHandler);
    }

    public void createNewDeck(Deck deck, String heroName, ClientHandler clientHandler) throws HearthStoneException {
        clients.get(clientHandler.getUsername()).getAccount().createDeck(deck, heroName);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.createNewDeckResponse(heroName, clientHandler);
    }

    public void selectDeck(String heroName, String deckName, ClientHandler clientHandler) {
        clients.get(clientHandler.getUsername()).getAccount().selectDeck(heroName, deckName);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.selectDeckResponse(heroName, clientHandler);
    }

    public void removeDeck(String heroName, String deckName, ClientHandler clientHandler) {
        clients.get(clientHandler.getUsername()).getAccount().removeDeck(heroName, deckName);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.removeDeckResponse(heroName, clientHandler);
    }

    public void addCardToDeck(String heroName, String deckName, int cardId, int cnt, ClientHandler clientHandler) throws HearthStoneException {
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        Card card = account.addCardToDeck(heroName, deckName, cardId, account.getCollection(),
                account.getUnlockedCards(), cnt);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
        DataBase.save(account);

        ServerMapper.addCardToDeckResponse(card, clientHandler);
    }

    public void removeCardFromDeck(String heroName, String deckName, int cardId, int cnt, ClientHandler clientHandler) throws HearthStoneException {
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        Card card = account.removeCardFromDeck(heroName, deckName, cardId, cnt);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
        DataBase.save(account);

        ServerMapper.removeCardFromDeckResponse(card, clientHandler);
    }

    public ClientHandler getClientHandlerByPlayer(Player player) {
        return clients.get(player.getUsername()).getClientHandler();
    }

    public int makeNewPlayerId(Player player){
        synchronized (playersLock) {
            players.put(players.size(), player);
            return players.size() - 1;
        }
    }

    public Player getPlayer(int playerId){
        synchronized (playersLock) {
            return players.get(playerId);
        }
    }

    public String getPlayerName(int playerId){
        synchronized (playersLock) {
            return players.get(playerId).getUsername();
        }
    }

    public void updateGameRequest(int playerId) {
        String username;
        synchronized (playersLock) {
            username = players.get(playerId).getUsername();
        }

        String username0 = clients.get(username).getClientHandler().getGame().getPlayerById(0).getUsername();
        String username1 = clients.get(username).getClientHandler().getGame().getPlayerById(1).getUsername();

        Player player0 = clients.get(username0).getClientHandler().getPlayer();
        Player player1 = clients.get(username1).getClientHandler().getPlayer();

        player0.updatePlayer();
        player1.updatePlayer();

        ServerMapper.updateBoardRequest(player0, player1, clients.get(username0).getClientHandler());
        ServerMapper.updateBoardRequest(player1, player0, clients.get(username1).getClientHandler());
    }

    public void startGameOnGui(int playerId) {
        String username;
        synchronized (playersLock) {
            username = players.get(playerId).getUsername();
        }

        String username0 = clients.get(username).getClientHandler().getGame().getPlayerById(0).getUsername();
        String username1 = clients.get(username).getClientHandler().getGame().getPlayerById(1).getUsername();

        Player player0 = clients.get(username0).getClientHandler().getPlayer();
        Player player1 = clients.get(username1).getClientHandler().getPlayer();

        ServerMapper.startGameOnGuiRequest(player0, player1, clients.get(username0).getClientHandler());
        ServerMapper.startGameOnGuiRequest(player1, player0, clients.get(username1).getClientHandler());
    }

    public void animateSpellRequest(int playerId, Card card) {
        Player player = getPlayer(playerId);
        ClientHandler clientHandler = clients.get(player.getUsername()).getClientHandler();
        ServerMapper.animateSpellRequest(playerId, card, clientHandler);
    }

    public void deleteMouseWaitingRequest(int playerId) {
        Player player = getPlayer(playerId);
        ClientHandler clientHandler = clients.get(player.getUsername()).getClientHandler();
        ServerMapper.deleteMouseWaitingRequest(clientHandler);
    }

    public void createMouseWaiting(int playerId, CursorType cursorType, Card card) {
        Player player = getPlayer(playerId);
        ClientHandler clientHandler = clients.get(player.getUsername()).getClientHandler();
        ServerMapper.createMouseWaitingRequest(cursorType, card, clientHandler);
    }
}
