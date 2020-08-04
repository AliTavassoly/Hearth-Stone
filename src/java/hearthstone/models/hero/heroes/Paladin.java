package hearthstone.models.hero.heroes;

import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.specialpower.specialpowers.PaladinPower;

import javax.persistence.Entity;

@Entity
public class Paladin extends Hero {
    public Paladin(){ }

    public Paladin(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new PaladinPower(id, "Paladin Power");
    }
}
