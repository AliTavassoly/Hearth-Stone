package hearthstone.server;

import hearthstone.server.data.DataBase;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;

public class Main {
    public static int serverPort = 8000;
    public static String dataPath = "data";

    public static void main(String[] args) {
        try {
            DataBase.load();
        } catch (Exception e){
            e.printStackTrace();
        }
        HSServer.makeNewInstance(serverPort).start();
    }
}