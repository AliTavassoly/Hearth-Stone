package hearthstone.models.card.minions;

import hearthstone.models.hero.Hero;

public interface MinionBehaviour {
    public void drawBehave();
    public void endTurnBehave();
    public void startTurnBehave();
    public void gotAttackedBehave();
    public void deathBehave();
    public void friendlyMinionDied();
    public boolean attack(MinionCard minionCard);
    public boolean attack(Hero hero);
    public boolean found(Object minion);
    public boolean pressed();
}
