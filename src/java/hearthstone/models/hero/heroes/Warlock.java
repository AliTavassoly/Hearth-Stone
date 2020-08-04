package hearthstone.models.hero.heroes;

import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.specialpower.specialpowers.RoguePower;
import hearthstone.models.specialpower.specialpowers.WarlockPower;

import javax.persistence.Entity;

@Entity
public class Warlock extends Hero {
    public Warlock(){ }

    public Warlock(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new WarlockPower(id, "Warlock Power");
    }
}
