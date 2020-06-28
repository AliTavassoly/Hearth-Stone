package hearthstone.logic.gamestuff;

import hearthstone.Mapper;
import hearthstone.logic.models.player.Player;
import hearthstone.util.HearthStoneException;
import hearthstone.util.timer.MyTask;
import hearthstone.util.timer.MyTimerTask;

public class Game {
    private Player player0, player1;
    private int whoseTurn;

    public Game(Player player0, Player player1){
        this.player0  = player0;
        this.player1  = player1;

        whoseTurn = 0;

        MyTimerTask myTimerTask = new MyTimerTask(500, new MyTask() {
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
                Mapper.getInstance().gameEnded();
            }

            @Override
            public boolean finishCondition() {
                return gameEnded();
            }
        });
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

    public boolean gameEnded(){
        return player0.getHero().getHealth() <= 0 || player1.getHero().getHealth() <= 0;
    }
}
