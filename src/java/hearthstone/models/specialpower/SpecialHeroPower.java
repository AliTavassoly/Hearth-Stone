package hearthstone.models.specialpower;

import javax.persistence.*;

@Entity
public abstract class SpecialHeroPower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int allId;

    @Column
    private int id;
    @Column
    private String name;

    @Transient
    protected int playerId;

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
