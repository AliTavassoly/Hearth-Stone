package hearthstone.logic.models;

import hearthstone.util.HearthStoneException;

public interface Character {
    void gotDamage(int damage) throws HearthStoneException;
    void gotHeal(int heal);
    void setHealth(int health);
    void restoreHealth(int heal);
}
