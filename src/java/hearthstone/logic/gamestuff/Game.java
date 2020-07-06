package hearthstone.logic.gamestuff;

import hearthstone.Mapper;
import hearthstone.logic.models.player.Player;
import hearthstone.util.HearthStoneException;
import hearthstone.util.timer.HSBigTask;
import hearthstone.util.timer.HSTimerTask;

public class Game {
    private Player player0, player1;
    private int whoseTurn;
    private boolean shuffleCards;

    public Game(Player player0, Player player1, boolean shuffleCards){
        this.player0  = player0;
        this.player1  = player1;
        this.shuffleCards = shuffleCards;

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
                Mapper.getInstance().gameEnded();
            }

            @Override
            public boolean finishCondition() {
                return gameEnded();
            }
        }).start();
    }

    private void configGame(boolean shuffleCards) {
        whoseTurn = 0;

        player0.setPlayerId(0);
        player1.setPlayerId(1);

        player0.configPlayer(shuffleCards);
        player1.configPlayer(shuffleCards);
    }

    public void startGame(){
        try {
            configGame(shuffleCards);

            Mapper.getInstance().startGame(player0.getPlayerId());

            Mapper.getInstance().startGame(player1.getPlayerId());

            Mapper.getInstance().startTurn(player0.getPlayerId());
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
                Mapper.getInstance().endTurn(player0.getPlayerId());
                whoseTurn = 1;
                Mapper.getInstance().startTurn(player1.getPlayerId());
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
                Mapper.getInstance().endTurn(player1.getPlayerId());
                whoseTurn = 0;
                Mapper.getInstance().startTurn(player0.getPlayerId());
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
