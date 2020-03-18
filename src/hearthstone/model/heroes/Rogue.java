package hearthstone.model.heroes;

import java.util.List;

public class Rogue extends Hero{
    public Rogue(){ }

    public Rogue(int id, String name, HeroType type, String description, int health, List<Integer> collection) throws Exception{
        super(id, name, type, description, health, collection);
    }
}
