package hearthstone.logic.models.passive.passives;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.interfaces.EndTurnBehave;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.passive.Passive;

public class Nurse extends Passive implements EndTurnBehave {
    public Nurse(int id, String name){
        super(id, name);
    }

    @Override
    public void endTurnBehave() {
        MinionCard minionCard = DataTransform.getInstance().getRandomMinionFromLand(getPlayerId());

        if(minionCard == null)
            return;

        Mapper.getInstance().restoreHealth(minionCard);
        Mapper.getInstance().updateBoard();
    }
}
