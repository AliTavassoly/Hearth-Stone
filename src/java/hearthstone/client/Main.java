package hearthstone.client;

import hearthstone.client.data.GUIConfigs;

public class Main {
    static String serverIP = "localhost";
    static int serverPort = 8000;

    public static void main(String[] args) {
        try{
            GUIConfigs.loadConfigs();
        } catch(Exception e){
            e.printStackTrace();;
        }
        HSClient.makeNewClient(serverIP, serverPort).start();
    }
}
