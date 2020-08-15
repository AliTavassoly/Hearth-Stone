package hearthstone.client.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import hearthstone.client.data.ClientData;
import hearthstone.client.gui.BaseFrame;
import hearthstone.models.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Receiver extends Thread{
    private InputStream inputStream;

    public Receiver(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(inputStream);
            while (true) {
                System.out.println("client started to read ... : ");
                String message = scanner.nextLine();
                // System.out.println(message);

                ObjectMapper mapper = ClientData.getNetworkMapper();

                Packet packet = mapper.readValue(message, Packet.class);

                System.out.println(packet.getFunctionName());

                ClientMapper.invokeFunction(packet);
            }
        } catch (Exception e) {
            BaseFrame.error("Disconnected from server");
            e.printStackTrace();
        }
    }
}
