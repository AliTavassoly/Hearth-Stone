package hearthstone.client;

import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.network.Receiver;
import hearthstone.client.network.Sender;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class HSClient {
    private static HSClient client;

    private Socket socket;
    private Receiver receiver;
    private Sender sender;

    private HSClient(String serverIP, int serverPort){
        try{
            this.socket = new Socket(serverIP, serverPort);

            CredentialsFrame.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HSClient makeNewClient(String serverIP, int serverPort){
        return client = new HSClient(serverIP, serverPort);
    }

    public static HSClient getClient(){
        return client;
    }

    public void start() {
        try {
            InputStream socketInputStream = socket.getInputStream();
            PrintStream socketPrinter = new PrintStream(socket.getOutputStream());

            receiver = new Receiver(socketInputStream);
            sender = new Sender(socketPrinter);

            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}