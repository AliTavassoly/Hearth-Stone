package hearthstone.models.card.weapon;

import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.util.HearthStoneException;

public interface WeaponBehaviour {
    void startTurnBehave() throws HearthStoneException;
    void attack(MinionCard minionCard) throws HearthStoneException;
    void attack(Hero hero) throws HearthStoneException;
    boolean pressed();
}
