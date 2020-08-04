package hearthstone.models.passive.passives;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.behaviours.StartGameBehave;
import hearthstone.models.passive.Passive;

import javax.persistence.Entity;

@Entity
public class ManaJump extends Passive implements StartGameBehave {
    public ManaJump(){ }

    public ManaJump(int id, String name){
        super(id, name);
    }

    @Override
    public void startGameBehave() {
        //Mapper.setExtraMane(getPlayerId(), 1);
        DataTransform.getPlayer(getPlayerId()).setExtraMana(1);
    }
}
