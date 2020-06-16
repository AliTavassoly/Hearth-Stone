package hearthstone.models.card.cards;

import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.hero.HeroType;

public class MinionCard extends Card {
    protected int health, attack;
    protected boolean isTaunt;
    protected boolean isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield;
    protected boolean isCharge, isRush;

    public MinionCard(){ }

    public MinionCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
        this.health = health;
        this.attack = attack;
    }

    public MinionCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                      boolean isTaunt, boolean isDeathRattle, boolean isTriggeredEffect, boolean isSpellDamage, boolean isDivineShield,
                      boolean isCharge, boolean isRush) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
        this.health = health;
        this.attack = attack;
        this.isTaunt = isTaunt;

        this.isDeathRattle = isDeathRattle;
        this.isTriggeredEffect = isTriggeredEffect;
        this.isSpellDamage = isSpellDamage;
        this.isDivineShield = isDivineShield;

        this.isCharge = isCharge;
        this.isRush = isRush;
    }

    protected void setHealth(int health) {
        this.health = health;
    }
    protected int getHealth() {
        return health;
    }

    protected void setAttack(int attack) {
        this.attack = attack;
    }
    protected int getAttack() {
        return attack;
    }

    protected boolean isTaunt() {
        return isTaunt;
    }
    protected void setTaunt(boolean taunt) {
        isTaunt = taunt;
    }

    protected boolean isDeathRattle() {
        return isDeathRattle;
    }
    protected void setDeathRattle(boolean deathRattle) {
        isDeathRattle = deathRattle;
    }

    protected boolean isTriggeredEffect() {
        return isTriggeredEffect;
    }
    protected void setTriggeredEffect(boolean triggeredEffect) {
        isTriggeredEffect = triggeredEffect;
    }

    protected boolean isSpellDamage() {
        return isSpellDamage;
    }
    protected void setSpellDamage(boolean spellDamage) {
        isSpellDamage = spellDamage;
    }

    protected boolean isDivineShield() {
        return isDivineShield;
    }
    protected void setDivineShield(boolean divineShield) {
        isDivineShield = divineShield;
    }
}
