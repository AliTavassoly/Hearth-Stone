package hearthstone.logic.models.passives;

import hearthstone.Mapper;
import hearthstone.logic.models.card.interfaces.StartGameBehave;

public class ManaJump extends Passive implements StartGameBehave {
    public ManaJump(int id, String name){
        super(id, name);
    }

    @Override
    public void startGameBehave() {
        Mapper.getInstance().setExtraMane(getPlayerId(), 1);
    }
}
