package hearthstone.logic.models.card.minion;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.Character;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.IsAttacked;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public abstract class MinionCard extends Card implements MinionBehaviour, Character {
    protected int health;
    protected int attack;
    protected int initialHealth;
    protected int initialAttack;
    protected boolean isTaunt;
    protected boolean isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield;
    protected boolean isCharge, isRush;
    protected boolean hasWaitingForDraw, hasEndTurnBehave;
    protected boolean isImmune, isFreeze;

    private ArrayList<Integer> immunities, freezes;

    protected int numberOfAttack;
    protected boolean isFirstTurn;
    protected int numberOfAttackedMinion;
    protected int numberOfAttackedHero;

    private MinionType minionType;


    public MinionCard() {
        configMinion();
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

        freezes = new ArrayList<>();
        immunities = new ArrayList<>();
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

    public void setFreeze(boolean isFreeze){
        this.isFreeze = isFreeze;
    }

    public boolean isFreeze(){
        return isFreeze;
    }

    @Override
    public void reduceImmunities(){
        for(int i = 0; i < immunities.size(); i++){
            immunities.set(i, immunities.get(i) - 1);
            if(immunities.get(i) <= 0){
                immunities.remove(i);
                i--;
            }
        }
    }

    @Override
    public void reduceFreezes(){
        for(int i = 0; i < freezes.size(); i++){
            freezes.set(i, freezes.get(i) - 1);
            if(freezes.get(i) <= 0){
                freezes.remove(i);
                i--;
            }
        }
    }

    @Override
    public void handleImmunities(){
        if(immunities.size() > 0)
            isImmune = true;
        else
            isImmune = false;
    }

    @Override
    public void handleFreezes(){
        if(freezes.size() > 0)
            isFreeze = true;
        else
            isFreeze = false;
    }

    @Override
    public void addImmunity(int numberOfTurn){
        immunities.add(numberOfTurn);
    }

    @Override
    public void addFreezes(int numberOfTurn) {
        freezes.add(numberOfTurn);
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack = 1;
        isFirstTurn = false;

        Mapper.getInstance().reduceImmunities(getPlayerId(), this);
        Mapper.getInstance().handleImmunities(getPlayerId(), this);

        Mapper.getInstance().reduceFreezes(getPlayerId(), this);
        Mapper.getInstance().handleFreezes(getPlayerId(), this);
    }

    @Override
    public void gotDamage(int damage) throws HearthStoneException {
        if (isImmune)
            throw new HearthStoneException("The minion is Immune!");
        health -= damage;
    }

    @Override
    public void gotHeal(int heal) {
        this.health += heal;
    }

    @Override
    public void restoreHealth(int heal) {
        this.health = Math.min(initialHealth, this.health + heal);
    }

    @Override
    public void changeAttack(int attack) {
        this.attack += attack;
    }

    @Override
    public void attack(MinionCard minionCard) {
        try {
            Mapper.getInstance().damage(this.attack, minionCard);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (minionCard instanceof IsAttacked) {
            Mapper.getInstance().isAttacked((IsAttacked)minionCard);
        }

        try {
            Mapper.getInstance().damage(minionCard.getAttack(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (DataTransform.getInstance().haveTaunt(hero.getPlayerId())) {
            throw new HearthStoneException("There is taunt in front of you!");
        }
        Mapper.getInstance().damage(this.attack, hero);
    }

    @Override
    public void found(Object object) throws HearthStoneException {
        if (object instanceof MinionCard) {
            if (((Card) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((MinionCard) object);
                numberOfAttack--;
                numberOfAttackedMinion++;
            }
        } else if (object instanceof Hero) {
            if (((Hero) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((Hero) object);
                numberOfAttack--;
                numberOfAttackedHero++;
            }
        }
    }

    @Override
    public boolean canAttack() {
        return numberOfAttack > 0 && !isFreeze;
    }
}
