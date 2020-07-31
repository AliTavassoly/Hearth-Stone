package hearthstone.models.specialpower.specialpowers;

import hearthstone.models.specialpower.SpecialHeroPower;

import javax.persistence.Entity;

@Entity
public class RoguePower extends SpecialHeroPower {
    public RoguePower(){}

    public RoguePower(int id, String name){
        super(id, name);
    }

    // upgrading hero power and 2 mana discount on cards that are not in this class
}
