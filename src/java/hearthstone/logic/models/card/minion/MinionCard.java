package hearthstone.logic.models.card.minion;

import hearthstone.Mapper;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.IsAttacked;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

public abstract class MinionCard extends Card implements MinionBehaviour {
    protected int health;
    protected int attack;
    protected int initialHealth;
    protected int initialAttack;
    protected boolean isTaunt;
    protected boolean isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield;
    protected boolean isCharge, isRush;
    protected boolean hasWaitingForDraw, hasEndTurnBehave;
    protected boolean isImmune;

    protected int numberOfAttack;
    protected boolean isFirstTurn;
    protected int numberOfAttackedMinion;
    protected int numberOfAttackedHero;

    private MinionType minionType;


    public MinionCard() {
    }

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

        isFirstTurn = true;

        configMinion();
    }

    private void configMinion() {
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

    public boolean isImmune() {
        return isImmune;
    }

    public void setImmune(boolean immune) {
        isImmune = immune;
    }

    public int getNumberOfAttack() {
        return numberOfAttack;
    }

    public void setNumberOfAttack(int numberOfAttack) {
        this.numberOfAttack = numberOfAttack;
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack = 1;
        isFirstTurn = false;
    }

    @Override
    public void gotDamage(int damage) throws HearthStoneException {
        if(isImmune)
            throw new HearthStoneException("The minion is Immune!");
        health -= damage;
    }

    @Override
    public void gotHeal(int heal) throws HearthStoneException {
        this.health += heal;
    }

    @Override
    public void changeAttack(int attack) {
        this.attack += attack;
    }

    @Override
    public void attack(MinionCard minionCard) {
        try {
            Mapper.getInstance().damage(this.attack, minionCard);
        } catch (Exception ignore) { }

        if (minionCard instanceof IsAttacked) {
            ((IsAttacked) minionCard).isAttacked();
        }

        try {
            Mapper.getInstance().damage(minionCard.getAttack(), this);
        } catch (Exception ignore) { }
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (hero.getPlayer().haveTaunt()) {
            throw new HearthStoneException("There is taunt in front of you!");
        }
        Mapper.getInstance().damage(this.attack, hero);
    }

    @Override
    public void found(Object object) throws HearthStoneException {
        if (object instanceof MinionCard) {
            if (((Card) object).getPlayer() == this.getPlayer()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((MinionCard) object);
                numberOfAttack--;
                numberOfAttackedMinion++;
            }
        } else if (object instanceof Hero) {
            if (((Hero) object).getPlayer() == this.getPlayer()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((Hero) object);
                numberOfAttack--;
                numberOfAttackedHero++;
            }
        }
    }

    @Override
    public boolean pressed() {
        return numberOfAttack > 0;
    }
}
