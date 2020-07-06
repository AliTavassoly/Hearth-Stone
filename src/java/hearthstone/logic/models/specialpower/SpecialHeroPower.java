package hearthstone.logic.models.specialpower;

public class SpecialHeroPower {
    private String name;
    private int id;
    private int playerId;

    public SpecialHeroPower(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void setPlayerId(int playerId){
        this.playerId = playerId;
    }

    public int getPlayerId(){
        return playerId;
    }
}
