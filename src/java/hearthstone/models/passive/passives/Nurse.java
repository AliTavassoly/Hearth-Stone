package hearthstone.models.passive.passives;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.behaviours.EndTurnBehave;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.passive.Passive;

import javax.persistence.Entity;

@Entity
public class Nurse extends Passive implements EndTurnBehave {
    public Nurse(){ }

    public Nurse(int id, String name){
        super(id, name);
    }

    @Override
    public void endTurnBehave() {
        MinionCard minionCard = DataTransform.getRandomMinionFromLand(getPlayerId());

        if(minionCard == null)
            return;

        //Mapper.restoreHealth(minionCard);
        minionCard.restoreHealth();
        Mapper.updateBoard();
    }
}
