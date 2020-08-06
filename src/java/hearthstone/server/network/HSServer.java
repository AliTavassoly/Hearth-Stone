package hearthstone.server.network;

import hearthstone.models.Account;
import hearthstone.server.data.DataBase;
import hearthstone.server.data.ServerData;
import hearthstone.server.model.ClientDetails;
import hearthstone.util.HearthStoneException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HSServer extends Thread{
    private static HSServer server;

    private ServerSocket serverSocket;

    private Map<String, ClientDetails> clients;

    private HSServer(int serverPort){
        try {
            this.serverSocket = new ServerSocket(serverPort);

            System.out.println("Server Started at: " + serverPort);

            configServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configServer(){
        clients = new HashMap<>();
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

        System.out.println(ServerData.getClientDetails(username));

        clients.put(username, ServerData.getClientDetails(username));

        //updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT_INFO});
    }

    private void accountDisconnected(ClientHandler clientHandler) {
        String username = clientHandler.getUsername();
        clientHandler.clientSignedOut();
        ServerData.getClientDetails(username).setClientHandler(null);
        ServerData.getClientDetails(username).setCurrentGame(null);

        //updateWaiters(new UpdateWaiter.UpdaterType[]{UpdateWaiter.UpdaterType.ACCOUNT_INFO});

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

    /*public void logout(ClientHandler clientHandler) {
        XOServer.getInstance().clientHandlerDisconnected(clientHandler.getAuthToken());
        Mapper.logoutResponse(clientHandler);
    }*/
}
