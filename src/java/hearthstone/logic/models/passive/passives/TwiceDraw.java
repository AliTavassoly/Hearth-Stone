package hearthstone.logic.models.passive.passives;

import hearthstone.Mapper;
import hearthstone.logic.behaviours.StartTurnBehave;
import hearthstone.logic.models.passive.Passive;
import hearthstone.util.HearthStoneException;

public class TwiceDraw extends Passive implements StartTurnBehave {
    public TwiceDraw(int id, String name){
        super(id, name);
    }

    @Override
    public void startTurnBehave() {
        try {
            Mapper.getInstance().drawCard(getPlayerId());
        } catch (HearthStoneException ignore) {}
    }
}
