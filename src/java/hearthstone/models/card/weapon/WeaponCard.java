package hearthstone.models.card.weapon;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.behaviours.IsAttacked;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public abstract class WeaponCard extends Card implements WeaponBehaviour {
    @Column
    protected int durability;
    @Column
    protected int attack;

    @Transient
    protected int numberOfAttack;
    @Transient
    protected boolean isFirstTurn;

    public WeaponCard() {
    }

    public WeaponCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
        this.durability = durability;
        this.attack = attack;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    protected void log(Hero hero){
        try {
            hearthstone.util.Logger.saveLog("Weapon Attack", this.getName() + " attacked to " + hero.getName() + "!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void log(MinionCard minion){
        try {
            hearthstone.util.Logger.saveLog("Weapon Attack", this.getName() + " attacked to " + minion.getName() + "!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean canAttack() {
        return numberOfAttack > 0 &&
                DataTransform.getInstance().getWhoseTurn() == getPlayerId() && !DataTransform.getInstance().getHero(getPlayerId()).isFreeze();
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack = 1;
        durability--;
        isFirstTurn = false;
    }

    @Override
    public void attack(MinionCard minionCard) throws HearthStoneException {
        Mapper.getInstance().damage(this.attack, minionCard);
        log(minionCard);

        try {
            Mapper.getInstance().damage(minionCard.getAttack(),
                    DataTransform.getInstance().getHero(getPlayerId()));
        } catch (HearthStoneException ignore) {
        }

        if (minionCard instanceof IsAttacked)
            Mapper.getInstance().isAttacked((IsAttacked) minionCard);
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (DataTransform.getInstance().haveTaunt(hero.getPlayerId())) {
            throw new HearthStoneException("There is taunt in front of you!");
        }
        Mapper.getInstance().damage(this.attack, hero);
        log(hero);
    }

    @Override
    public void found(Object object) throws HearthStoneException {
        if (object instanceof MinionCard) {
            if (((Card) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((MinionCard) object);
                numberOfAttack--;
            }
        } else if (object instanceof Hero) {
            if (((Hero) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((Hero) object);
                numberOfAttack--;
            }
        }
    }

    @Override
    public boolean pressed() {
        return canAttack();
    }
}