package hearthstone.server.network;

import hearthstone.client.data.ClientData;
import hearthstone.models.*;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.player.AIPlayer;
import hearthstone.models.player.Player;
import hearthstone.models.player.PlayerModel;
import hearthstone.server.data.DataBase;
import hearthstone.server.data.ServerData;
import hearthstone.server.logic.*;
import hearthstone.server.model.GameRequest;
import hearthstone.server.model.GameType;
import hearthstone.server.model.updaters.AccountUpdater;
import hearthstone.server.model.ClientDetails;
import hearthstone.server.model.updaters.MarketCardsUpdater;
import hearthstone.server.model.UpdateWaiter;
import hearthstone.server.model.updaters.RankingUpdater;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Reflection;
import hearthstone.util.timer.HSPeriodTask;
import hearthstone.util.timer.HSPeriodTimer;

import java.io.IOException;
import java.lang.reflect.Constructor;
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

    private ArrayList<GameRequest> waitingForGame;
    private ArrayList<GameRequest> waitingForDeckReaderGame;
    private ArrayList<GameRequest> waitingForTavernBrawlGame;

    private ArrayList<Game> games;
    private final Object gamesLock = new Object();

    private final Object waitingForGameLock = new Object();
    private final Object waitingForDeckReaderGameLock = new Object();
    private final Object waitingForTavernBrawlGameLock = new Object();

    private HSPeriodTimer onlineGameFinder, deckReaderGameFinder, tavernBrawlGameFinder;

    public static Market market = new Market();

    private HSServer(int serverPort) {
        try {
            this.serverSocket = new ServerSocket(serverPort);

            System.out.println("Server Started at: " + serverPort);

            configServer();

            startOnlineMathFinder();

            startDeckReaderMathFinder();

            startTavernBrawlMathFinder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startOnlineMathFinder() {
        onlineGameFinder = new HSPeriodTimer(500, new HSPeriodTask() {
            @Override
            public void startFunction() {
            }

            @Override
            public void periodFunction() {
                synchronized (waitingForGameLock) {
                    for (int i = 0; i < waitingForGame.size(); i++) {
                        for (int j = i + 1; j < waitingForGame.size(); j++) {
                            if (HSServer.getInstance().canMatch(waitingForGame.get(i), waitingForGame.get(j))) {
                                GameRequest request0 = waitingForGame.get(i);
                                GameRequest request1 = waitingForGame.get(j);

                                waitingForGame.remove(request0);
                                waitingForGame.remove(request1);

                                makeNewOnlineGame(request0.getUsername(), request1.getUsername());

                                i--;
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public boolean finishCondition() {
                return false;
            }
        });
        onlineGameFinder.start();
    }

    private void startDeckReaderMathFinder() {
        deckReaderGameFinder = new HSPeriodTimer(500, new HSPeriodTask() {
            @Override
            public void startFunction() {
            }

            @Override
            public void periodFunction() {
                synchronized (waitingForDeckReaderGameLock) {
                    for (int i = 0; i < waitingForDeckReaderGame.size(); i++) {
                        for (int j = i + 1; j < waitingForDeckReaderGame.size(); j++) {
                            if (HSServer.getInstance().canMatch(waitingForDeckReaderGame.get(i), waitingForDeckReaderGame.get(j))) {
                                GameRequest request0 = waitingForDeckReaderGame.get(i);
                                GameRequest request1 = waitingForDeckReaderGame.get(j);

                                waitingForDeckReaderGame.remove(request0);
                                waitingForDeckReaderGame.remove(request1);

                                try {
                                    makeNewDeckReaderGame(request0.getUsername(), request1.getUsername());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                i--;
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public boolean finishCondition() {
                return false;
            }
        });
        deckReaderGameFinder.start();
    }

    private void startTavernBrawlMathFinder() {
        tavernBrawlGameFinder = new HSPeriodTimer(500, new HSPeriodTask() {
            @Override
            public void startFunction() {
            }

            @Override
            public void periodFunction() {
                synchronized (waitingForTavernBrawlGameLock) {
                    for (int i = 0; i < waitingForTavernBrawlGame.size(); i++) {
                        for (int j = i + 1; j < waitingForTavernBrawlGame.size(); j++) {
                            if (HSServer.getInstance().canMatch(waitingForTavernBrawlGame.get(i), waitingForTavernBrawlGame.get(j))) {
                                GameRequest request0 = waitingForTavernBrawlGame.get(i);
                                GameRequest request1 = waitingForTavernBrawlGame.get(j);

                                waitingForTavernBrawlGame.remove(request0);
                                waitingForTavernBrawlGame.remove(request1);

                                try {
                                    makeNewTavernBrawlGame(request0.getUsername(), request1.getUsername());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                i--;
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public boolean finishCondition() {
                return false;
            }
        });
        tavernBrawlGameFinder.start();
    }

    private boolean canMatch(GameRequest request0, GameRequest request1) {
        Account account0 = ServerData.getClientDetails(request0.getUsername()).getAccount();
        Account account1 = ServerData.getClientDetails(request1.getUsername()).getAccount();

        return Math.abs(account0.getCup() - account1.getCup()) <= 100 &&
                Math.abs(account0.getGamesValue() - account1.getGamesValue()) <
                        Math.abs(System.currentTimeMillis() - request0.getRequestTime()) * 50;
    }

    private void configServer() {
        clients = new HashMap<>();
        updaterWaiters = new ArrayList<>();
        waitingForGame = new ArrayList<>();
        waitingForDeckReaderGame = new ArrayList<>();
        waitingForTavernBrawlGame = new ArrayList<>();
        players = new HashMap<>();
        games = new ArrayList<>();
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
            if (clients.get(username).getCurrentGame() != null) {
                clients.get(username).getCurrentGame().getPlayerByUsername(username).getHero().setHealth(0);
                HSPeriodTimer timer = new HSPeriodTimer(500, new HSPeriodTask() {
                    @Override
                    public void startFunction() {
                    }

                    @Override
                    public void periodFunction() {
                    }

                    @Override
                    public boolean finishCondition() {
                        return clients.get(username).getCurrentGame() == null;
                    }
                });
            } else {
                onlineGameCancelRequest(clientHandler);
                deckReaderGameCancelRequest(clientHandler);
                accountDisconnected(clientHandler);
            }
        }
    }

    // CREDENTIAL
    public void login(String username, String password, ClientHandler clientHandler) throws HearthStoneException {
        ServerData.checkAccountCredentials(username, password);

        HSServer.getInstance().accountConnected(username, clientHandler);

        ServerMapper.loginResponse(username, clientHandler);
    }

    public void register(String name, String username, String password, String repPassword, ClientHandler clientHandler) throws HearthStoneException {
        if(!password.equals(repPassword)){
            throw new HearthStoneException("Password does not match!");
        }

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
        synchronized (waitingForGameLock) {
            waitingForGame.add(new GameRequest(clientHandler.getUsername(), System.currentTimeMillis()));
        }
    }

    public void deckReaderGameRequest(ClientHandler clientHandler) {
        synchronized (waitingForDeckReaderGameLock) {
            waitingForDeckReaderGame.add(new GameRequest(clientHandler.getUsername(), System.currentTimeMillis()));
        }
    }

    public void tavernBrawlGameRequest(ClientHandler clientHandler) {
        synchronized (waitingForTavernBrawlGameLock) {
            waitingForTavernBrawlGame.add(new GameRequest(clientHandler.getUsername(), System.currentTimeMillis()));
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
            for (GameRequest gameRequest : waitingForGame) {
                if (gameRequest.getUsername().equals(clientHandler.getUsername())) {
                    waitingForGame.remove(gameRequest);
                    break;
                }
            }
        }
    }

    public void deckReaderGameCancelRequest(ClientHandler clientHandler) {
        synchronized (waitingForDeckReaderGameLock) {
            for (GameRequest gameRequest : waitingForDeckReaderGame) {
                if (gameRequest.getUsername().equals(clientHandler.getUsername())) {
                    waitingForDeckReaderGame.remove(gameRequest);
                    break;
                }
            }
        }
    }

    public void tavernBrawlGameCancelRequest(ClientHandler clientHandler){
        synchronized (waitingForTavernBrawlGameLock) {
            for (GameRequest gameRequest : waitingForTavernBrawlGame) {
                if (gameRequest.getUsername().equals(clientHandler.getUsername())) {
                    waitingForTavernBrawlGame.remove(gameRequest);
                    break;
                }
            }
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

        ServerMapper.onlineGameResponse(PlayerModel.getPlayerModel(player0), PlayerModel.getPlayerModel(player1),
                clients.get(player0.getUsername()).getClientHandler());
        ServerMapper.onlineGameResponse(PlayerModel.getPlayerModel(player1), PlayerModel.getPlayerModel(player0),
                clients.get(player1.getUsername()).getClientHandler());

        synchronized (gamesLock) {
            games.add(game);
        }

        game.start();
    }

    private void makeNewDeckReaderGame(String username0, String username1) throws Exception {
        Player player0 = getFirstPlayerInDeckReader(username0);
        Player player1 = getSecondPlayerInDeckReader(username1);

        Game game = new OnlineGame(player0, player1, makeNewPlayerId(player0), makeNewPlayerId(player1), GameType.DECK_READER_GAME);

        clients.get(username0).setCurrentGame(game);
        clients.get(username1).setCurrentGame(game);

        clients.get(username0).getClientHandler().setGame(game);
        clients.get(username1).getClientHandler().setGame(game);

        ServerMapper.onlineGameResponse(PlayerModel.getPlayerModel(player0), PlayerModel.getPlayerModel(player1),
                clients.get(player0.getUsername()).getClientHandler());
        ServerMapper.onlineGameResponse(PlayerModel.getPlayerModel(player1), PlayerModel.getPlayerModel(player0),
                clients.get(player1.getUsername()).getClientHandler());

        synchronized (gamesLock) {
            games.add(game);
        }

        game.start();
    }

    private void makeNewTavernBrawlGame(String username0, String username1) {
        Player player0 = getTBPlayer(username0);
        Player player1 = getTBPlayer(username1);

        Game game = getTBGame(player0, player1, makeNewPlayerId(player0), makeNewPlayerId(player1), GameType.DECK_READER_GAME);

        clients.get(username0).setCurrentGame(game);
        clients.get(username1).setCurrentGame(game);

        clients.get(username0).getClientHandler().setGame(game);
        clients.get(username1).getClientHandler().setGame(game);

        ServerMapper.onlineGameResponse(PlayerModel.getPlayerModel(player0), PlayerModel.getPlayerModel(player1), clients.get(player0.getUsername()).getClientHandler());
        ServerMapper.onlineGameResponse(PlayerModel.getPlayerModel(player1), PlayerModel.getPlayerModel(player0), clients.get(player1.getUsername()).getClientHandler());

        synchronized (gamesLock) {
            games.add(game);
        }

        game.start();
    }

    private void makeNewPracticeGame(String username, Player player, Player practicePlayer) {
        Game game = new PracticeGame(player, practicePlayer, makeNewPlayerId(player), makeNewPlayerId(practicePlayer), GameType.PRACTICE_GAME);

        clients.get(username).setCurrentGame(game);

        clients.get(username).getClientHandler().setGame(game);

        ServerMapper.practiceGameResponse(PlayerModel.getPlayerModel(player), PlayerModel.getPlayerModel(practicePlayer), clients.get(username).getClientHandler());

        game.start();
    }

    private void makeNewSoloGame(String username, Player player, Player aiPlayer) {
        Game game = new SoloGame(player, aiPlayer, makeNewPlayerId(player), makeNewPlayerId(aiPlayer), GameType.SOLO_GAME);

        clients.get(username).setCurrentGame(game);

        clients.get(username).getClientHandler().setGame(game);

        ServerMapper.soloGameResponse(PlayerModel.getPlayerModel(player), PlayerModel.getPlayerModel(aiPlayer), clients.get(username).getClientHandler());

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

        for (int i = 0; i < accountInfos.size(); i++) {
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
    public void updateGame(int playerId) {
        String username;
        synchronized (playersLock) {
            username = players.get(playerId).getUsername();
        }

        String username0 = clients.get(username).getCurrentGame().getFirstPlayer().getUsername();
        String username1 = clients.get(username).getCurrentGame().getSecondPlayer().getUsername();

        Player player0 = clients.get(username0).getCurrentGame().getFirstPlayer();
        Player player1 = clients.get(username1).getCurrentGame().getSecondPlayer();

        player0.updatePlayer();
        player1.updatePlayer();

        if (isOnlineGame(player0)) {
            ServerMapper.updateBoardRequest(PlayerModel.getPlayerModel(player1), (getSafePlayer(PlayerModel.getPlayerModel(player0))),
                    clients.get(username1).getClientHandler());
            updateGameWatchers(username0);
        }
        if (isOnlineGame(player0))
            ServerMapper.updateBoardRequest(PlayerModel.getPlayerModel(player0), (getSafePlayer(PlayerModel.getPlayerModel(player1))),
                    clients.get(username0).getClientHandler());
        else
            ServerMapper.updateBoardRequest(PlayerModel.getPlayerModel(player0), PlayerModel.getPlayerModel(player1),
                    clients.get(username0).getClientHandler());
    }

    private void updateGameWatchers(String username){
        Game game = clients.get(username).getCurrentGame();
        for(WatcherInfo watcherInfo: game.getWatchers()){
            ServerMapper.viewerUpdateRequest(PlayerModel.getPlayerModel(game.getFirstPlayer()), PlayerModel.getPlayerModel(game.getSecondPlayer()),
                    clients.get(watcherInfo.getUsername()).getClientHandler());
        }
    }

    public void startGameOnGui(int playerId) {
        String username;
        synchronized (playersLock) {
            username = players.get(playerId).getUsername();
        }

        String username0 = clients.get(username).getCurrentGame().getFirstPlayer().getUsername();
        String username1 = clients.get(username).getCurrentGame().getSecondPlayer().getUsername();

        Player player0 = clients.get(username0).getCurrentGame().getFirstPlayer();
        Player player1 = clients.get(username1).getCurrentGame().getSecondPlayer();

        if (isOnlineGame(player0))
            ServerMapper.startGameOnGuiRequest(PlayerModel.getPlayerModel(player1),
                    getSafePlayer(PlayerModel.getPlayerModel(player0)),
                    clients.get(username1).getClientHandler());

        if (isOnlineGame(player0))
            ServerMapper.startGameOnGuiRequest(PlayerModel.getPlayerModel(player0),
                    getSafePlayer(PlayerModel.getPlayerModel(player1)),
                    clients.get(username0).getClientHandler());
        else
            ServerMapper.startGameOnGuiRequest(PlayerModel.getPlayerModel(player0),
                    PlayerModel.getPlayerModel(player1),
                    clients.get(username0).getClientHandler());
    }

    public void animateSpellRequest(int playerId, Card card) {
        Player player = getPlayer(playerId);

        Player player0 = clients.get(player.getUsername()).getCurrentGame().getFirstPlayer();
        Player player1 = clients.get(player.getUsername()).getCurrentGame().getSecondPlayer();

        ServerMapper.animateSpellRequest(card, clients.get(player0.getUsername()).getClientHandler());
        if (isOnlineGame(player0))
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
        updateGame(player0.getPlayerId());
        if (isOnlineGame(player0))
            updateGame(player1.getPlayerId());

        ServerMapper.endGameRequest(clients.get(player0.getUsername()).getClientHandler());
        if (isOnlineGame(player0))
            ServerMapper.endGameRequest(clients.get(player1.getUsername()).getClientHandler());
    }

    public void endTurnGuiResponse(ClientHandler clientHandler) {
        String username0 = clients.get(clientHandler.getUsername()).getCurrentGame().getFirstPlayer().getUsername();
        String username1 = clients.get(clientHandler.getUsername()).getCurrentGame().getSecondPlayer().getUsername();

        ServerMapper.endTurnResponse(clients.get(username0).getClientHandler());

        ServerMapper.endTurnResponse(clients.get(username1).getClientHandler());
    }

    private void updateGameEndedInClients(String username0, String username1) {
        clients.get(username0).getClientHandler().gameEnded();
        if (isOnlineGame(username0))
            clients.get(username1).getClientHandler().gameEnded();
    }

    public void removeGameFromList(Game game) {
        synchronized (gamesLock) {
            games.remove(game);
        }
    }

    public void gameEnded(Player player0, Player player1) {
        if (isOnlineGame(player0))
            removeGameFromList(clients.get(player0.getUsername()).getCurrentGame());

        updateGameEndedInGui(player0, player1);

        updateGameEndedInClients(player0.getUsername(), player1.getUsername());

        if (isOnlineGame(player0))
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

        int cup0 = account0.getCup();
        int cup1 = account1.getCup();

        if (player0.getHero().getHealth() <= 0 && isOnlineGame(player0)) {
            account0.lostGame(player0.getHero().getName(), player0.getDeck().getName(), cup1, clients.get(player0.getUsername()).getCurrentGame().getGameType() != GameType.DECK_READER_GAME);
            account1.wonGame(player1.getHero().getName(), player1.getDeck().getName(), cup0, clients.get(player0.getUsername()).getCurrentGame().getGameType() != GameType.DECK_READER_GAME);
        } else if (isOnlineGame(player0)) {
            account0.wonGame(player0.getHero().getName(), player0.getDeck().getName(), cup0, clients.get(player0.getUsername()).getCurrentGame().getGameType() != GameType.DECK_READER_GAME);
            account1.lostGame(player1.getHero().getName(), player1.getDeck().getName(), cup1, clients.get(player0.getUsername()).getCurrentGame().getGameType() != GameType.DECK_READER_GAME);
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

    private PlayerModel getSafePlayer(PlayerModel playerModel) {
        PlayerModel safePlayerModel = playerModel.copy();

        for (int i = 0; i < safePlayerModel.getHand().size(); i++) {
            Card card = safePlayerModel.getHand().get(i);
            safePlayerModel.getHand().set(i, getInstance().safeCard(card));
        }

        for (int i = 0; i < safePlayerModel.getDeck().getCards().size(); i++) {
            Card card = safePlayerModel.getDeck().getCards().get(i);
            safePlayerModel.getDeck().getCards().set(i, getInstance().safeCard(card));
        }

        return safePlayerModel;
    }

    // SETTINGS
    public void changePassword(String password, ClientHandler clientHandler) {
        Account account = clients.get(clientHandler.getUsername()).getAccount();

        ServerData.changePassword(clientHandler.getUsername(), password);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});

        DataBase.save(account);
    }

    public void changeName(String name, ClientHandler clientHandler) {
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        account.setName(name);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});

        DataBase.save(account);
    }

    public void changeBackCard(int backId, ClientHandler clientHandler) {
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        account.setCardsBackId(backId);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});

        DataBase.save(account);
    }
    // SETTINGS

    private boolean isOnlineGame(String username) {
        GameType gameType = clients.get(username).getCurrentGame().getGameType();
        return gameType == GameType.ONLINE_GAME || gameType == GameType.DECK_READER_GAME;
    }

    private boolean isOnlineGame(Player player) {
        return isOnlineGame(player.getUsername());
    }

    public ArrayList<GameInfo> getGamesList() {
        ArrayList<GameInfo> gameInfos = new ArrayList<>();
        synchronized (gamesLock) {
            for (Game game : games) {
                gameInfos.add(new GameInfo(game.getFirstPlayer().getUsername(), game.getSecondPlayer().getUsername()));
            }
        }
        return gameInfos;
    }

    public ArrayList<WatcherInfo> getWatchers(String username) {
        return clients.get(username).getCurrentGame().getWatchers();
    }

    public void kickWatcher(String kicked, String kicker, ClientHandler clientHandler) {
        clients.get(kicker).getCurrentGame().removeWatcher(kicked);
        ServerMapper.kickWatcherResponse(clients.get(kicked).getClientHandler());

        ServerMapper.watchersResponse(getWatchers(clientHandler.getUsername()), clientHandler);
    }

    public void view(String wantToView, String viewer, ClientHandler clientHandler) {
        clients.get(wantToView).getCurrentGame().addWatcher(viewer);

        ServerMapper.viewResponse(PlayerModel.getPlayerModel(clients.get(wantToView).getCurrentGame().getFirstPlayer()),
                PlayerModel.getPlayerModel(clients.get(wantToView).getCurrentGame().getSecondPlayer()),
                clientHandler);

        Game game = clients.get(wantToView).getCurrentGame();

        String user0 = game.getFirstPlayer().getUsername();
        String user1 = game.getSecondPlayer().getUsername();

        ServerMapper.watchersResponse(getWatchers(user0), clients.get(user0).getClientHandler());
        ServerMapper.watchersResponse(getWatchers(user1), clients.get(user1).getClientHandler());
    }

    public void cancelView(String firstPlayerUsername, String wantToCancel) {
        clients.get(firstPlayerUsername).getCurrentGame().removeWatcher(wantToCancel);
    }

    public Player getTBPlayer(String username){
        Player player = clients.get(username).getAccount().getPlayer();
        Class tbClass = Reflection.getClassByName("./TavernBrawl.jar", "TBPlayer");
        Player tbPlayer = null;

        try {
            Constructor constructor = tbClass.getConstructor(Hero.class, Deck.class, String.class);
            tbPlayer = (Player) constructor.newInstance(player.getHero(), player.getDeck(), player.getUsername());
        } catch (Exception e){
            e.printStackTrace();
        }

        return tbPlayer;
    }

    private Game getTBGame(Player player0, Player player1, int makeNewPlayerId, int makeNewPlayerId1, GameType gameType) {
        Class tbClass = Reflection.getClassByName("./TavernBrawl.jar", "TBGame");
        Game tbGame = null;

        try {
            Constructor constructor = tbClass.getConstructor(Player.class, Player.class, int.class, int.class, GameType.class);
            tbGame = (Game) constructor.newInstance(player0, player1, makeNewPlayerId, makeNewPlayerId1, gameType);
        } catch (Exception e){
            e.printStackTrace();
        }

        return tbGame;
    }
}
