package hearthstone.models.passive.passives;

import hearthstone.models.behaviours.StartGameBehave;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.passive.Passive;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class FreePower extends Passive implements StartGameBehave {
    public FreePower(){ }

    public FreePower(int id, String name){
        super(id, name);
    }

    @Override
    public void startGameBehave() {
        HeroPowerCard power = HSServer.getInstance().getPlayer(getPlayerId()).getHeroPower();

        power.setManaCost(Math.max(0, power.getManaCost() - 1));

        power.setExtraNumberOfAttack(1);

        HSServer.getInstance().updateGameRequest(getPlayerId());
    }
}
