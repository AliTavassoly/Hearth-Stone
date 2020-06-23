package hearthstone.models.card.minion;

import hearthstone.models.hero.Hero;
import hearthstone.util.HearthStoneException;

public interface MinionBehaviour {
    void battlecry();
    void endTurnBehave() throws HearthStoneException;
    void startTurnBehave() throws HearthStoneException;
    void gotAttackedBehave() throws HearthStoneException;
    void deathRattle() throws HearthStoneException;
    void friendlyMinionDied() throws HearthStoneException;
    void killedEnemyMinion() throws HearthStoneException;
    void attack(MinionCard minionCard) throws HearthStoneException;
    void attack(Hero hero) throws HearthStoneException;
    boolean pressed();
}
