package hearthstone.client;

import hearthstone.client.data.ClientData;
import hearthstone.client.network.HSClient;

public class Main {
    static String serverIP = "localhost";
    static int serverPort = 8000;

    public static void main(String[] args) {
        try{
            ClientData.load();
        } catch(Exception e){
            e.printStackTrace();;
        }
        HSClient.makeNewClient(serverIP, serverPort).start();
    }
}
