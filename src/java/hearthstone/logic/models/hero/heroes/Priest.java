package hearthstone.logic.models.hero.heroes;

import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;

import java.util.List;

public class Priest extends Hero {
    public Priest(){ }

    public Priest(int id, String name, HeroType type, String description,
                String heroPowerName, int health, List<Integer> collection) throws Exception{
        super(id, name, type, description, heroPowerName, health, collection);
    }
}
