package hearthstone.server.network;

import hearthstone.client.ClientMapper;
import hearthstone.client.HSClient;
import hearthstone.models.Packet;
import hearthstone.server.model.ClientDetails;
import hearthstone.util.HearthStoneException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServerMapper {
    private static Packet addClientHandlerToPacket(Packet packet, ClientHandler clientHandler) {
        if (packet.getArgs() == null) {
            packet.setArgs(new Object[]{clientHandler});
        } else {
            Object[] newArgs = new Object[packet.getArgs().length + 1];
            for (int i = 0; i < packet.getArgs().length; i++) {
                newArgs[i] = packet.getArgs()[i];
            }
            newArgs[packet.getArgs().length] = clientHandler;
            packet.setArgs(newArgs);
        }

        return packet;
    }

    public static void invokeFunction(Packet packet, ClientHandler clientHandler) {
        addClientHandlerToPacket(packet, clientHandler);

        for (Method method : ServerMapper.class.getMethods()) {
            if (method.getName().equals(packet.getFunctionName())) {
                try {
                    method.invoke(null, packet.getArgs());
                    break;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteAccountRequest(ClientHandler clientHandler){
        HSServer.getInstance().deleteAccount(clientHandler);
    }

    public static void deleteAccountResponse(ClientHandler clientHandler){
        Packet packet = new Packet("deleteAccountResponse",
                null);
        clientHandler.sendPacket(packet);
    }

    public static void logoutRequest(ClientHandler clientHandler) {
        HSServer.getInstance().logout(clientHandler);
    }

    public static void logoutResponse(ClientHandler clientHandler) {
        Packet packet = new Packet("logoutResponse",
                null);
        clientHandler.sendPacket(packet);
    }

    public static void loginRequest(String username, String password, ClientHandler clientHandler) {
        try {
            HSServer.getInstance().login(username, password, clientHandler);
        } catch (HearthStoneException e) {
            Packet packet = new Packet("showLoginError",
                    new Object[]{e.getMessage()});
            clientHandler.sendPacket(packet);
        }
    }

    public static void loginResponse(String username, ClientHandler clientHandler) {
        Packet packet = new Packet("loginResponse",
                null);
        clientHandler.sendPacket(packet);
    }

    public static void registerRequest(String name, String username, String password, ClientHandler clientHandler) {
        try {
            HSServer.getInstance().register(name, username, password, clientHandler);
        } catch (HearthStoneException e) {
            Packet packet = new Packet("showRegisterError",
                    new Object[]{e.getMessage()});
            clientHandler.sendPacket(packet);
        }
    }

    public static void registerResponse(String username, ClientHandler clientHandler) {
        Packet packet = new Packet("registerResponse",
                null);
        clientHandler.sendPacket(packet);
    }
}
