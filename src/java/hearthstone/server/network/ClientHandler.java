package hearthstone.server.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hearthstone.models.Packet;
import hearthstone.server.data.ServerData;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket socket;
    private String username;

    public String getUsername(){
        return username;
    }

    public ClientHandler(){}

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        System.out.println("server started to read client ... : ");

        try {
            while (true) {
                String message = scanner.nextLine();

                System.out.println(message + " received in server!");

                ServerMapper.invokeFunction(getPacket(message), this);
            }
        } catch (Exception e) {
            // HSServer.getInstance().clientHandlerDisconnected(authToken);
            // Disconnected
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet packet) {
        try {
            String objectString;

            objectString = ServerData.getNetworkMapper().writeValueAsString(packet);

            System.out.println("From server: " + objectString);

            new PrintStream(socket.getOutputStream()).println(objectString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Packet getPacket(String message) {
        ObjectMapper mapper = ServerData.getNetworkMapper();
        Packet packet = null;
        try {
            packet = mapper.readValue(message, Packet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public void clientSignedIn(String username){
        this.username = username;
    }

    public void clientSignedOut(){
        this.username = null;
    }

    public void clientDisconnected(){

    }
}
