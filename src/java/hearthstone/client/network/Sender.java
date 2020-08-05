package hearthstone.client.network;

import hearthstone.client.data.Data;
import hearthstone.models.Packet;

import java.io.PrintStream;

public class Sender {
    private PrintStream printStream;

    public Sender(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void sendPacket(Packet packet) {
        try {
            String objectString;

            objectString = Data.getNetworkMapper().writeValueAsString(packet);

            System.out.println(objectString);

            printStream.println(objectString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
