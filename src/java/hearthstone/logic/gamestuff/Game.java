package hearthstone.logic.gamestuff;

import hearthstone.Mapper;
import hearthstone.logic.models.player.Player;
import hearthstone.util.HearthStoneException;
import hearthstone.util.timer.MyBigTask;
import hearthstone.util.timer.MyTimerTask;

public class Game {
    private Player player0, player1;
    private int whoseTurn;

    public Game(Player player0, Player player1){
        this.player0  = player0;
        this.player1  = player1;

        configGame();

        MyTimerTask myTimerTask = new MyTimerTask(500, new MyBigTask() {
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

    private void configGame() {
        whoseTurn = 0;

        player0.setPlayerId(0);
        player1.setPlayerId(1);

        player0.configPlayer();
        player1.configPlayer();
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
