package hearthstone.models.passive.passives;

import hearthstone.Mapper;
import hearthstone.models.behaviours.StartGameBehave;
import hearthstone.models.passive.Passive;

import javax.persistence.Entity;

@Entity
public class OffCards extends Passive implements StartGameBehave {
    public OffCards(){ }

    public OffCards(int id, String name){
        super(id, name);
    }


    @Override
    public void startGameBehave() {
        Mapper.getInstance().discountAllDeckCard(getPlayerId(), 1);
    }
}
