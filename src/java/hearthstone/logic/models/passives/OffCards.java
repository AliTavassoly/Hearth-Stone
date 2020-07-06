package hearthstone.logic.models.passives;

import hearthstone.Mapper;
import hearthstone.logic.models.card.interfaces.StartGameBehave;

public class OffCards extends Passive implements StartGameBehave {
    public OffCards(int id, String name){
        super(id, name);
    }


    @Override
    public void startGameBehave() {
        Mapper.getInstance().discountAllDeckCard(getPlayerId(), 1);
    }
}
