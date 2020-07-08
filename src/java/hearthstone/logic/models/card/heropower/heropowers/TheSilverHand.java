package hearthstone.logic.models.card.heropower.heropowers;

import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.hero.IHero;
import hearthstone.util.CursorType;

public class TheSilverHand extends HeroPowerCard  {
    public TheSilverHand() { }

    public TheSilverHand(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }

    private void doAbility(){
        Mapper.getInstance().reduceMana(getPlayerId(), this.getManaCost());

        Mapper.getInstance().summonMinionFromCurrentDeck(getPlayerId(), 1, 1);
        Mapper.getInstance().summonMinionFromCurrentDeck(getPlayerId(), 1, 1);
        Mapper.getInstance().updateBoard();

        log();
    }

    @Override
    public CursorType lookingForCursorType() {
        return CursorType.SEARCH;
    }

    @Override
    public void found(Object object) {
        if(object instanceof IHero && ((IHero)object).getPlayerId() == this.getPlayerId()){
            doAbility();
            numberOfAttack--;
        }
    }
}
