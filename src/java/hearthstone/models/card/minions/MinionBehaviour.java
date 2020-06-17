package hearthstone.models.card.minions;

import hearthstone.models.hero.Hero;

public interface MinionBehaviour {
    public void drawBehave();
    public void endTurnBehave();
    public void startTurnBehave();
    public void gotAttackedBehave();
    public void deathBehave();
    public void friendlyMinionDied();
    public void attack(MinionCard minionCard);
    public void attack(Hero hero);
}
