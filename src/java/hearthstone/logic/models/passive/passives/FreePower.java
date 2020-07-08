package hearthstone.logic.models.passive.passives;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.StartGameBehave;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.passive.Passive;

public class FreePower extends Passive implements StartGameBehave {
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