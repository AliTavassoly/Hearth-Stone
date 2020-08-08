package hearthstone.models.specialpower.specialpowers;

import hearthstone.models.behaviours.StartGameBehave;
import hearthstone.models.specialpower.SpecialHeroPower;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class MagePower extends SpecialHeroPower implements StartGameBehave {
    public MagePower(){}

    public MagePower(int id, String name){
        super(id, name);
    }

    @Override
    public void startGameBehave() {
        HSServer.getInstance().getPlayer(getPlayerId()).discountSpells(2);
    }
}
