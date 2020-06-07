package hearthstone.models.hero.heroes;

import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;

import java.util.List;

public class Paladin extends Hero {
    public Paladin(){ }

    public Paladin(int id, String name, HeroType type, String description, int health, List<Integer> collection) throws Exception{
        super(id, name, type, description, health, collection);
    }
}