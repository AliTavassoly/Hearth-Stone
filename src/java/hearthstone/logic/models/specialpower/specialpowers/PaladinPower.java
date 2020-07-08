package hearthstone.logic.models.specialpower.specialpowers;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.EndTurnBehave;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.specialpower.SpecialHeroPower;

public class PaladinPower extends SpecialHeroPower implements EndTurnBehave {
    public PaladinPower(int id, String name){
        super(id, name);
    }

    @Override
    public void endTurnBehave() {
        MinionCard minionCard = DataTransform.getInstance().getRandomMinionFromLand(getPlayerId());
        if(minionCard == null)
            return;
        Mapper.getInstance().addAttack(1, minionCard);
        Mapper.getInstance().addHealth(1, minionCard);
        Mapper.getInstance().updateBoard();
    }
}
