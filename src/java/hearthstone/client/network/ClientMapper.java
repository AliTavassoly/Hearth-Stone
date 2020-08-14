package hearthstone.client.network;

import hearthstone.client.data.ClientData;
import hearthstone.client.gui.controls.dialogs.CardSelectionDialog;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.gui.game.market.MarketPanel;
import hearthstone.client.gui.game.play.boards.OnlineGameBoard;
import hearthstone.client.gui.game.play.boards.ViewGameBoard;
import hearthstone.client.gui.game.ranking.RankingPanel;
import hearthstone.models.*;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;
import hearthstone.models.player.Player;
import hearthstone.server.logic.OnlineGame;
import hearthstone.server.network.ClientHandler;
import hearthstone.server.network.HSServer;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

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

    public static void updateAccount(Account account) {
        HSClient.getClient().updateAccount(account);
    }

    // CREATE BASE
    public static void createBaseCardsRequest() {
        Packet packet = new Packet("createBaseCardsRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void createBaseCardsResponse(Map<Integer, Card> baseCards) {
        for (Object object : baseCards.keySet()) {
            Card card = baseCards.get(object);
            String key = (String) object;
            ClientData.baseCards.put(Integer.parseInt(key), card);
        }
        //baseCards.forEach((k, v) ->  ClientData.baseCards.put(Integer.parseInt(String.valueOf(k)), v));
    }

    public static void createBaseHeroesRequest() {
        Packet packet = new Packet("createBaseHeroesRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void createBaseHeroesResponse(Map<Integer, Hero> baseHeroes) {
        for (Object object : baseHeroes.keySet()) {
            Hero hero = baseHeroes.get(object);
            String key = (String) object;
            ClientData.baseHeroes.put(Integer.parseInt(key), hero);
        }
    }

    public static void createBasePassivesRequest() {
        Packet packet = new Packet("createBasePassivesRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void createBasePassivesResponse(Map<Integer, Passive> basePassives) {
        for (Object object : basePassives.keySet()) {
            Passive passive = basePassives.get(object);
            String key = (String) object;
            ClientData.basePassives.put(Integer.parseInt(key), passive);
        }
    }
    // CREATE BASE

    // CREDENTIALS
    public static void deleteAccountRequest() {
        Packet packet = new Packet("deleteAccountRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void deleteAccountResponse() {
        HSClient.getClient().deleteAccount();
    }

    public static void logoutRequest() {
        Packet packet = new Packet("logoutRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void logoutResponse() {
        HSClient.getClient().logout();
    }

    public static void loginRequest(String username, String password) {
        Packet packet = new Packet("loginRequest",
                new Object[]{username, password});
        HSClient.sendPacket(packet);
    }

    public static void loginResponse() {
        HSClient.getClient().login();
    }

    public static void registerRequest(String name, String username, String password) {
        Packet packet = new Packet("registerRequest",
                new Object[]{name, username, password});
        HSClient.sendPacket(packet);
    }

    public static void registerResponse() {
        HSClient.getClient().register();
    }
    // CREDENTIALS


    // MARKET
    public static void startUpdateMarketCards() {
        Packet packet = new Packet("startUpdateMarketCards",
                null);
        HSClient.sendPacket(packet);
    }

    public static void updateMarketCards(ArrayList<Card> cards) {
        HSClient.updateMarketCards(cards);
    }

    public static void stopUpdateMarketCards() {
        Packet packet = new Packet("stopUpdateMarketCards",
                null);
        HSClient.sendPacket(packet);
    }

    public static void marketCardsRequest() {
        Packet packet = new Packet("marketCardsRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void marketCardsResponse(ArrayList<Card> cards) {
        HSClient.getClient().openMarket(cards);
    }

    public static void buyCardRequest(int cardId) {
        Packet packet = new Packet("buyCardRequest",
                new Object[]{cardId});
        HSClient.sendPacket(packet);
    }

    public static void buyCardResponse(Card card) {
        MarketPanel.getInstance().bought(card);
    }

    public static void sellCardRequest(int cardId) {
        Packet packet = new Packet("sellCardRequest",
                new Object[]{cardId});
        HSClient.sendPacket(packet);
    }

    public static void sellCardResponse(Card card) {
        MarketPanel.getInstance().sold(card);
    }
    // MARKET

    // RANKING
    public static void rankingRequest() {
        Packet packet = new Packet("rankingRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void rankingResponse(ArrayList<AccountInfo> topRanks, ArrayList<AccountInfo> nearRanks) {
        HSClient.getClient().openRanking(topRanks, nearRanks);
    }

    public static void startUpdateRanking() {
        Packet packet = new Packet("startUpdateRanking",
                null);
        HSClient.sendPacket(packet);
    }

    public static void updateRanking(ArrayList<AccountInfo> topRanks, ArrayList<AccountInfo> nearRanks){
        RankingPanel.getInstance().updatePanel(topRanks, nearRanks);
    }

    public static void stopUpdateRanking() {
        Packet packet = new Packet("stopUpdateRanking",
                null);
        HSClient.sendPacket(packet);
    }
    // RANKING

    // ERROR
    public static void showLoginError(String error) {
        HSClient.getClient().showLoginError(error);
    }

    public static void showRegisterError(String error) {
        HSClient.getClient().showRegisterError(error);
    }

    public static void showMenuError(String error) {
        HSClient.getClient().showMenuError(error);
    }

    public static void showGameError(String error) {
        HSClient.currentGameBoard.showError(error);
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

    public static void selectDeckRequest(String heroName, String deckName) {
        Packet packet = new Packet("selectDeckRequest",
                new Object[]{heroName, deckName});
        HSClient.sendPacket(packet);
    }

    public static void selectDeckResponse(String herName) {
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

    public static void addCardToDeckRequest(String heroName, String deckName, int cardId, int cnt) {
        Packet packet = new Packet("addCardToDeckRequest",
                new Object[]{heroName, deckName, cardId, cnt});
        HSClient.sendPacket(packet);
    }

    public static void addCardToDeckResponse(Card card) {
        HSClient.getClient().addCardToDeck(card);
    }

    public static void removeCardFromDeckRequest(String heroName, String deckName, int cardId, int cnt) {
        Packet packet = new Packet("removeCardFromDeckRequest",
                new Object[]{heroName, deckName, cardId, cnt});
        HSClient.sendPacket(packet);
    }

    public static void removeCardFromDeckResponse(Card card) {
        HSClient.getClient().removeCardFromDeck(card);
    }
    // DECK ARRANGE

    // FIND GAME
    public static void onlineGameRequest() {
        Packet packet = new Packet("onlineGameRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void onlineGameResponse(Player myPlayer, Player enemyPlayer) {
        HSClient.getClient().makeNewOnlineGame(myPlayer, enemyPlayer);
    }

    public static void onlineGameCancelRequest() {
        Packet packet = new Packet("onlineGameCancelRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void deckReaderGameRequest() {
        Packet packet = new Packet("deckReaderGameRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void deckReaderGameResponse(Player myPlayer, Player enemyPlayer) {
        HSClient.getClient().makeNewDeckReaderGame(myPlayer, enemyPlayer);
    }

    public static void deckReaderGameCancelRequest() {
        Packet packet = new Packet("deckReaderGameCancelRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void practiceGameRequest() {
        Packet packet = new Packet("practiceGameRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void practiceGameResponse(Player myPlayer, Player practicePlayer) {
        HSClient.getClient().makeNewPracticeGame(myPlayer, practicePlayer);
    }

    public static void soloGameRequest() {
        Packet packet = new Packet("soloGameRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void soloGameResponse(Player myPlayer, Player aiPlayer) {
        HSClient.getClient().makeNewSoloGame(myPlayer, aiPlayer);
    }
    // FIND GAME

    // BEGIN OF GAME
    public static void selectPassiveRequest(int playerId) {
        HSClient.currentGameBoard.showPassiveDialogs(playerId);
    }

    public static void selectPassiveResponse(int playerId, Passive passive) {
        Packet packet = new Packet("selectPassiveResponse",
                new Object[]{playerId, passive});
        HSClient.sendPacket(packet);
    }

    public static void selectNotWantedCardsRequest(int playerId, ArrayList<Card> topCards) {
        HSClient.currentGameBoard.showCardDialog(playerId, topCards);
    }

    public static void selectNotWantedCardsResponse(int playerId, ArrayList<Integer> discardedCards) {
        Packet packet = new Packet("selectNotWantedCardsResponse",
                new Object[]{playerId, discardedCards});
        HSClient.sendPacket(packet);
    }

    public static void startGameOnGuiRequest(Player myPlayer, Player enemyPlayer) {
        HSClient.currentGameBoard.startGameOnGui(myPlayer, enemyPlayer);
    }
    // BEGIN OF GAME

    // MIDDLE OF GAME
    public static void animateSpellRequest(Card card) {
        HSClient.currentGameBoard.animateSpell(card);
    }

    public static void endTurnRequest(int playerId) {
        Packet packet = new Packet("endTurnRequest",
                new Object[]{playerId});
        HSClient.sendPacket(packet);
    }

    public static void endTurnResponse() {
        HSClient.currentGameBoard.restartTimeLine();
    }

    public static void deleteMouseWaitingRequest() {
        HSClient.currentGameBoard.deleteCurrentMouseWaiting();
    }

    public static void createMouseWaitingRequest(CursorType cursorType, Card card) {
        HSClient.currentGameBoard.makeNewMouseWaiting(cursorType, card);
    }

    public static void foundObjectRequest(int playerId, Object waitedCard, Object founded) {
        Packet packet = new Packet("foundObjectRequest",
                new Object[]{playerId, waitedCard, founded});
        HSClient.sendPacket(packet);
    }

    public static void foundObjectResponse() {
        HSClient.currentGameBoard.deleteCurrentMouseWaiting();
    }

    public static void playCardRequest(int playerId, Card card) {
        Packet packet = new Packet("playCardRequest",
                new Object[]{playerId, card});
        HSClient.sendPacket(packet);
    }

    public static void playCardResponse(Card card) {
        HSClient.currentGameBoard.removeCardAnimation(card);
    }

    public static void chooseCardAbilityRequest(int cardGameId, ArrayList<Card> cards) {
        chooseCardAbilityResponse(cardGameId, new CardSelectionDialog(GameFrame.getInstance(), cards).getCard());
    }

    public static void chooseCardAbilityResponse(int cardGameId, Card card) {
        Packet packet = new Packet("chooseCardAbilityResponse",
                new Object[]{cardGameId, card});
        HSClient.sendPacket(packet);
    }

    public static void updateBoardRequest(Player myPlayer, Player enemyPlayer) {
        HSClient.currentGameBoard.restart(myPlayer, enemyPlayer);
    }

    public static void endGameRequest() {
        HSClient.currentGameBoard.gameEnded();
    }

    public static void exitGameRequest(int playerId) {
        Packet packet = new Packet("exitGameRequest",
                new Object[]{playerId});
        HSClient.sendPacket(packet);
    }
    // MIDDLE OF GAME

    // SETTINGS
    public static void changePasswordRequest(String password) {
        Packet packet = new Packet("changePasswordRequest",
                new Object[]{password});
        HSClient.sendPacket(packet);
    }

    public static void changeNameRequest(String name) {
        Packet packet = new Packet("changeNameRequest",
                new Object[]{name});
        HSClient.sendPacket(packet);
    }

    public static void changeBackCard(int backId) {
        Packet packet = new Packet("changeBackCard",
                new Object[]{backId});
        HSClient.sendPacket(packet);
    }
    // SETTINGS

    // GAME VIEW
    public static void gamesListRequest() {
        Packet packet = new Packet("gamesListRequest",
                null);
        HSClient.sendPacket(packet);
    }

    public static void gamesListResponse(ArrayList<GameInfo> games) {
        HSClient.getClient().gamesResponse(games);
    }

    public static void watchersRequest(String username) {
        Packet packet = new Packet("watchersRequest",
                new Object[]{username});
        HSClient.sendPacket(packet);
    }

    public static void watchersResponse(ArrayList<WatcherInfo> watchers) {
        OnlineGameBoard.getInstance().updateWatchers(watchers);
    }

    public static void viewRequest(String wantToView, String viewer){
        Packet packet = new Packet("viewRequest",
                new Object[]{wantToView, viewer});
        HSClient.sendPacket(packet);
    }

    public static void viewResponse(Player firstPlayer, Player secondPlayer) {
        HSClient.getClient().makeNewViewGameBoard(firstPlayer, secondPlayer);
        ViewGameBoard.getInstance().restart(firstPlayer, secondPlayer);
    }

    public static void viewerUpdateRequest(Player firstPlayer, Player secondPlayer){
        ViewGameBoard.getInstance().restart(firstPlayer, secondPlayer);
    }

    public static void cancelViewRequest(String firstPlayerUsername, String wantToCancel) {
        Packet packet = new Packet("cancelViewRequest",
                new Object[]{firstPlayerUsername, wantToCancel});
        HSClient.sendPacket(packet);
    }

    public static void kickWatcherRequest(String username){
        Packet packet = new Packet("kickWatcherRequest",
                new Object[]{username});
        HSClient.sendPacket(packet);
    }

    public static void kickWatcherResponse(){
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
    }
    // GAME VIEW
}
