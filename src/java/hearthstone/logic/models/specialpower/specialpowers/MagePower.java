package hearthstone.logic.models.specialpower.specialpowers;

import hearthstone.Mapper;
import hearthstone.logic.models.card.interfaces.StartGameBehave;
import hearthstone.logic.models.specialpower.SpecialHeroPower;

public class MagePower extends SpecialHeroPower implements StartGameBehave {
    public MagePower(int id, String name){
        super(id, name);
    }

    @Override
    public void startGameBehave() {
        Mapper.getInstance().discountSpells(getPlayerId(), 2);
    }
}
