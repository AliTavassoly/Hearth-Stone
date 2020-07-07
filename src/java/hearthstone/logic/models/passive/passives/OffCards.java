package hearthstone.logic.models.passive.passives;

import hearthstone.Mapper;
import hearthstone.logic.interfaces.StartGameBehave;
import hearthstone.logic.models.passive.Passive;

public class OffCards extends Passive implements StartGameBehave {
    public OffCards(int id, String name){
        super(id, name);
    }


    @Override
    public void startGameBehave() {
        Mapper.getInstance().discountAllDeckCard(getPlayerId(), 1);
    }
}
