package hearthstone.models.specialpower.specialpowers;

import hearthstone.models.behaviours.EndTurnBehave;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.specialpower.SpecialHeroPower;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class PaladinPower extends SpecialHeroPower implements EndTurnBehave {
    public PaladinPower(){}

    public PaladinPower(int id, String name){
        super(id, name);
    }

    @Override
    public void endTurnBehave() {
        MinionCard minionCard = HSServer.getInstance().getPlayer(getPlayerId()).getFactory().getRandomMinionFromLand();

        if(minionCard == null)
            return;
        minionCard.changeAttack(1);

        minionCard.gotHeal(1);

        HSServer.getInstance().updateGame(playerId);
    }
}
