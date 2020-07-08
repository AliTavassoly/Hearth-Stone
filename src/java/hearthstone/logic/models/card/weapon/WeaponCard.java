package hearthstone.logic.models.card.weapon;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.IsAttacked;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.hero.IHero;
import hearthstone.util.HearthStoneException;

public abstract class WeaponCard extends Card implements IWeaponBehaviour {
    protected int durability;
    protected int attack;

    protected int numberOfAttack;
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

    protected void log(IHero hero){
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
    public void attack(IHero hero) throws HearthStoneException {
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
        } else if (object instanceof IHero) {
            if (((IHero) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((IHero) object);
                numberOfAttack--;
            }
        }
    }

    @Override
    public boolean pressed() {
        return canAttack();
    }
}