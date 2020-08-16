package hearthstone.client;

import hearthstone.client.data.ClientData;
import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.client.network.HSClient;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static String serverIP /*= "localhost"*/;
    public static int serverPort /*= 8000*/;

    public static void main(String[] args) {
        try {
            ClientData.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CredentialsFrame.getNewIpPortInstance();
    }

    public static void createNewClient(String serverIP, Integer serverPort) {
        Main.serverIP = serverIP;
        Main.serverPort = serverPort;

        HSClient.makeNewClient(Main.serverIP, Main.serverPort).start();
    }
}
