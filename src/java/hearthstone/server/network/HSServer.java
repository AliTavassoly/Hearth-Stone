package hearthstone.server.network;

import hearthstone.client.data.ClientData;
import hearthstone.client.network.HSClient;
import hearthstone.models.Account;
import hearthstone.models.AccountCredential;
import hearthstone.models.AccountInfo;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.hero.HeroType;
import hearthstone.models.player.AIPlayer;
import hearthstone.models.player.Player;
import hearthstone.server.data.DataBase;
import hearthstone.server.data.ServerData;
import hearthstone.server.logic.*;
import hearthstone.server.model.GameType;
import hearthstone.server.model.updaters.AccountUpdater;
import hearthstone.server.model.ClientDetails;
import hearthstone.server.model.updaters.MarketCardsUpdater;
import hearthstone.server.model.UpdateWaiter;
import hearthstone.server.model.updaters.RankingUpdater;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class HSServer extends Thread {
    private static HSServer server;

    private ServerSocket serverSocket;

    private Map<String, ClientDetails> clients;

    private Map<Integer, Player> players;
    private final Object playersLock = new Object();

    private ArrayList<UpdateWaiter> updaterWaiters;

    private ArrayList<String> waitingForGame;
    private ArrayList<String> waitingForDeckReaderGame;

    private final Object waitingForGameLock = new Object();
    private final Object waitingForDeckReaderGameLock = new Object();

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

    public void endTurnGuiResponse(ClientHandler clientHandler) {
        String username0 = clients.get(clientHandler.getUsername()).getClientHandler().getGame().getFirstPlayer().getUsername();
        String username1 = clients.get(clientHandler.getUsername()).getClientHandler().getGame().getSecondPlayer().getUsername();

        ServerMapper.endTurnResponse(clients.get(username0).getClientHandler());

        ServerMapper.endTurnResponse(clients.get(username1).getClientHandler());
    }

    private void configServer() {
        clients = new HashMap<>();
        updaterWaiters = new ArrayList<>();
        waitingForGame = new ArrayList<>();
        waitingForDeckReaderGame = new ArrayList<>();
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

    // BASES
    public void createBaseCards(ClientHandler clientHandler) {
        ServerMapper.createBaseCardsResponse(ServerData.baseCards, clientHandler);
    }

    public void createBaseHeroes(ClientHandler clientHandler) {
        ServerMapper.createBaseHeroesResponse(ServerData.baseHeroes, clientHandler);
    }

    public void createBasePassives(ClientHandler clientHandler) {
        ServerMapper.createBasePassivesResponse(ServerData.basePassives, clientHandler);
    }
    // BASES

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
            for (UpdateWaiter.UpdaterType updaterType : updaterTypes) {
                if (updateWaiter.updaterType() == updaterType) {
                    updateWaiter.update();
                    break;
                }
            }
        }
    }

    public static ArrayList<Card> getCardsArrayFromName(ArrayList<String> cardsName) {
        ArrayList<Card> ans = new ArrayList<>();

        for (int i = 0; i < cardsName.size(); i++) {
            ans.add(ServerData.getCardByName(cardsName.get(i)));
        }

        return ans;
    }

    private void accountConnected(String username, ClientHandler clientHandler) {
        ServerData.getClientDetails(username).setClientHandler(clientHandler);

        clientHandler.clientSignedIn(username);

        updaterWaiters.add(new AccountUpdater(clientHandler.getUsername(), UpdateWaiter.UpdaterType.ACCOUNT, clientHandler));

        clients.put(username, ServerData.getClientDetails(username));

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT, UpdateWaiter.UpdaterType.RANKING});
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

    // CREDENTIAL
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
    // CREDENTIAL

    // GAME REQUEST
    public void onlineGameRequest(ClientHandler clientHandler) {
        String username0 = null, username1 = null;

        synchronized (waitingForGameLock) {
            waitingForGame.add(clientHandler.getUsername());
            if (waitingForGame.size() >= 2) {
                username1 = waitingForGame.remove(waitingForGame.size() - 1);
                username0 = waitingForGame.remove(0);
            }
        }

        makeNewOnlineGame(username0, username1);
    }

    public void deckReaderGameRequest(ClientHandler clientHandler) {
        String username0 = null, username1 = null;

        synchronized (waitingForDeckReaderGameLock) {
            waitingForDeckReaderGame.add(clientHandler.getUsername());
            if (waitingForDeckReaderGame.size() >= 2) {
                username1 = waitingForDeckReaderGame.remove(waitingForDeckReaderGame.size() - 1);
                username0 = waitingForDeckReaderGame.remove(0);
            }
        }

        if (username0 != null && username1 != null) {
            try {
                makeNewDeckReaderGame(username0, username1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void practiceGameRequest(ClientHandler clientHandler) {
        Player player = clients.get(clientHandler.getUsername()).getAccount().getPlayer();
        Player practicePlayer = clients.get(clientHandler.getUsername()).getAccount().getPlayer();

        makeNewPracticeGame(clientHandler.getUsername(), player, practicePlayer);
    }

    public void soloGameRequest(ClientHandler clientHandler) {
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        Player player = account.getPlayer();
        Player aiPlayer = new AIPlayer(account.getSelectedHero(), account.getSelectedHero().getSelectedDeck(), account.getUsername());

        makeNewSoloGame(clientHandler.getUsername(), player, aiPlayer);
    }
    // GAME REQUEST

    // CANCEL GAME
    public void onlineGameCancelRequest(ClientHandler clientHandler) {
        synchronized (waitingForGameLock) {
            waitingForGame.remove(clientHandler.getUsername());
        }
    }

    public void deckReaderGameCancelRequest(ClientHandler clientHandler) {
        synchronized (waitingForDeckReaderGameLock) {
            waitingForDeckReaderGame.remove(clientHandler.getUsername());
        }
    }
    // CANCEL GAME

    // MAKE GAME
    public void makeNewOnlineGame(String username0, String username1) {
        Player player0 = clients.get(username0).getAccount().getPlayer();
        Player player1 = clients.get(username1).getAccount().getPlayer();

        Game game = new OnlineGame(player0, player1, makeNewPlayerId(player0), makeNewPlayerId(player1), GameType.ONLINE_GAME);

        clients.get(username0).setCurrentGame(game);
        clients.get(username1).setCurrentGame(game);

        clients.get(player0.getUsername()).getClientHandler().setGame(game);
        clients.get(player1.getUsername()).getClientHandler().setGame(game);

        ServerMapper.onlineGameResponse(player0, player1, clients.get(player0.getUsername()).getClientHandler());
        ServerMapper.onlineGameResponse(player1, player0, clients.get(player1.getUsername()).getClientHandler());

        game.start();
    }

    private void makeNewDeckReaderGame(String username0, String username1) throws Exception {
        Player player0 = getFirstPlayerInDeckReader(username0);
        Player player1 = getSecondPlayerInDeckReader(username1);

        Game game = new OnlineGame(player0, player1, makeNewPlayerId(player0), makeNewPlayerId(player1), GameType.ONLINE_GAME);

        clients.get(username0).setCurrentGame(game);
        clients.get(username1).setCurrentGame(game);

        clients.get(player0.getUsername()).getClientHandler().setGame(game);
        clients.get(player1.getUsername()).getClientHandler().setGame(game);

        ServerMapper.onlineGameResponse(player0, player1, clients.get(player0.getUsername()).getClientHandler());
        ServerMapper.onlineGameResponse(player1, player0, clients.get(player1.getUsername()).getClientHandler());

        game.start();
    }

    private void makeNewPracticeGame(String username, Player player, Player practicePlayer) {
        Game game = new PracticeGame(player, practicePlayer, makeNewPlayerId(player), makeNewPlayerId(practicePlayer), GameType.PRACTICE_GAME);

        clients.get(username).setCurrentGame(game);

        clients.get(username).getClientHandler().setGame(game);

        ServerMapper.practiceGameResponse(player, practicePlayer, clients.get(username).getClientHandler());

        game.start();
    }

    private void makeNewSoloGame(String username, Player player, Player aiPlayer) {
        Game game = new SoloGame(player, aiPlayer, makeNewPlayerId(player), makeNewPlayerId(aiPlayer), GameType.SOLO_GAME);

        clients.get(username).setCurrentGame(game);

        clients.get(username).getClientHandler().setGame(game);

        ServerMapper.soloGameResponse(player, aiPlayer, clients.get(username).getClientHandler());

        game.start();
    }
    // MAKE GAME

    // MARKET
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
    // MARKET

    public void updateAccount(ClientHandler clientHandler) {
        ServerMapper.updateAccount(ServerData.getClientDetails(clientHandler.getUsername()).getAccount(), clientHandler);
    }

    // RANKING
    public void startUpdateRanking(ClientHandler clientHandler) {
        updaterWaiters.add(new RankingUpdater(clientHandler.getUsername(),
                UpdateWaiter.UpdaterType.RANKING,
                clientHandler));
    }

    public void stopUpdateRanking(ClientHandler clientHandler) {
        removeUpdater(clientHandler.getUsername(), UpdateWaiter.UpdaterType.RANKING);
    }

    public void updateRanking(ClientHandler clientHandler) {
        ServerMapper.updateRanking(getTopRanks(), getNearRanks(clientHandler.getUsername()), clientHandler);
    }

    private ArrayList<AccountInfo> getNearRanks(String username) {
        ArrayList<AccountInfo> allAccountInfos = new ArrayList<>();
        ArrayList<AccountInfo> res = new ArrayList<>();

        for (AccountCredential accountCredential : ServerData.getAccounts().values()) {
            Account account = ServerData.getClientDetails(accountCredential.getUsername()).getAccount();
            allAccountInfos.add(new AccountInfo(account.getUsername(), account.getCup()));
        }

        Collections.sort(allAccountInfos);

        for (int i = 0; i < allAccountInfos.size(); i++) {
            if (allAccountInfos.get(i).getUsername().equals(username)) {
                for (int j = i - 5; j <= i - 1; j++) {
                    if (j >= 0) {
                        res.add(allAccountInfos.get(j));
                        allAccountInfos.get(j).setRank(j + 1);
                    }
                }

                allAccountInfos.get(i).setRank(i + 1);
                res.add(allAccountInfos.get(i));

                for (int j = i + 1; j <= i + 5; j++) {
                    if (j < allAccountInfos.size()) {
                        res.add(allAccountInfos.get(j));
                        allAccountInfos.get(j).setRank(j + 1);
                    }
                }
                return res;
            }
        }

        return res;
    }

    private ArrayList<AccountInfo> getTopRanks() {
        ArrayList<AccountInfo> accountInfos = new ArrayList<>();

        for (AccountCredential accountCredential : ServerData.getAccounts().values()) {
            Account account = ServerData.getClientDetails(accountCredential.getUsername()).getAccount();
            accountInfos.add(new AccountInfo(account.getUsername(), account.getCup()));
        }

        Collections.sort(accountInfos);

        while (accountInfos.size() > 10)
            accountInfos.remove(accountInfos.size() - 1);

        for (int i = 0; i < accountInfos.size(); i++){
            accountInfos.get(i).setRank(i + 1);
        }

        return accountInfos;
    }

    public void initialRanks(ClientHandler clientHandler) {
        ServerMapper.rankingResponse(getTopRanks(), getNearRanks(clientHandler.getUsername()), clientHandler);
    }
    // RANKING

    // STATUS
    public void statusBestDecks(ClientHandler clientHandler) {
        ServerMapper.statusDecksResponse(clients.get(clientHandler.getUsername()).getAccount().getBestDecks(10), clientHandler);
    }
    //STATUS

    // COLLECTION
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
    // COLLECTION

    // MIDDLE OF GAME
    public void updateGameRequest(int playerId) {
        String username;
        synchronized (playersLock) {
            username = players.get(playerId).getUsername();
        }

        String username0 = clients.get(username).getClientHandler().getGame().getFirstPlayer().getUsername();
        String username1 = clients.get(username).getClientHandler().getGame().getSecondPlayer().getUsername();

        Player player0 = clients.get(username0).getClientHandler().getGame().getFirstPlayer();
        Player player1 = clients.get(username1).getClientHandler().getGame().getSecondPlayer();

        player0.updatePlayer();
        player1.updatePlayer();

        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            ServerMapper.updateBoardRequest(player1, getSafePlayer(player0), clients.get(username1).getClientHandler());

        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            ServerMapper.updateBoardRequest(player0, getSafePlayer(player1), clients.get(username0).getClientHandler());
        else
            ServerMapper.updateBoardRequest(player0, player1, clients.get(username0).getClientHandler());
    }

    public void startGameOnGui(int playerId) {
        String username;
        synchronized (playersLock) {
            username = players.get(playerId).getUsername();
        }

        String username0 = clients.get(username).getClientHandler().getGame().getFirstPlayer().getUsername();
        String username1 = clients.get(username).getClientHandler().getGame().getSecondPlayer().getUsername();

        Player player0 = clients.get(username0).getClientHandler().getGame().getFirstPlayer();
        Player player1 = clients.get(username1).getClientHandler().getGame().getSecondPlayer();

        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            ServerMapper.startGameOnGuiRequest(player1, getSafePlayer(player0), clients.get(username1).getClientHandler());

        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            ServerMapper.startGameOnGuiRequest(player0, getSafePlayer(player1), clients.get(username0).getClientHandler());
        else
            ServerMapper.startGameOnGuiRequest(player0, player1, clients.get(username0).getClientHandler());
    }

    public void animateSpellRequest(int playerId, Card card) {
        Player player = getPlayer(playerId);

        Player player0 = clients.get(player.getUsername()).getCurrentGame().getFirstPlayer();
        Player player1 = clients.get(player.getUsername()).getCurrentGame().getSecondPlayer();

        ServerMapper.animateSpellRequest(card, clients.get(player0.getUsername()).getClientHandler());
        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            ServerMapper.animateSpellRequest(card, clients.get(player1.getUsername()).getClientHandler());
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

    public void chooseCardAbilityRequest(int cardGameId, ArrayList<Card> cards, int playerId) {
        ServerMapper.chooseCardAbilityRequest(cardGameId, cards, clients.get(getPlayer(playerId).getUsername()).getClientHandler());
    }

    private void updateGameEndedInGui(Player player0, Player player1) {
        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            player1 = getSafePlayer(player1);

        updateGameRequest(player0.getPlayerId());
        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            updateGameRequest(player1.getPlayerId());

        ServerMapper.endGameRequest(clients.get(player0.getUsername()).getClientHandler());
        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            ServerMapper.endGameRequest(clients.get(player1.getUsername()).getClientHandler());
    }

    private void updateGameEndedInClients(String username0, String username1) {
        clients.get(username0).getClientHandler().gameEnded();
        if (clients.get(username0).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            clients.get(username1).getClientHandler().gameEnded();
    }

    public void gameEnded(Player player0, Player player1) {
        updateGameEndedInGui(player0, player1);

        updateGameEndedInClients(player0.getUsername(), player1.getUsername());

        if (clients.get(player0.getUsername()).getCurrentGame().getGameType() == GameType.ONLINE_GAME)
            updateGameEndedInAccounts(player0, player1);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT, UpdateWaiter.UpdaterType.RANKING});
    }
    // MIDDLE OF GAME

    public ClientHandler getClientHandlerByPlayer(Player player) {
        return clients.get(player.getUsername()).getClientHandler();
    }

    public int makeNewPlayerId(Player player) {
        synchronized (playersLock) {
            players.put(players.size(), player);
            return players.size() - 1;
        }
    }

    public Player getPlayer(int playerId) {
        synchronized (playersLock) {
            return players.get(playerId);
        }
    }

    public String getPlayerName(int playerId) {
        synchronized (playersLock) {
            return players.get(playerId).getUsername();
        }
    }

    private void updateGameEndedInAccounts(Player player0, Player player1) {
        Account account0 = clients.get(player0.getUsername()).getAccount();
        Account account1 = clients.get(player1.getUsername()).getAccount();

        if (player0.getHero().getHealth() <= 0) {
            account0.lostGame(player0.getHero().getName(), player0.getDeck().getName(), account1.getCup());
            account1.wonGame(player1.getHero().getName(), player1.getDeck().getName(), account0.getCup());
        } else {
            account0.wonGame(player0.getHero().getName(), player0.getDeck().getName(), account1.getCup());
            account1.lostGame(player1.getHero().getName(), player1.getDeck().getName(), account0.getCup());
        }

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});

        DataBase.save(account0);
        DataBase.save(account1);
    }

    private Player getFirstPlayerInDeckReader(String username) throws Exception {
        Map<String, ArrayList<String>> decks = DataBase.getDecks();

        if (decks == null)
            throw new HearthStoneException("There is a problem in deck reader!");

        Player player = new Player(Objects.requireNonNull(ServerData.getHeroByName("Mage")),
                new Deck("Deck1", HeroType.MAGE,
                        ClientData.getCardsArrayFromName(decks.get("friend"))),
                username);

        return player;
    }

    private Player getSecondPlayerInDeckReader(String username) throws Exception {
        Map<String, ArrayList<String>> decks = DataBase.getDecks();

        if (decks == null)
            throw new HearthStoneException("There is a problem in deck reader!");

        Player player = new Player(Objects.requireNonNull(ServerData.getHeroByName("Mage")),
                new Deck("Deck2", HeroType.MAGE,
                        ClientData.getCardsArrayFromName(decks.get("enemy"))),
                username);

        return player;
    }

    private Card safeCard(Card card) {
        Card safeCard = ServerData.getCardByName("Empty Card");
        safeCard.setCardGameId(card.getCardGameId());
        safeCard.setPlayerId(card.getPlayerId());
        safeCard.setEnemyPlayerId(card.getEnemyPlayerId());
        return safeCard;
    }

    private Player getSafePlayer(Player player) {
        Player safePlayer = player.copy();

        for (int i = 0; i < safePlayer.getHand().size(); i++) {
            Card card = safePlayer.getHand().get(i);
            safePlayer.getHand().set(i, getInstance().safeCard(card));
        }

        for (int i = 0; i < safePlayer.getDeck().getCards().size(); i++) {
            Card card = safePlayer.getDeck().getCards().get(i);
            safePlayer.getDeck().getCards().set(i, getInstance().safeCard(card));
        }

        for (int i = 0; i < safePlayer.getOriginalDeck().getCards().size(); i++) {
            Card card = safePlayer.getOriginalDeck().getCards().get(i);
            safePlayer.getOriginalDeck().getCards().set(i, getInstance().safeCard(card));
        }
        return safePlayer;
    }

    // SETTINGS
    public void changePassword(String password, ClientHandler clientHandler) {
        Account account = clients.get(clientHandler.getUsername()).getAccount();

        ServerData.changePassword(clientHandler.getUsername(), password);

        DataBase.save(account);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
    }

    public void changeName(String name, ClientHandler clientHandler) {
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        account.setName(name);

        DataBase.save(account);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
    }

    public void changeBackCard(int backId, ClientHandler clientHandler) {
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        account.setCardsBackId(backId);

        DataBase.save(account);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
    }
    // SETTINGS
}
