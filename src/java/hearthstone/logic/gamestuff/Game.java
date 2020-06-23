package hearthstone.logic.gamestuff;

import hearthstone.models.card.Card;
import hearthstone.models.player.Player;
import hearthstone.util.HearthStoneException;

public class Game {
    private Player player0, player1;
    private int whoseTurn;

    public Game(Player player0, Player player1){
        this.player0  = player0;
        this.player1  = player1;

        whoseTurn = 0;

        configGame();
    }

    private void configGame() {
        for(Card card: player0.getDeck().getCards()){
            player0.configCard(card);
        }
        player0.configHero(player0.getHero());

        for(Card card: player1.getDeck().getCards()){
            player1.configCard(card);
        }
        player1.configHero(player1.getHero());
    }

    public void startGame(){
        try {
            player0.startGame();
            player0.startTurn();

            player1.startGame();
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
                player0.endTurn();
                whoseTurn = 1;
                player1.startTurn();
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
                player1.endTurn();
                whoseTurn = 0;
                player0.startTurn();
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
}
