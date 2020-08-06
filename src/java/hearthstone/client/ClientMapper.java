package hearthstone.client;

import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.client.gui.credetials.LoginPanel;
import hearthstone.client.gui.credetials.LogisterPanel;
import hearthstone.client.gui.credetials.RegisterPanel;
import hearthstone.models.Packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientMapper {
    public static void deleteAccountRequest(){
        Packet packet = new Packet("deleteAccountRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void deleteAccountResponse(){
        HSClient.getClient().deleteAccount();
    }

    public static void logoutRequest(){
        Packet packet = new Packet("logoutRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void logoutResponse(){
        HSClient.getClient().logout();
    }

    public static void loginRequest(String username, String password){
        Packet packet = new Packet("loginRequest",
                new Object[]{username, password});
        HSClient.sendPacket(packet);
    }

    public static void loginResponse(){
        HSClient.getClient().login();
    }

    public static void registerRequest(String name, String username, String password){
        Packet packet = new Packet("registerRequest",
                new Object[]{name, username, password});
        HSClient.sendPacket(packet);
    }

    public static void registerResponse(){
        HSClient.getClient().register();
    }

    public static void showLoginError(String error){
        ((LoginPanel)CredentialsFrame.getInstance().getContentPane()).showError(error);
    }

    public static void showRegisterError(String error){
        ((RegisterPanel)CredentialsFrame.getInstance().getContentPane()).showError(error);
    }

    public static void invokeFunction(Packet packet) {
        for (Method method : ClientMapper.class.getMethods()) {
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
}
