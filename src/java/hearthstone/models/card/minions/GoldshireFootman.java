package hearthstone.models.card.minions;

import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;

public class GoldshireFootman extends MinionCard implements MinionBehaviour{

    public GoldshireFootman(){ }

    public GoldshireFootman(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                      boolean isDeathRattle, boolean isTriggeredEffect, boolean isSpellDamage, boolean isDivineShield,
                      boolean isTaunt, boolean isCharge, boolean isRush) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield,
                isTaunt, isCharge, isRush);
    }

    @Override
    public void drawBehave() {

    }

    @Override
    public void endTurnBehave() {

    }

    @Override
    public void startTurnBehave() {

    }

    @Override
    public void gotAttackedBehave() {

    }

    @Override
    public void deathBehave() {

    }

    @Override
    public void friendlyMinionDied() {

    }

    @Override
    public void attack(MinionCard minionCard) {
        minionCard.setHealth(minionCard.getHealth() - this.attack);
        this.health -= minionCard.getAttack();
    }

    @Override
    public void attack(Hero hero) {
        hero.setHealth(hero.getHealth() - this.attack);
    }
}
