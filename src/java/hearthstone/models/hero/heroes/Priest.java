package hearthstone.models.hero.heroes;

import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.specialpower.specialpowers.PriestPower;

import javax.persistence.Entity;

@Entity
public class Priest extends Hero {
    public Priest(){

    }

    public Priest(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new PriestPower(id, "Priest Power");
    }
}
