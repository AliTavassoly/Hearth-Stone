package hearthstone.models.hero.heroes;

import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.specialpower.specialpowers.RoguePower;

import javax.persistence.Entity;

@Entity
public class Rogue extends Hero {
    public Rogue(){
        specialHeroPower = new RoguePower(super.getId(), "Rogue Power");
    }

    public Rogue(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new RoguePower(id, "Rogue Power");
    }
}
