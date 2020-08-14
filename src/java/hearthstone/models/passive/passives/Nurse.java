package hearthstone.models.passive.passives;

import hearthstone.models.behaviours.EndTurnBehave;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.passive.Passive;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class Nurse extends Passive implements EndTurnBehave {
    public Nurse(){ }

    public Nurse(int id, String name){
        super(id, name);
    }

    @Override
    public void endTurnBehave() {
        MinionCard minionCard = HSServer.getInstance().getPlayer(playerId).getFactory().getRandomMinionFromLand();

        if(minionCard == null)
            return;

        minionCard.restoreHealth();

        HSServer.getInstance().updateGame(playerId);
    }
}
