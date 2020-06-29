package hearthstone.logic.models.card.heropower.heropowers;

import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerBehaviour;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.CursorType;

public class TheSilverHand extends HeroPowerCard implements HeroPowerBehaviour {
    public TheSilverHand() { }

    public TheSilverHand(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }

    private void doAbility(){
        getPlayer().setMana(getPlayer().getMana() - this.getManaCost());

        getPlayer().getFactory().summonMinionFromCurrentDeck(1, 1);
        getPlayer().getFactory().summonMinionFromCurrentDeck(1, 1);
        Mapper.getInstance().updateBoard();
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack = 1;
    }

    @Override
    public boolean canAttackThisTurn() {
        return numberOfAttack > 0 && getManaCost() <= getPlayer().getMana();
    }

    @Override
    public CursorType lookingForCursorType() {
        return CursorType.SEARCH;
    }

    @Override
    public void found(Object object) {
        if(object instanceof Hero && ((Hero)object).getPlayer() == this.getPlayer()){
            doAbility();
            numberOfAttack--;
        }
    }

    @Override
    public boolean pressed() {
        return canAttackThisTurn();
    }
}
