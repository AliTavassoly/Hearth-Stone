package hearthstone.logic.models.hero;

import hearthstone.util.HearthStoneException;

public interface IHeroBehaviour {
    void gotDamage(int damage) throws HearthStoneException;
    void gotHeal(int heal) throws HearthStoneException;
    void restoreHealth(int heal);

    void startTurnBehave();
}
