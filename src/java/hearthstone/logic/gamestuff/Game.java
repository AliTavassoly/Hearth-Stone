package hearthstone.logic.gamestuff;

import hearthstone.logic.models.Player;
import hearthstone.logic.models.card.Card;
import hearthstone.util.HearthStoneException;

public class Game {
    private Player player0, player1;
    private int turnNumber;
    private int whoseTurn;

    public Game(Player player0, Player player1){
        this.player0  = player0;
        this.player1  = player1;

        whoseTurn = 0;

        startGame();
    }

    public void startGame(){
        try {
            player0.startGame();
        } catch (HearthStoneException e){
            try {
                hearthstone.util.Logger.saveLog("ERROR",
                        e.getClass().getName() + ": " + e.getMessage()
                                + "\nStack Trace: " + e.getStackTrace());
            } catch (Exception f) { }
            System.out.println(e.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public void endTurn(){
        if(whoseTurn == 0) {
            try {
                player0.startTurn();
            } catch (HearthStoneException e){
                try {
                    hearthstone.util.Logger.saveLog("ERROR",
                            e.getClass().getName() + ": " + e.getMessage()
                                    + "\nStack Trace: " + e.getStackTrace());
                } catch (Exception f) { }
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            // player1.off
        } else {
            try {
                player1.startTurn();
            } catch (HearthStoneException e){
                try {
                    hearthstone.util.Logger.saveLog("ERROR",
                            e.getClass().getName() + ": " + e.getMessage()
                                    + "\nStack Trace: " + e.getStackTrace());
                } catch (Exception f) { }
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            // player0.off
        }
        // whoseTurn ^= 1;
    }

    public void pickCard(Card card){}
}
