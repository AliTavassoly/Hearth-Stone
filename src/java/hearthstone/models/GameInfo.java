package hearthstone.models;

public class GameInfo {
    private String player0, player1;

    public String getPlayer0() {
        return player0;
    }
    public void setPlayer0(String player0) {
        this.player0 = player0;
    }

    public String getPlayer1() {
        return player1;
    }
    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public GameInfo(){ }

    public GameInfo(String player0, String player1) {
        this.player0 = player0;
        this.player1 = player1;
    }
}
