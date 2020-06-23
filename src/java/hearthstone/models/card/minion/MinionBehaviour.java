package hearthstone.models.card.minion;

import hearthstone.models.hero.Hero;
import hearthstone.util.HearthStoneException;

public interface MinionBehaviour {
    void startTurnBehave() throws HearthStoneException;
    void attack(MinionCard minionCard) throws HearthStoneException;
    void attack(Hero hero) throws HearthStoneException;
    boolean pressed();
}
