package hearthstone.models.specialpower.specialpowers;

import hearthstone.models.specialpower.SpecialHeroPower;

import javax.persistence.Entity;

@Entity
public class WarlockPower extends SpecialHeroPower {
    public WarlockPower(){}

    public WarlockPower(int id, String name){
        super(id, name);
    }

    // 35 health which  developed in hero class
}
