package hearthstone.client.network;

import hearthstone.client.data.ClientData;
import hearthstone.client.gui.game.market.MarketPanel;
import hearthstone.models.Account;
import hearthstone.models.Deck;
import hearthstone.models.Packet;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;
import hearthstone.shared.GameConfigs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class ClientMapper {
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

    public static void updateAccount(Account account){
        HSClient.getClient().updateAccount(account);
        System.out.println("Update received to client: " + account);
    }

    // CREATE BASE
    public static void createBaseCardsRequest(){
        Packet packet = new Packet("createBaseCardsRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void createBaseCardsResponse(Map<Integer, Card> baseCards){
        ClientData.baseCards = baseCards;
    }

    public static void createBaseHeroesRequest(){
        Packet packet = new Packet("createBaseHeroesRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void createBaseHeroesResponse(Map<Integer, Hero> baseHeroes){
        ClientData.baseHeroes = baseHeroes;
    }

    public static void createBasePassivesRequest(){
        Packet packet = new Packet("createBasePassivesRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void createBasePassivesResponse(Map<Integer, Passive> basePassives){
        ClientData.basePassives = basePassives;
    }
    // CREATE BASE

    // CREDENTIALS
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
    // CREDENTIALS


    // MARKET
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
    // MARKET

    // ERROR
    public static void showLoginError(String error){
        HSClient.getClient().showLoginError(error);
    }

    public static void showRegisterError(String error){
        HSClient.getClient().showRegisterError(error);
    }

    public static void showMenuError(String error){
        HSClient.getClient().showMenuError(error);
    }
    // ERROR

    // STATUS
    public static void statusDecksRequest() {
        Packet packet = new Packet("statusDecksRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void statusDecksResponse(ArrayList<Deck> decks) {
        HSClient.getClient().openStatus(decks);
    }
    // STATUS

    // HERO ARRANGE
    public static void selectHeroRequest(String name) {
        Packet packet = new Packet("selectHeroRequest",
                new Object[]{name});
        HSClient.sendPacket(packet);
    }

    public static void selectHeroResponse() {
        HSClient.getClient().selectedHero();
    }
    // HERO ARRANGE

    // DECK ARRANGE
    public static void createNewDeckRequest(Deck deck, String heroName) {
        Packet packet = new Packet("createNewDeckRequest",
                new Object[]{deck, heroName});
        HSClient.sendPacket(packet);
    }

    public static void createNewDeckResponse(String heroName) {
        HSClient.getClient().newDeckCreated(heroName);
    }

    public static void selectDeckRequest(String heroName, String deckName){
        Packet packet = new Packet("selectDeckRequest",
                new Object[]{heroName, deckName});
        HSClient.sendPacket(packet);
    }

    public static void selectDeckResponse(String herName){
        HSClient.getClient().deckSelected(herName);
    }

    public static void removeDeckRequest(String heroName, String deckName) {
        Packet packet = new Packet("removeDeckRequest",
                new Object[]{heroName, deckName});
        HSClient.sendPacket(packet);
    }

    public static void removeDeckResponse(String heroName) {
        HSClient.getClient().deckRemoved(heroName);
    }

    public static void addCardToDeckRequest(String heroName, String deckName, int cardId, int cnt){
        Packet packet = new Packet("addCardToDeckRequest",
                new Object[]{heroName, deckName, cardId, cnt});
        HSClient.sendPacket(packet);
    }

    public static void addCardToDeckResponse(Card card){
        HSClient.getClient().addCardToDeck(card);
    }

    public static void removeCardFromDeckRequest(String heroName, String deckName, int cardId, int cnt){
        Packet packet = new Packet("removeCardFromDeckRequest",
                new Object[]{heroName, deckName, cardId, cnt});
        HSClient.sendPacket(packet);
    }

    public static void removeCardFromDeckResponse(Card card){
        HSClient.getClient().removeCardFromDeck(card);
    }
    // DECK ARRANGE
}
