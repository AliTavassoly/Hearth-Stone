package hearthstone.logic.gamestuff;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.player.Player;
import hearthstone.util.HearthStoneException;
import hearthstone.util.timer.HSBigTask;
import hearthstone.util.timer.HSTimerTask;

import java.util.ArrayList;

public class Game {
    private Player player0, player1;
    private int whoseTurn;

    private ArrayList<Card> cards;
    private ArrayList<Hero> heroes;

    public Game(Player player0, Player player1){
        this.player0  = player0;
        this.player1  = player1;

        cards = new ArrayList<>();
        heroes = new ArrayList<>();

        new HSTimerTask(500, new HSBigTask() {
            @Override
            public void startFunction() { }

            @Override
            public void periodFunction() { }

            @Override
            public void warningFunction() { }

            @Override
            public void finishedFunction() {

            }

            @Override
            public void closeFunction() {
                Mapper.gameEnded();
            }

            @Override
            public boolean finishCondition() {
                return gameEnded();
            }
        }).start();
    }

    private void configGame() {
        whoseTurn = 0;

        player0.setGame(this);
        player1.setGame(this);

        player0.setPlayerId(0);
        player1.setPlayerId(1);

        player0.configPlayer();
        player1.configPlayer();
    }

    public void startGame(){
        try {
            configGame();

            //Mapper.startGame(player0.getPlayerId());
            DataTransform.getPlayer(player0.getPlayerId()).startGame();

            //Mapper.startGame(player1.getPlayerId());
            DataTransform.getPlayer(player1.getPlayerId()).startGame();

            //Mapper.startTurn(player0.getPlayerId());
            DataTransform.getPlayer(player0.getPlayerId()).startTurn();
        } catch (HearthStoneException e){
            try {
                hearthstone.util.Logger.saveLog("ERROR",
                        e.getClass().getName() + ": " + e.getMessage()
                                + "\nStack Trace: " + e.getStackTrace());
            } catch (Exception f) { }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void endTurn(){
        if(whoseTurn == 0) {
            try {
                //Mapper.endTurn(player0.getPlayerId());
                DataTransform.getPlayer(player0.getPlayerId()).endTurn();

                whoseTurn = 1;
                //Mapper.startTurn(player1.getPlayerId());
                DataTransform.getPlayer(player1.getPlayerId()).startTurn();
            } catch (HearthStoneException e){
                try {
                    hearthstone.util.Logger.saveLog("ERROR",
                            e.getClass().getName() + ": " + e.getMessage()
                                    + "\nStack Trace: " + e.getStackTrace());
                } catch (Exception f) { }
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try {
                //Mapper.endTurn(player1.getPlayerId());
                DataTransform.getPlayer(player1.getPlayerId()).endTurn();

                whoseTurn = 0;
                //Mapper.startTurn(player0.getPlayerId());
                DataTransform.getPlayer(player0.getPlayerId()).startTurn();
            } catch (HearthStoneException e){
                try {
                    hearthstone.util.Logger.saveLog("ERROR",
                            e.getClass().getName() + ": " + e.getMessage()
                                    + "\nStack Trace: " + e.getStackTrace());
                } catch (Exception f) { }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public int getWhoseTurn(){
        return whoseTurn;
    }

    public Player getPlayerById(int id){
        if (id == 0) return player0;
        else return player1;
    }

    public boolean gameEnded(){
        return player0.getHero().getHealth() <= 0 || player1.getHero().getHealth() <= 0;
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public void addHero(Hero hero) {
        heroes.add(hero);
    }

    public int getNewCardId() {
        return cards.size();
    }

    public int getNewHeroId() {
        return heroes.size();
    }

    public Card getCardById(int cardId){
        for(Card card: cards){
            if(card.getCardGameId() == cardId){
                return card;
            }
        }
        return null;
    }
}
