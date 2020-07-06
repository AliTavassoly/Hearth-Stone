package hearthstone.logic.models;

import hearthstone.util.HearthStoneException;

public interface Character {
    void gotDamage(int damage) throws HearthStoneException;
    void gotHeal(int heal);
    int getHealth();
    void setHealth(int health);
    void restoreHealth(int heal);
    void restoreHealth();
    void addFreezes(int turnNumber);
    void addImmunity(int turnNumber);
    void reduceImmunities();
    void reduceFreezes();
    void handleImmunities();
    void handleFreezes();
    int getPlayerId();
    boolean isSpellSafe();
}
