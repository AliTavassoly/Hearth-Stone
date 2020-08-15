package hearthstone.client;

import hearthstone.client.data.ClientData;
import hearthstone.client.network.HSClient;

import java.io.File;
import java.util.Scanner;

public class Main {
    static String serverIP = "localhost";
    static int serverPort = 8000;

    public static void main(String[] args) {
        if (args.length > 0) {
            makePort(args[0]);
        }
        if(args.length > 1) {
            makeIP(args[1]);
        }

        try {
            ClientData.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HSClient.makeNewClient(serverIP, serverPort).start();
    }

    private static void makePort(String path) {
        File portFile = new File(path);
        if (!portFile.exists())
            return;

        try (Scanner fileReader = new Scanner(portFile)) {
            String portString = fileReader.nextLine();
            if (portString != null && portString.length() > 0 && portString.length() < 10) {
                serverPort = Integer.valueOf(portString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void makeIP(String path){
        File ipFile = new File(path);
        if (!ipFile.exists())
            return;

        try (Scanner fileReader = new Scanner(ipFile)) {
            String ipString = fileReader.nextLine();
            if (ipString != null) {
                serverIP = ipString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
