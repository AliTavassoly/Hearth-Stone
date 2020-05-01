package hearthstone.logic.gamestuff;

import hearthstone.logic.models.Player;
import hearthstone.util.HearthStoneException;

public class Game {
    private Player player0, player1;
    private int whoseTurn;

    public Game(Player player0, Player player1){
        this.player0  = player0;
        this.player1  = player1;

        whoseTurn = 0;

        startGame();
    }

    private void startGame(){
        try {
            player0.startGame();
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
        } else {
            try {
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
        }
    }
}
