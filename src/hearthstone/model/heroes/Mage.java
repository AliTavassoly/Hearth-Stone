package hearthstone.model.heroes;

import java.util.List;

public class Mage extends Hero {
    public Mage(){ }

    public Mage(int id, String name, HeroType type, String description, int health, List<Integer> collection) throws Exception{
        super(id, name, type, description, health, collection);
    }
}
