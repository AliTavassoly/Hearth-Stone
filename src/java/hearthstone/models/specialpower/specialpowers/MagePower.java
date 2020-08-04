package hearthstone.models.specialpower.specialpowers;

import hearthstone.Mapper;
import hearthstone.models.behaviours.StartGameBehave;
import hearthstone.models.specialpower.SpecialHeroPower;

import javax.persistence.Entity;

@Entity
public class MagePower extends SpecialHeroPower implements StartGameBehave {
    public MagePower(){}

    public MagePower(int id, String name){
        super(id, name);
    }

    @Override
    public void startGameBehave() {
        //Mapper.discountSpells(getPlayerId(), 2);
        Mapper.getPlayer(getPlayerId()).discountSpells(2);
    }
}
