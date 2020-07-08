package hearthstone.logic.models.hero.heroes;

import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.specialpower.specialpowers.PriestPower;

public class Priest extends Hero {
    public Priest(){
        specialHeroPower = new PriestPower(super.getId(), "Priest Power");
    }

    public Priest(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new PriestPower(id, "Priest Power");
    }
}
