package hearthstone.logic.models.card.weapon;

import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.hero.IHero;
import hearthstone.util.HearthStoneException;

public interface IWeaponBehaviour {
    void startTurnBehave() throws HearthStoneException;
    void attack(MinionCard minionCard) throws HearthStoneException;
    void attack(IHero hero) throws HearthStoneException;
    boolean pressed();
}
