package hearthstone.server.network;

import hearthstone.models.Account;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.passive.Passive;
import hearthstone.server.data.DataBase;
import hearthstone.server.data.ServerData;
import hearthstone.server.logic.Market;
import hearthstone.server.model.updaters.AccountUpdater;
import hearthstone.server.model.ClientDetails;
import hearthstone.server.model.updaters.MarketCardsUpdater;
import hearthstone.server.model.UpdateWaiter;
import hearthstone.shared.GameConfigs;
import hearthstone.util.HearthStoneException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HSServer extends Thread {
    public static Map<Integer, Card> baseCards = new HashMap<>();
    public static Map<Integer, Hero> baseHeroes = new HashMap<>();
    public static Map<Integer, Passive> basePassives = new HashMap<>();

    public static Card getCardByName(String name){
        for(Card card: baseCards.values()){
            if(card.getName().equals(name)){
                return card.copy();
            }
        }
        return null;
    }

    public static Card getCardById(int cardId){
        for(Card card: baseCards.values()){
            if(card.getId() == cardId){
                return card.copy();
            }
        }
        return null;
    }

    public static Hero getHeroByName(String name){
        for(Hero hero: baseHeroes.values()){
            if(hero.getName().equals(name)){
                return hero.copy();
            }
        }
        return null;
    }

    public static Hero getHeroByType(HeroType heroType){
        for(Hero hero: baseHeroes.values()){
            if(hero.getType() == heroType){
                return hero.copy();
            }
        }
        return null;
    }

    private static HSServer server;

    private ServerSocket serverSocket;

    private Map<String, ClientDetails> clients;

    private ArrayList<UpdateWaiter> updaterWaiters;

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
        ServerMapper.createBaseCardsResponse(baseCards, clientHandler);
    }

    public void createBaseHeroes(ClientHandler clientHandler) {
        ServerMapper.createBaseHeroesResponse(baseHeroes, clientHandler);
    }

    public void createBasePassives(ClientHandler clientHandler) {
        ServerMapper.createBasePassivesResponse(basePassives, clientHandler);
    }


    private void accountConnected(String username, ClientHandler clientHandler) {
        ServerData.getClientDetails(username).setClientHandler(clientHandler);

        clientHandler.clientSignedIn(username);

        updaterWaiters.add(new AccountUpdater(clientHandler.getUsername(), UpdateWaiter.UpdaterType.ACCOUNT, clientHandler));

        System.out.println("Username connected: " + ServerData.getClientDetails(username));

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

    public void buyCard(int cardId, ClientHandler clientHandler) throws HearthStoneException {
        Card card = HSServer.getCardById(cardId);
        clients.get(clientHandler.getUsername()).getAccount().buyCards(card, 1);
        HSServer.market.removeCard(card, 1);

        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.buyCardResponse(card, clientHandler);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT, UpdateWaiter.UpdaterType.MARKET_CARDS});
    }

    public void sellCard(int cardId, ClientHandler clientHandler) throws HearthStoneException{
        Card card = HSServer.getCardById(cardId);
        clients.get(clientHandler.getUsername()).getAccount().sellCards(card, 1);
        HSServer.market.addCard(card.copy(), 1);

        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.sellCardResponse(card, clientHandler);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT, UpdateWaiter.UpdaterType.MARKET_CARDS});
    }

    public void removeUpdater(String username, UpdateWaiter.UpdaterType updaterType){
        for(UpdateWaiter updateWaiter: updaterWaiters){
            if(updateWaiter.getUsername().equals(username) && updateWaiter.updaterType() == updaterType){
                updaterWaiters.remove(updateWaiter);
                System.out.println("Updater " + updaterType + " removed!" + " size: " + updaterWaiters.size());
                return;
            }
        }
    }

    public void updateWaiters(UpdateWaiter.UpdaterType[] updaterTypes) {
        if(updaterTypes == null)
            return;
        for(UpdateWaiter updateWaiter: this.updaterWaiters){
            for(int i = 0; i < updaterTypes.length; i++){
                if(updateWaiter.updaterType() == updaterTypes[i]){
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

        System.out.println("market initial cards size: " + cards.size());

        ServerMapper.marketCardsResponse(cards, clientHandler);
    }

    public void startUpdateMarketCards(ClientHandler clientHandler) {
        System.out.println("Market updated started: " + clientHandler.getUsername());

        updaterWaiters.add(new MarketCardsUpdater(clientHandler.getUsername(),
                UpdateWaiter.UpdaterType.MARKET_CARDS,
                clientHandler));
    }

    public void updateMarketCards(ClientHandler clientHandler) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(market.getCards());

        System.out.println("market updated: " + cards.size());

        ServerMapper.updateMarketCards(cards, clientHandler);
    }

    public void stopUpdateMarketCards(ClientHandler clientHandler) {
        System.out.println("Market updated stopped: " + clientHandler.getUsername());
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

    public void createNewDeck(Deck deck, String heroName, ClientHandler clientHandler) throws HearthStoneException{
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

    public void addCardToDeck(String heroName, String deckName, int cardId, int cnt, ClientHandler clientHandler) throws HearthStoneException{
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        Card card = account.addCardToDeck(heroName, deckName, cardId, account.getCollection(),
                account.getUnlockedCards(), cnt);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
        DataBase.save(account);

        ServerMapper.addCardToDeckResponse(card, clientHandler);
    }

    public void removeCardFromDeck(String heroName, String deckName, int cardId, int cnt, ClientHandler clientHandler) throws HearthStoneException{
        Account account = clients.get(clientHandler.getUsername()).getAccount();
        Card card = account.removeCardFromDeck(heroName, deckName, cardId, cnt);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT});
        DataBase.save(account);

        ServerMapper.removeCardFromDeckResponse(card, clientHandler);
    }
}
