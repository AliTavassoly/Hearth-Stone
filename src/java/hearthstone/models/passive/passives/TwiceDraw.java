package hearthstone.models.passive.passives;

import hearthstone.Mapper;
import hearthstone.logic.behaviours.StartTurnBehave;
import hearthstone.models.passive.Passive;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;

@Entity
public class TwiceDraw extends Passive implements StartTurnBehave {
    public TwiceDraw(){ }

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