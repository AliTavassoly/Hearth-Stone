package hearthstone.server.network;

import hearthstone.models.Account;
import hearthstone.models.Deck;
import hearthstone.models.Packet;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;
import hearthstone.util.HearthStoneException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

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

    public static void showLoginError(String error, ClientHandler clientHandler){
        Packet packet = new Packet("showLoginError",
                new Object[]{error});
        clientHandler.sendPacket(packet);
    }

    public static void showRegisterError(String error, ClientHandler clientHandler){
        Packet packet = new Packet("showRegisterError",
                new Object[]{error});
        clientHandler.sendPacket(packet);
    }

    public static void showMenuError(String error, ClientHandler clientHandler){
        Packet packet = new Packet("showMenuError",
                new Object[]{error});
        clientHandler.sendPacket(packet);
    }

    // BASES
    public static void createBaseCardsRequest(ClientHandler clientHandler) {
        HSServer.getInstance().createBaseCards(clientHandler);
    }

    public static void createBaseCardsResponse(Map<Integer, Card> baseCards, ClientHandler clientHandler) {
        Packet packet = new Packet("createBaseCardsResponse",
                new Object[]{baseCards});
        clientHandler.sendPacket(packet);
    }

    public static void createBaseHeroesRequest(ClientHandler clientHandler) {
        HSServer.getInstance().createBaseHeroes(clientHandler);
    }

    public static void createBaseHeroesResponse(Map<Integer, Hero> baseHeroes, ClientHandler clientHandler) {
        Packet packet = new Packet("createBaseHeroesResponse",
                new Object[]{baseHeroes});
        clientHandler.sendPacket(packet);
    }

    public static void createBasePassivesRequest(ClientHandler clientHandler) {
        HSServer.getInstance().createBasePassives(clientHandler);
    }

    public static void createBasePassivesResponse(Map<Integer, Passive> basePassives, ClientHandler clientHandler) {
        Packet packet = new Packet("createBasePassivesResponse",
                new Object[]{basePassives});
        clientHandler.sendPacket(packet);
    }
    // BASES

    // CREDENTIALS
    public static void deleteAccountRequest(ClientHandler clientHandler) {
        HSServer.getInstance().deleteAccount(clientHandler);
    }

    public static void deleteAccountResponse(ClientHandler clientHandler) {
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
            showLoginError(e.getMessage(), clientHandler);
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
            showRegisterError(e.getMessage(), clientHandler);
        }
    }

    public static void registerResponse(String username, ClientHandler clientHandler) {
        Packet packet = new Packet("registerResponse",
                null);
        clientHandler.sendPacket(packet);
    }
    // CREDENTIALS

    // MARKET
    public static void buyCardRequest(int cardId, ClientHandler clientHandler) {
        try {
            HSServer.getInstance().buyCard(cardId, clientHandler);
        } catch (HearthStoneException e) {
            showMenuError(e.getMessage(), clientHandler);
        }
    }

    public static void buyCardResponse(Card card, ClientHandler clientHandler) {
        Packet packet = new Packet("buyCardResponse",
                new Object[]{card});
        clientHandler.sendPacket(packet);
    }

    public static void sellCardRequest(int cardId, ClientHandler clientHandler) {
        try {
            HSServer.getInstance().sellCard(cardId, clientHandler);
        } catch (HearthStoneException e) {
            showMenuError(e.getMessage(), clientHandler);
        }
    }

    public static void sellCardResponse(Card card, ClientHandler clientHandler) {
        Packet packet = new Packet("sellCardResponse",
                new Object[]{card});
        clientHandler.sendPacket(packet);
    }

    public static void marketCardsRequest(ClientHandler clientHandler) {
        HSServer.getInstance().marketCardsInitialCard(clientHandler);
    }

    public static void marketCardsResponse(ArrayList<Card> cards, ClientHandler clientHandler) {
        Packet packet = new Packet("marketCardsResponse",
                new Object[]{cards});
        clientHandler.sendPacket(packet);
    }

    public static void startUpdateMarketCards(ClientHandler clientHandler) {
        HSServer.getInstance().startUpdateMarketCards(clientHandler);
    }

    public static void updateMarketCards(ArrayList<Card> cards, ClientHandler clientHandler) {
        Packet packet = new Packet("updateMarketCards",
                new Object[]{cards});
        clientHandler.sendPacket(packet);
    }

    public static void stopUpdateMarketCards(ClientHandler clientHandler) {
        HSServer.getInstance().stopUpdateMarketCards(clientHandler);
    }
    // MARKET

    // STATUS
    public static void statusDecksRequest(ClientHandler clientHandler) {
        HSServer.getInstance().statusBestDecks(clientHandler);
    }

    public static void statusDecksResponse(ArrayList<Deck> decks, ClientHandler clientHandler) {
        Packet packet = new Packet("statusDecksResponse",
                new Object[]{decks});
        clientHandler.sendPacket(packet);
    }
    // STATUS

    // HERO SELECTION
    public static void selectHeroRequest(String heroName, ClientHandler clientHandler) {
        HSServer.getInstance().selectHero(heroName, clientHandler);
    }

    public static void selectHeroResponse(ClientHandler clientHandler) {
        Packet packet = new Packet("selectHeroResponse",
                null);
        clientHandler.sendPacket(packet);
    }
    // HERO SELECTION

    // DECK ARRANGE
    public static void createNewDeckRequest(Deck deck, String heroName, ClientHandler clientHandler) {
        try {
            HSServer.getInstance().createNewDeck(deck, heroName, clientHandler);
        } catch (HearthStoneException e) {
            showMenuError(e.getMessage(), clientHandler);
        }
    }

    public static void createNewDeckResponse(String heroName, ClientHandler clientHandler) {
        Packet packet = new Packet("createNewDeckResponse",
                new Object[]{heroName});
        clientHandler.sendPacket(packet);
    }

    public static void selectDeckRequest(String heroName, String deckName, ClientHandler clientHandler) {
        HSServer.getInstance().selectDeck(heroName, deckName, clientHandler);
    }

    public static void selectDeckResponse(String heroName, ClientHandler clientHandler) {
        Packet packet = new Packet("selectDeckResponse",
                new Object[]{heroName});
        clientHandler.sendPacket(packet);
    }

    public static void removeDeckRequest(String heroName, String deckName, ClientHandler clientHandler) {
        HSServer.getInstance().removeDeck(heroName, deckName, clientHandler);
    }

    public static void removeDeckResponse(String heroName, ClientHandler clientHandler) {
        Packet packet = new Packet("removeDeckResponse",
                new Object[]{heroName});
        clientHandler.sendPacket(packet);
    }

    public static void addCardToDeckRequest(String heroName, String deckName, int cardId, int cnt, ClientHandler clientHandler){
        try {
            HSServer.getInstance().addCardToDeck(heroName, deckName, cardId, cnt, clientHandler);
        } catch (HearthStoneException e){
            showMenuError(e.getMessage(), clientHandler);
        }
    }

    public static void addCardToDeckResponse(Card card, ClientHandler clientHandler){
        Packet packet = new Packet("addCardToDeckResponse",
                new Object[]{card});
        clientHandler.sendPacket(packet);
    }

    public static void removeCardFromDeckRequest(String heroName, String deckName, int cardId, int cnt, ClientHandler clientHandler){
        try {
            HSServer.getInstance().removeCardFromDeck(heroName, deckName, cardId, cnt, clientHandler);
        } catch (HearthStoneException e){
            showMenuError(e.getMessage(), clientHandler);
        }
    }

    public static void removeCardFromDeckResponse(Card card, ClientHandler clientHandler){
        Packet packet = new Packet("removeCardFromDeckResponse",
                new Object[]{card});
        clientHandler.sendPacket(packet);
    }
    // DECK ARRANGE

    public static void updateAccount(Account account, ClientHandler clientHandler) {
        System.out.println("Update account sent to client: " + account.getUsername());
        Packet packet = new Packet("updateAccount",
                new Object[]{account});
        clientHandler.sendPacket(packet);
    }
}
