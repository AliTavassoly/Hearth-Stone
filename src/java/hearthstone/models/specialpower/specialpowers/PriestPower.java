package hearthstone.models.specialpower.specialpowers;

import hearthstone.models.specialpower.SpecialHeroPower;

import javax.persistence.Entity;

@Entity
public class PriestPower extends SpecialHeroPower {
    public PriestPower(){}

    public PriestPower(int id, String name){
        super(id, name);
    }

    // we have not healer spell !
}
