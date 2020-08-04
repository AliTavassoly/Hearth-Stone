package hearthstone.models.hero.heroes;

import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.specialpower.specialpowers.MagePower;

import javax.persistence.Entity;

@Entity
public class Mage extends Hero {
    public Mage(){ }

    public Mage(int id, String name, HeroType type, String description,
                String heroPowerName, int health) throws Exception{
        super(id, name, type, description, heroPowerName, health);

        specialHeroPower = new MagePower(id, "Mage Power");
    }
}
