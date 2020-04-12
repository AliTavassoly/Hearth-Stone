package hearthstone.logic.models.heroes;

import java.util.List;

public class Warlock extends Hero implements Cloneable {
    public Warlock(){ }

    public Warlock(int id, String name, HeroType type, String description, int health, List<Integer> collection) throws Exception{
        super(id, name, type, description, health, collection);
    }
}
