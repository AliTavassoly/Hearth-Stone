package hearthstone.models.card.minion;

import hearthstone.models.hero.Hero;

public interface MinionBehaviour {
    public void battlecry();
    public void endTurnBehave();
    public void startTurnBehave();
    public void gotAttackedBehave();
    public void deathBehave();
    public void friendlyMinionDied();
    public boolean attack(MinionCard minionCard);
    public boolean attack(Hero hero);
    public boolean pressed();
}
