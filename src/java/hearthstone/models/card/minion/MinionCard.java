package hearthstone.models.card.minion;

import hearthstone.models.card.Card;
import hearthstone.models.card.CardBehaviour;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;

public abstract class MinionCard extends Card implements MinionBehaviour, CardBehaviour {
    protected int health;
    protected int attack;
    protected int initialHealth;
    protected int initialAttack;
    protected boolean isTaunt;
    protected boolean isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield;
    protected boolean isCharge, isRush;

    private MinionType minionType;

    public MinionCard(){ }

    public MinionCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
        this.health = health;
        this.attack = attack;

        configMinion();
    }

    public MinionCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                      boolean isDeathRattle, boolean isTriggeredEffect, boolean isSpellDamage, boolean isDivineShield,
                      boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
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
        this.minionType = minionType;

        configMinion();
    }

    private void configMinion(){
        initialAttack = attack;
        initialHealth = health;
    }

    public int getInitialHealth() {
        return initialHealth;
    }
    public void setInitialHealth(int initialHealth) {
        this.initialHealth = initialHealth;
    }

    public int getInitialAttack() {
        return initialAttack;
    }
    public void setInitialAttack(int initialAttack) {
        this.initialAttack = initialAttack;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public int getHealth() {
        return health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
    public int getAttack() {
        return attack;
    }

    public boolean isTaunt() {
        return isTaunt;
    }
    public void setTaunt(boolean taunt) {
        isTaunt = taunt;
    }

    public boolean isDeathRattle() {
        return isDeathRattle;
    }
    public void setDeathRattle(boolean deathRattle) {
        isDeathRattle = deathRattle;
    }

    public boolean isTriggeredEffect() {
        return isTriggeredEffect;
    }
    public void setTriggeredEffect(boolean triggeredEffect) {
        isTriggeredEffect = triggeredEffect;
    }

    public boolean isSpellDamage() {
        return isSpellDamage;
    }
    public void setSpellDamage(boolean spellDamage) {
        isSpellDamage = spellDamage;
    }

    public boolean isDivineShield() {
        return isDivineShield;
    }
    public void setDivineShield(boolean divineShield) {
        isDivineShield = divineShield;
    }

    public MinionType getMinionType() {
        return minionType;
    }
    public void setMinionType(MinionType minionType) {
        this.minionType = minionType;
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
    public boolean attack(MinionCard minionCard) {
        return false;
    }

    @Override
    public boolean attack(Hero hero) {
        return false;
    }

    @Override
    public boolean found(Object object) {
        return false;
    }

    @Override
    public boolean pressed() {
        return false;
    }
}
