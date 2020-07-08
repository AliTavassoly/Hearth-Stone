package hearthstone.logic.models.hero.heroes;

import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.hero.IHero;
import hearthstone.logic.models.specialpower.specialpowers.RoguePower;

public class Rogue extends IHero {
    public Rogue(){
        specialHeroPower = new RoguePower(super.getId(), "Rogue Power");
    }

    public Rogue(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new RoguePower(id, "Rogue Power");
    }
}
