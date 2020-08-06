package hearthstone.server.network;

import hearthstone.HearthStone;
import hearthstone.models.Account;
import hearthstone.models.card.Card;
import hearthstone.server.data.DataBase;
import hearthstone.server.data.ServerData;
import hearthstone.server.logic.Market;
import hearthstone.server.model.updaters.AccountUpdater;
import hearthstone.server.model.ClientDetails;
import hearthstone.server.model.updaters.MarketCardsUpdater;
import hearthstone.server.model.UpdateWaiter;
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

    private ArrayList<UpdateWaiter> updaterWaiters;

    public static Market market;

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
        Card card = HearthStone.getCardById(cardId);
        clients.get(clientHandler.getUsername()).getAccount().buyCards(card, 1);
        HSServer.market.removeCard(card, 1);

        DataBase.save(clients.get(clientHandler.getUsername()).getAccount());

        ServerMapper.buyCardResponse(card, clientHandler);

        updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT, UpdateWaiter.UpdaterType.MARKET_CARDS});
    }

    public void sellCard(int cardId, ClientHandler clientHandler) throws HearthStoneException{
        Card card = HearthStone.getCardById(cardId);
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
}
