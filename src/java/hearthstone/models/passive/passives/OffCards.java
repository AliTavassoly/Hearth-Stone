package hearthstone.models.passive.passives;

import hearthstone.models.behaviours.StartGameBehave;
import hearthstone.models.passive.Passive;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class OffCards extends Passive implements StartGameBehave {
    public OffCards(){ }

    public OffCards(int id, String name){
        super(id, name);
    }


    @Override
    public void startGameBehave() {
        HSServer.getInstance().getPlayer(getPlayerId()).discountAllDeckCard(1);
    }
}
