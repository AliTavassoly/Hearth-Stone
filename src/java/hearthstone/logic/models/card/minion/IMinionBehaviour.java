package hearthstone.logic.models.card.minion;

import hearthstone.logic.models.hero.IHero;
import hearthstone.util.HearthStoneException;

public interface IMinionBehaviour {
    void startTurnBehave() throws HearthStoneException;

    void changeAttack(int attack) throws HearthStoneException;

    void attack(MinionCard minionCard) throws HearthStoneException;
    void attack(IHero hero) throws HearthStoneException;

    boolean canAttack();
}
