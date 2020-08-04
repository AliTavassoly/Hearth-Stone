package hearthstone.models.card.minion;

import hearthstone.Mapper;
import hearthstone.models.behaviours.Character;
import hearthstone.models.behaviours.IsAttacked;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;

@Entity
public abstract class MinionCard extends Card implements MinionBehaviour, Character {
    @Column
    protected int health;
    @Column
    protected int attack;
    @Column
    protected int initialHealth;
    @Column
    protected int initialAttack;
    @Column
    protected boolean isTaunt;
    @Column
    protected boolean isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield;
    @Column
    protected boolean isCharge, isRush;

    @Transient
    protected boolean isImmune, isFreeze;

    @Transient
    private ArrayList<Integer> immunities, freezes;

    @Transient
    protected int numberOfAttack;
    @Transient
    protected boolean isFirstTurn;
    @Transient
    protected int numberOfAttackedMinion;
    @Transient
    protected int numberOfAttackedHero;

    protected boolean spellSafe;

    @Column
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
                      boolean isDeathRattle, boolean isTriggeredEffect,
                      boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                      boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
        this.health = health;
        this.attack = attack;
        this.isTaunt = isTaunt;

        this.isDeathRattle = isDeathRattle;
        this.isTriggeredEffect = isTriggeredEffect;
        this.isSpellSafe = isSpellSafe;
        this.isHeroPowerSafe = isHeroPowerSafe;
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

    public boolean isCharge() {
        return isCharge;
    }

    public void setCharge(boolean isCharge) {
        this.isCharge = isCharge;
    }

    public int getNumberOfAttack() {
        return numberOfAttack;
    }

    public void setNumberOfAttack(int numberOfAttack) {
        this.numberOfAttack = numberOfAttack;
    }

    public void setFreeze(boolean isFreeze) {
        this.isFreeze = isFreeze;
    }

    public boolean isFreeze() {
        return isFreeze;
    }

    public boolean isSpellSafe() {
        return spellSafe;
    }

    public void setSpellSafe(boolean spellSafe){
        this.spellSafe = spellSafe;
    }

    public boolean isHeroPowerSafe(){
        return isHeroPowerSafe;
    }

    public void setHeroPowerSafe(boolean isHeroPowerSafe){
        this.isHeroPowerSafe = isHeroPowerSafe;
    }

    // END OF GETTER SETTER

    public void removeDivineShield() {
        this.isDivineShield = false;
    }

    public void log(Hero hero){
        try {
            hearthstone.util.Logger.saveLog("Minion Attack",
                    this.getName() + " attack to " + hero.getName() + "!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void log(MinionCard minion){
        try {
            hearthstone.util.Logger.saveLog("Minion Attack",
                    this.getName() + " attack to " + minion.getName() + "!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void reduceImmunities() {
        for (int i = 0; i < immunities.size(); i++) {
            immunities.set(i, immunities.get(i) - 1);
            if (immunities.get(i) <= 0) {
                immunities.remove(i);
                i--;
            }
        }
    }

    @Override
    public void reduceFreezes() {
        for (int i = 0; i < freezes.size(); i++) {
            freezes.set(i, freezes.get(i) - 1);
            if (freezes.get(i) <= 0) {
                freezes.remove(i);
                i--;
            }
        }
    }

    @Override
    public void handleImmunities() {
        if (immunities.size() > 0)
            isImmune = true;
        else
            isImmune = false;
    }

    @Override
    public void handleFreezes() {
        if (freezes.size() > 0)
            isFreeze = true;
        else
            isFreeze = false;
    }

    @Override
    public void addImmunity(int numberOfTurn) {
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

        //Mapper.reduceImmunities(getPlayerId(), this);
        this.reduceImmunities();

        //Mapper.handleImmunities(getPlayerId(), this);
        this.handleImmunities();

        //Mapper.reduceFreezes(getPlayerId(), this);
        this.reduceFreezes();

        //Mapper.handleFreezes(getPlayerId(), this);
        this.handleFreezes();
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
    public void restoreHealth() {
        this.health = initialHealth;
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
    public void attack(MinionCard minionCard) throws HearthStoneException {
        if (!minionCard.isImmune && minionCard.isDivineShield) {
            minionCard.removeDivineShield();
        } else {
            //Mapper.damage(this.attack, minionCard);
            minionCard.gotDamage(this.attack);
            Mapper.updateBoard();
            log(minionCard);
        }

        if (minionCard instanceof IsAttacked) {
            //Mapper.isAttacked((IsAttacked) minionCard);
            ((IsAttacked) minionCard).isAttacked();
        }

        try {
            //Mapper.damage(minionCard.getAttack(), this);
            this.gotDamage(minionCard.getAttack());
            Mapper.updateBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (/*DataTransform.haveTaunt(hero.getPlayerId())*/Mapper.getPlayer(hero.getPlayerId()).haveTaunt()) {
            throw new HearthStoneException("There is taunt in front of you!");
        } else if (isFirstTurn) {
            if (isRush && !isCharge) {
                throw new HearthStoneException("Rush card can not attack to hero in first turn!");
            }
        }
        //Mapper.damage(this.attack, hero);
        hero.gotDamage(this.attack);
        Mapper.updateBoard();

        log(hero);
    }

    @Override
    public void found(Object object) throws HearthStoneException {
        if (object instanceof MinionCard) {
            MinionCard minion = (MinionCard) object;
            if (minion.getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack(minion);
                if (!isFirstTurn)
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
        if (isFreeze)
            return false;
        if (numberOfAttack > 0)
            return true;
        if (numberOfAttack == 0 && (isRush || isCharge))
            return true;
        return false;
    }
}
