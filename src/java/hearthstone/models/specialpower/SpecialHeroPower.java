package hearthstone.models.specialpower;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public abstract class SpecialHeroPower {
    @Id
    private int id;
    @Column
    private String name;
    @Transient
    private int playerId;

    public SpecialHeroPower(){}

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
