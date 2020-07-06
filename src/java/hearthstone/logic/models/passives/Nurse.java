package hearthstone.logic.models.passives;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.interfaces.EndTurnBehave;
import hearthstone.logic.models.card.minion.MinionCard;

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
