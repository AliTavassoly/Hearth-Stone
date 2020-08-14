package hearthstone.client.network;

import hearthstone.client.gui.BaseFrame;
import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.client.gui.credetials.LoginPanel;
import hearthstone.client.gui.credetials.LogisterPanel;
import hearthstone.client.gui.credetials.RegisterPanel;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.collection.DeckArrangement;
import hearthstone.client.gui.game.collection.DeckSelection;
import hearthstone.client.gui.game.collection.HeroSelection;
import hearthstone.client.gui.game.market.MarketPanel;
import hearthstone.client.gui.game.onlinegames.GamesPanel;
import hearthstone.client.gui.game.play.boards.*;
import hearthstone.client.gui.game.ranking.RankingPanel;
import hearthstone.client.gui.game.status.StatusPanel;
import hearthstone.models.*;
import hearthstone.models.card.Card;
import hearthstone.models.player.Player;

import javax.swing.text.View;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class HSClient {
    private static HSClient client;

    private Socket socket;
    private Receiver receiver;
    private Sender sender;

    public static Account currentAccount;

    public static GameBoard currentGameBoard;

    private HSClient(String serverIP, int serverPort){
        try{
            this.socket = new Socket(serverIP, serverPort);

            CredentialsFrame.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HSClient makeNewClient(String serverIP, int serverPort){
        return client = new HSClient(serverIP, serverPort);
    }

    public static HSClient getClient(){
        return client;
    }

    public void start() {
        try {
            InputStream socketInputStream = socket.getInputStream();
            PrintStream socketPrinter = new PrintStream(socket.getOutputStream());

            receiver = new Receiver(socketInputStream);
            sender = new Sender(socketPrinter);

            receiver.start();

            createHearthStoneConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHearthStoneConfigs() {
        ClientMapper.createBaseCardsRequest();
        ClientMapper.createBaseHeroesRequest();
        ClientMapper.createBasePassivesRequest();
    }

    public static void sendPacket(Packet packet) {
        getClient().sender.sendPacket(packet);
    }

    // -------------

    public void updateAccount(Account account) {
        currentAccount = account;
    }

    public void login(){
        CredentialsFrame.getInstance().setVisible(false);
        GameFrame.getNewInstance().setVisible(true);
    }

    public void register(){
        CredentialsFrame.getInstance().getContentPane().setVisible(false);
        CredentialsFrame.getInstance().setContentPane(new LogisterPanel());
        CredentialsFrame.getInstance().setVisible(false);
        GameFrame.getNewInstance().setVisible(true);
    }

    public void logout() {
        GameFrame.getInstance().setVisible(false);
        GameFrame.getInstance().dispose();
        CredentialsFrame.getNewInstance().setVisible(true);
    }

    public void deleteAccount(){
        GameFrame.getInstance().setVisible(false);
        GameFrame.getInstance().dispose();
        CredentialsFrame.getNewInstance().setVisible(true);
    }

    public void showLoginError(String error){
        LoginPanel.getInstance().showError(error);
    }

    public void showRegisterError(String error){
        RegisterPanel.getInstance().showError(error);
    }

    public void showMenuError(String error){
        BaseFrame.error(error);
    }

    public static void updateMarketCards(ArrayList<Card> cards) {
        MarketPanel.getInstance().update(cards);
    }

    public void openMarket(ArrayList<Card> cards) {
        MarketPanel.marketCards = cards;
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), MarketPanel.makeInstance());
    }

    public void openStatus(ArrayList<Deck> decks) {
        StatusPanel.topDecks = decks;
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), StatusPanel.makeInstance());
    }

    public void selectedHero() {
        HeroSelection.getInstance().restart();
    }

    public void newDeckCreated(String heroName) {
        DeckSelection.getInstance().restart(heroName);
    }

    public void deckSelected(String heroName) {
        DeckSelection.getInstance().restart(heroName);
    }

    public void deckRemoved(String heroName){
        DeckSelection.getInstance().restart(heroName);
    }

    public void addCardToDeck(Card card) {
        DeckArrangement.getInstance().addCardToDeck(card);
    }

    public void removeCardFromDeck(Card card){
        DeckArrangement.getInstance().removeCardFromDeck(card);
    }

    public void makeNewOnlineGame(Player myPlayer, Player enemyPlayer) {
        HSClient.currentGameBoard = OnlineGameBoard.makeInstance(myPlayer, enemyPlayer);
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), HSClient.currentGameBoard);
    }

    public void makeNewPracticeGame(Player myPlayer, Player practicePlayer) {
        HSClient.currentGameBoard = PracticeGameBoard.makeInstance(myPlayer, practicePlayer);
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), HSClient.currentGameBoard);
    }

    public void makeNewSoloGame(Player myPlayer, Player aiPlayer) {
        HSClient.currentGameBoard = SoloGameBoard.makeInstance(myPlayer, aiPlayer);
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), HSClient.currentGameBoard);
    }

    public void makeNewDeckReaderGame(Player myPlayer, Player enemyPlayer) {
        HSClient.currentGameBoard = OnlineGameBoard.makeInstance(myPlayer, enemyPlayer);
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), HSClient.currentGameBoard);
    }

    public void makeNewViewGameBoard(Player firstPlayer, Player secondPlayer) {
        HSClient.currentGameBoard = ViewGameBoard.makeInstance(firstPlayer, secondPlayer);
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), HSClient.currentGameBoard);
    }

    public void openRanking(ArrayList<AccountInfo> topRanks, ArrayList<AccountInfo> myRank) {
        RankingPanel.topRanks = topRanks;
        RankingPanel.nearRanks = myRank;
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), RankingPanel.makeInstance());
    }

    public void gamesResponse(ArrayList<GameInfo> games) {
        GamesPanel.games = games;
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), GamesPanel.makeInstance());
    }
}
