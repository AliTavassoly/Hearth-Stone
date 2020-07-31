package hearthstone.models.passive.passives;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.StartGameBehave;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.passive.Passive;

import javax.persistence.Entity;

@Entity
public class FreePower extends Passive implements StartGameBehave {
    public FreePower(){ }

    public FreePower(int id, String name){
        super(id, name);
    }

    @Override
    public void startGameBehave() {
        HeroPowerCard power = DataTransform.getInstance().getHeroPower(getPlayerId());

        Mapper.getInstance().setCardMana(power, Math.max(0, power.getManaCost() - 1));
        Mapper.getInstance().setHeroPowerExtraAttack(power, 1);

        Mapper.getInstance().updateBoard();
    }
}
