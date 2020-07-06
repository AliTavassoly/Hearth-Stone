package hearthstone.logic.models.passives;

import hearthstone.Mapper;
import hearthstone.logic.models.card.interfaces.StartTurnBehave;
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
