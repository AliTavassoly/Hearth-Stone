package hearthstone.models.passive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public abstract class Passive {
    @Id
    private int id;
    @Column
    private String name;

    // @Transient
    @JsonProperty("playerId")
    protected int playerId;

    public Passive() {
    }

    public Passive(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void log(){
        try {
            hearthstone.util.Logger.saveLog("Passive",
                    HSServer.getInstance().getPlayerName(getPlayerId()) +
                    " player, chose " + this.getName() + " passive!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Passive copy() {
        ObjectMapper mapper = ServerData.getObjectCloneMapper();
        Passive passive = null;
        try {
            passive = mapper.readValue(mapper.writeValueAsString(this), Passive.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return passive;
    }
}
