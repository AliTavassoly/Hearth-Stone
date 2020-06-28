package hearthstone.logic.models.card.weapon;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.IsAttacked;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

public abstract class WeaponCard extends Card implements WeaponBehaviour{
    protected int durability;
    protected int attack;

    protected int numberOfAttack;
    protected boolean isFirstTurn;

    public WeaponCard() { }

    public WeaponCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack){
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

    public boolean canAttack(){
        return numberOfAttack > 0 &&
                DataTransform.getInstance().getWhoseTurn() == getPlayer().getPlayerId();
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack++;
        durability--;
        isFirstTurn = false;
    }

    @Override
    public void attack(MinionCard minionCard) throws HearthStoneException{
        try {
            Mapper.getInstance().damage(this.attack, minionCard);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(minionCard instanceof IsAttacked){
            ((IsAttacked)minionCard).isAttacked();
        }
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (hero.getPlayer().haveTaunt()) {
            throw new HearthStoneException("There is taunt in front of you!");
        }
        Mapper.getInstance().damage(this.attack, hero);
    }

    @Override
    public void found(Object object) throws HearthStoneException{
        if (object instanceof MinionCard) {
            if (((Card) object).getPlayer() == this.getPlayer()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((MinionCard) object);
                numberOfAttack--;
            }
        } else if (object instanceof Hero) {
            if (((Hero) object).getPlayer() == this.getPlayer()) {
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