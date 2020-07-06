package hearthstone.logic.models.card.heropower;

import hearthstone.DataTransform;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.hero.HeroType;

public abstract class HeroPowerCard extends Card implements HeroPowerBehaviour {
    protected int extraNumberOfAttack;
    protected int numberOfAttack;

    public HeroPowerCard() {
    }

    public HeroPowerCard(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    public void setExtraNumberOfAttack(int extraNumberOfAttack) {
        this.extraNumberOfAttack = extraNumberOfAttack;
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack = 1 + extraNumberOfAttack;
    }

    @Override
    public boolean canAttack() {
        return numberOfAttack > 0 &&
                getManaCost() <= DataTransform.getInstance().getMana(getPlayerId()) &&
                DataTransform.getInstance().getWhoseTurn() == getPlayerId();
    }
}
