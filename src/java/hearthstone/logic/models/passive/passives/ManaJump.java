package hearthstone.logic.models.passive.passives;

import hearthstone.Mapper;
import hearthstone.logic.models.card.interfaces.StartGameBehave;
import hearthstone.logic.models.passive.Passive;

public class ManaJump extends Passive implements StartGameBehave {
    public ManaJump(int id, String name){
        super(id, name);
    }

    @Override
    public void startGameBehave() {
        Mapper.getInstance().setExtraMane(getPlayerId(), 1);
    }
}
