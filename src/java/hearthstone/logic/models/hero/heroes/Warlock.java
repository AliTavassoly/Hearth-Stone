package hearthstone.logic.models.hero.heroes;

import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.hero.IHero;
import hearthstone.logic.models.specialpower.specialpowers.RoguePower;
import hearthstone.logic.models.specialpower.specialpowers.WarlockPower;

public class Warlock extends IHero {
    public Warlock(){
        specialHeroPower = new RoguePower(super.getId(), "Rogue Power");
    }

    public Warlock(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new WarlockPower(id, "Warlock Power");
    }
}
