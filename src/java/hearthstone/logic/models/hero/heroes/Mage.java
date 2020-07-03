package hearthstone.logic.models.hero.heroes;

import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;

import java.util.List;

public class Mage extends Hero{
    public Mage(){ }

    public Mage(int id, String name, HeroType type, String description,
                String heroPowerName, int health, List<Integer> collection) throws Exception{
        super(id, name, type, description, heroPowerName, health, collection);
    }
}
