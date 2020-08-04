package hearthstone.models.card.heropower;

import hearthstone.Mapper;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public abstract class HeroPowerCard extends Card implements HeroPowerBehaviour {
    @Transient
    protected int extraNumberOfAttack;
    @Transient
    protected int numberOfAttack;

    public HeroPowerCard() {
    }

    public HeroPowerCard(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    public void setExtraNumberOfAttack(int extraNumberOfAttack) {
        this.extraNumberOfAttack = extraNumberOfAttack;
    }

    public void log(){
        try {
            hearthstone.util.Logger.saveLog("Power Action", this.getName() + " power used!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack = 1 + extraNumberOfAttack;
    }

    @Override
    public boolean canAttack() {
        return numberOfAttack > 0 &&
                getManaCost() <= Mapper.getMana(getPlayerId()) &&
                Mapper.getWhoseTurn() == getPlayerId();
    }
}
