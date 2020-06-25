package hearthstone.logic.gamestuff;

import hearthstone.logic.models.player.Player;
import hearthstone.util.HearthStoneException;

public class Game {
    private Player player0, player1;
    private int whoseTurn;

    public Game(Player player0, Player player1){
        this.player0  = player0;
        this.player1  = player1;

        whoseTurn = 0;
    }

    public void startGame(){
        try {
            player0.startGame();

            player1.startGame();

            player0.startTurn();
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

    public Player getPlayerById(int id){
        if (id == 0) return player0;
        else return player1;
    }
}
