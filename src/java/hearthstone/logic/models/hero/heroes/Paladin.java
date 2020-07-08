package hearthstone.logic.models.hero.heroes;

import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.hero.IHero;
import hearthstone.logic.models.specialpower.specialpowers.PaladinPower;

public class Paladin extends IHero {
    public Paladin(){
        specialHeroPower = new PaladinPower(super.getId(), "Paladin Power");
    }

    public Paladin(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new PaladinPower(id, "Paladin Power");
    }
}
