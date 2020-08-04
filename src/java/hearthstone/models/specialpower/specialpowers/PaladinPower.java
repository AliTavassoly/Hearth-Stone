package hearthstone.models.specialpower.specialpowers;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.behaviours.EndTurnBehave;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.specialpower.SpecialHeroPower;

import javax.persistence.Entity;

@Entity
public class PaladinPower extends SpecialHeroPower implements EndTurnBehave {
    public PaladinPower(){}

    public PaladinPower(int id, String name){
        super(id, name);
    }

    @Override
    public void endTurnBehave() {
        MinionCard minionCard = DataTransform.getRandomMinionFromLand(getPlayerId());
        if(minionCard == null)
            return;
        //Mapper.addAttack(1, minionCard.getCardGameId());
        minionCard.changeAttack(1);

        //Mapper.addHealth(1, minionCard);
        minionCard.gotHeal(1);
        Mapper.updateBoard();
    }
}
