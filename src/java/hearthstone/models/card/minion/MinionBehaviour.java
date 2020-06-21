package hearthstone.models.card.minion;

import hearthstone.models.hero.Hero;

public interface MinionBehaviour {
    void battlecry();
    void endTurnBehave();
    void startTurnBehave();
    void gotAttackedBehave();
    void deathBehave();
    void friendlyMinionDied();
    void killedEnemyMinion();
    boolean attack(MinionCard minionCard);
    boolean attack(Hero hero);
    boolean pressed();
}
