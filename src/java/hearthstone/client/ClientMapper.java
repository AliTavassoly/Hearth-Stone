package hearthstone.client;

import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.client.gui.credetials.LoginPanel;
import hearthstone.client.gui.credetials.LogisterPanel;
import hearthstone.client.gui.credetials.RegisterPanel;
import hearthstone.client.gui.game.market.MarketPanel;
import hearthstone.models.Account;
import hearthstone.models.Packet;
import hearthstone.models.card.Card;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

    public static void buyCardRequest(int cardId) {
        Packet packet = new Packet("buyCardRequest",
                new Object[]{cardId});
        HSClient.sendPacket(packet);
    }

    public static void buyCardResponse(Card card){
        MarketPanel.getInstance().bought(card);
    }

    public static void sellCardRequest(int cardId) {
        Packet packet = new Packet("sellCardRequest",
                new Object[]{cardId});
        HSClient.sendPacket(packet);
    }

    public static void sellCardResponse(Card card){
        MarketPanel.getInstance().sold(card);
    }


    public static void updateAccount(Account account){
        HSClient.getClient().updateAccount(account);
        System.out.println("Update received to client: " + account);
    }

    public static void startUpdateMarketCards(){
        Packet packet = new Packet("startUpdateMarketCards",
                null);
        HSClient.sendPacket(packet);
    }

    public static void updateMarketCards(ArrayList<Card> cards){
        HSClient.updateMarketCards(cards);
    }

    public static void stopUpdateMarketCards(){
        Packet packet = new Packet("stopUpdateMarketCards",
                null);
        HSClient.sendPacket(packet);
    }

    public static void marketCardsRequest(){
        Packet packet = new Packet("marketCardsRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void marketCardsResponse(ArrayList<Card> cards){
        HSClient.getClient().openMarket(cards);
    }

    public static void showLoginError(String error){
        HSClient.getClient().showLoginError(error);
    }

    public static void showRegisterError(String error){
        HSClient.getClient().showRegisterError(error);
    }

    public static void showMenuError(String error){
        HSClient.getClient().showMenuError(error);
    }
}
