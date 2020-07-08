package hearthstone.logic.models.hero.heroes;

import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.specialpower.specialpowers.MagePower;

public class Mage extends Hero {
    public Mage(){
        specialHeroPower = new MagePower(super.getId(), "Mage Power");
    }

    public Mage(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new MagePower(id, "Mage Power");
    }
}
