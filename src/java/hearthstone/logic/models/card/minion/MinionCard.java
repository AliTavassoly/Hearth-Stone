package hearthstone.logic.models.card.minion;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.ICharacter;
import hearthstone.logic.behaviours.IsAttacked;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.hero.IHero;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public abstract class MinionCard extends Card implements IMinionBehaviour, ICharacter {
    protected int health;
    protected int attack;
    protected int initialHealth;
    protected int initialAttack;
    protected boolean isTaunt;
    protected boolean isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield;
    protected boolean isCharge, isRush;
    protected boolean isImmune, isFreeze;

    private ArrayList<Integer> immunities, freezes;

    protected int numberOfAttack;
    protected boolean isFirstTurn;
    protected int numberOfAttackedMinion;
    protected int numberOfAttackedHero;

    protected boolean spellSafe;

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

    public void log(IHero hero){
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
            Mapper.getInstance().damage(this.attack, minionCard);
            log(minionCard);
        }

        if (minionCard instanceof IsAttacked) {
            Mapper.getInstance().isAttacked((IsAttacked) minionCard);
        }

        try {
            Mapper.getInstance().damage(minionCard.getAttack(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attack(IHero hero) throws HearthStoneException {
        if (DataTransform.getInstance().haveTaunt(hero.getPlayerId())) {
            throw new HearthStoneException("There is taunt in front of you!");
        } else if (isFirstTurn) {
            if (isRush && !isCharge) {
                throw new HearthStoneException("Rush card can not attack to hero in first turn!");
            }
        }
        Mapper.getInstance().damage(this.attack, hero);
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
        } else if (object instanceof IHero) {
            if (((IHero) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((IHero) object);
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
