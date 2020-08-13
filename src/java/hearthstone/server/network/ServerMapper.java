package hearthstone.server.network;

import hearthstone.client.gui.game.ranking.RankingPanel;
import hearthstone.client.network.ClientMapper;
import hearthstone.client.network.HSClient;
import hearthstone.models.*;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;
import hearthstone.models.player.Player;
import hearthstone.shared.GameConfigs;
import hearthstone.util.CursorType;
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

    // ERROR
    public static void showLoginError(String error, ClientHandler clientHandler) {
        Packet packet = new Packet("showLoginError",
                new Object[]{error});
        clientHandler.sendPacket(packet);
    }

    public static void showRegisterError(String error, ClientHandler clientHandler) {
        Packet packet = new Packet("showRegisterError",
                new Object[]{error});
        clientHandler.sendPacket(packet);
    }

    public static void showMenuError(String error, ClientHandler clientHandler) {
        Packet packet = new Packet("showMenuError",
                new Object[]{error});
        clientHandler.sendPacket(packet);
    }

    public static void showGameError(String error, ClientHandler clientHandler) {
        Packet packet = new Packet("showGameError",
                new Object[]{error});
        clientHandler.sendPacket(packet);
    }
    // ERROR

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

    // ACCOUNT UPDATE
    public static void updateAccount(Account account, ClientHandler clientHandler) {
        Packet packet = new Packet("updateAccount",
                new Object[]{account});
        clientHandler.sendPacket(packet);
    }
    // ACCOUNT UPDATE

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

    // RANKING
    public static void rankingRequest(ClientHandler clientHandler) {
        HSServer.getInstance().initialRanks(clientHandler);
    }

    public static void rankingResponse(ArrayList<AccountInfo> topRanks, ArrayList<AccountInfo> nearRanks, ClientHandler clientHandler) {
        Packet packet = new Packet("rankingResponse",
                new Object[]{topRanks, nearRanks});
        clientHandler.sendPacket(packet);
    }

    public static void startUpdateRanking(ClientHandler clientHandler) {
        HSServer.getInstance().startUpdateRanking(clientHandler);
    }

    public static void updateRanking(ArrayList<AccountInfo> topRanks, ArrayList<AccountInfo> nearRanks, ClientHandler clientHandler){
        Packet packet = new Packet("updateRanking",
                new Object[]{topRanks, nearRanks});
        clientHandler.sendPacket(packet);
    }

    public static void stopUpdateRanking(ClientHandler clientHandler) {
        HSServer.getInstance().stopUpdateRanking(clientHandler);
    }
    // RANKING

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

    public static void addCardToDeckRequest(String heroName, String deckName, int cardId, int cnt, ClientHandler clientHandler) {
        try {
            HSServer.getInstance().addCardToDeck(heroName, deckName, cardId, cnt, clientHandler);
        } catch (HearthStoneException e) {
            showMenuError(e.getMessage(), clientHandler);
        }
    }

    public static void addCardToDeckResponse(Card card, ClientHandler clientHandler) {
        Packet packet = new Packet("addCardToDeckResponse",
                new Object[]{card});
        clientHandler.sendPacket(packet);
    }

    public static void removeCardFromDeckRequest(String heroName, String deckName, int cardId, int cnt, ClientHandler clientHandler) {
        try {
            HSServer.getInstance().removeCardFromDeck(heroName, deckName, cardId, cnt, clientHandler);
        } catch (HearthStoneException e) {
            showMenuError(e.getMessage(), clientHandler);
        }
    }

    public static void removeCardFromDeckResponse(Card card, ClientHandler clientHandler) {
        Packet packet = new Packet("removeCardFromDeckResponse",
                new Object[]{card});
        clientHandler.sendPacket(packet);
    }
    // DECK ARRANGE

    // FIND GAME
    public static void onlineGameRequest(ClientHandler clientHandler) {
        HSServer.getInstance().onlineGameRequest(clientHandler);
    }

    public static void onlineGameResponse(Player myPlayer, Player enemyPlayer, ClientHandler clientHandler) {
        Packet packet = new Packet("onlineGameResponse",
                new Object[]{myPlayer, enemyPlayer});
        clientHandler.sendPacket(packet);
    }

    public static void onlineGameCancelRequest(ClientHandler clientHandler) {
        HSServer.getInstance().onlineGameCancelRequest(clientHandler);
    }

    public static void deckReaderGameRequest(ClientHandler clientHandler) {
        HSServer.getInstance().deckReaderGameRequest(clientHandler);
    }

    public static void deckReaderGameResponse(Player myPlayer, Player enemyPlayer, ClientHandler clientHandler) {
        Packet packet = new Packet("deckReaderGameResponse",
                new Object[]{myPlayer, enemyPlayer});
        clientHandler.sendPacket(packet);
    }

    public static void deckReaderGameCancelRequest(ClientHandler clientHandler) {
        HSServer.getInstance().deckReaderGameCancelRequest(clientHandler);
    }

    public static void practiceGameRequest(ClientHandler clientHandler){
        HSServer.getInstance().practiceGameRequest(clientHandler);
    }

    public static void practiceGameResponse(Player myPlayer, Player practicePlayer, ClientHandler clientHandler){
        Packet packet = new Packet("practiceGameResponse",
                new Object[]{myPlayer, practicePlayer});
        clientHandler.sendPacket(packet);
    }

    public static void soloGameRequest(ClientHandler clientHandler) {
        HSServer.getInstance().soloGameRequest(clientHandler);
    }

    public static void soloGameResponse(Player myPlayer, Player aiPlayer, ClientHandler clientHandler) {
        Packet packet = new Packet("soloGameResponse",
                new Object[]{myPlayer, aiPlayer});
        clientHandler.sendPacket(packet);
    }
    // FIND GAME

    // BEGINNING GAME
    public static void selectPassiveRequest(int playerId, ClientHandler clientHandler) {
        System.out.println("Sent to client");
        Packet packet = new Packet("selectPassiveRequest",
                new Object[]{playerId});
        clientHandler.sendPacket(packet);
    }

    public synchronized static void selectPassiveResponse(int playerId, Passive passive, ClientHandler clientHandler) {
        // clientHandler.getPlayer().setPassive(passive);
        HSServer.getInstance().getPlayer(playerId).setPassive(passive);
    }

    public static void selectNotWantedCardsRequest(int playerId, ArrayList<Card> topCards, ClientHandler clientHandler) {
        Packet packet = new Packet("selectNotWantedCardsRequest",
                new Object[]{playerId, topCards});
        clientHandler.sendPacket(packet);
    }

    public static void selectNotWantedCardsResponse(int playerId, ArrayList<Integer> discardedCards, ClientHandler clientHandler) {
        HSServer.getInstance().getPlayer(playerId).removeInitialCards(discardedCards, GameConfigs.initialDiscardCards);
    }

    public static void startGameOnGuiRequest(Player player0, Player player1, ClientHandler clientHandler) {
        Packet packet = new Packet("startGameOnGuiRequest",
                new Object[]{player0, player1});
        clientHandler.sendPacket(packet);
    }
    // BEGINNING GAME

    // MIDDLE OF GAME
    public static void animateSpellRequest(Card card, ClientHandler clientHandler) {
        Packet packet = new Packet("animateSpellRequest",
                new Object[]{card});
        clientHandler.sendPacket(packet);
    }

    public static void endTurnRequest(int playerId, ClientHandler clientHandler) {
        HSServer.getInstance().endTurnGuiResponse(clientHandler);

        clientHandler.getGame().endTurn();
    }

    public static void endTurnResponse(ClientHandler clientHandler) {
        Packet packet = new Packet("endTurnResponse",
                null);
        clientHandler.sendPacket(packet);
    }

    public static void deleteMouseWaitingRequest(ClientHandler clientHandler) {
        Packet packet = new Packet("deleteMouseWaitingRequest",
                null);
        clientHandler.sendPacket(packet);
    }

    public static void createMouseWaitingRequest(CursorType cursorType, Card card, ClientHandler clientHandler) {
        Packet packet = new Packet("createMouseWaitingRequest",
                new Object[]{cursorType, card});
        clientHandler.sendPacket(packet);
    }

    public static void foundObjectRequest(int playerId, Object waitedCard, Object founded, ClientHandler clientHandler) {
        try {
            clientHandler.getGame().foundObject(waitedCard, founded);
            foundObjectResponse(clientHandler);
        } catch (HearthStoneException e) {
            showGameError(e.getMessage(), clientHandler);
        }
    }

    public static void foundObjectResponse(ClientHandler clientHandler) {
        Packet packet = new Packet("foundObjectResponse",
                null);
        clientHandler.sendPacket(packet);
    }

    public static void playCardRequest(int playerId, Card card, ClientHandler clientHandler) {
        try {
            // clientHandler.getPlayer().playCard(card);
            HSServer.getInstance().getPlayer(playerId).playCard(card);

            HSServer.getInstance().updateGameRequest(playerId);

            playCardResponse(card, clientHandler);
        } catch (HearthStoneException e) {
            showGameError(e.getMessage(), clientHandler);
        }
    }

    public static void playCardResponse(Card card, ClientHandler clientHandler) {
        Packet packet = new Packet("playCardResponse",
                new Object[]{card});
        clientHandler.sendPacket(packet);
    }

    public static void updateBoardRequest(Player myPlayer, Player enemyPlayer, ClientHandler clientHandler) {
        Packet packet = new Packet("updateBoardRequest",
                new Object[]{myPlayer, enemyPlayer});
        clientHandler.sendPacket(packet);
    }

    public static void chooseCardAbilityRequest(int cardGameId, ArrayList<Card> cards, ClientHandler clientHandler) {
        Packet packet = new Packet("chooseCardAbilityRequest",
                new Object[]{cardGameId, cards});
        clientHandler.sendPacket(packet);
    }

    public static void chooseCardAbilityResponse(int cardGameId, Card card, ClientHandler clientHandler) {
        clientHandler.getGame().chooseCardAbility(cardGameId, card);
    }

    public static void endGameRequest(ClientHandler clientHandler) {
        Packet packet = new Packet("endGameRequest",
                null);
        clientHandler.sendPacket(packet);
    }

    public static void exitGameRequest(int playerId, ClientHandler clientHandler) {
        HSServer.getInstance().getPlayer(playerId).getHero().setHealth(0);
    }
    // MIDDLE OF GAME

    // SETTINGS
    public static void changePasswordRequest(String password, ClientHandler clientHandler) {
        HSServer.getInstance().changePassword(password, clientHandler);
    }

    public static void changeNameRequest(String name, ClientHandler clientHandler) {
        HSServer.getInstance().changeName(name, clientHandler);
    }

    public static void changeBackCard(int backId, ClientHandler clientHandler) {
        HSServer.getInstance().changeBackCard(backId, clientHandler);
    }
    // SETTINGS

    // GAMES
    public static void gamesListRequest(ClientHandler clientHandler) {
        gamesListResponse(HSServer.getInstance().getGamesList(), clientHandler);
    }

    public static void gamesListResponse(ArrayList<GameInfo> games, ClientHandler clientHandler) {
        Packet packet = new Packet("gamesListResponse",
                new Object[]{games});
        clientHandler.sendPacket(packet);
    }
    // GAMES
}
