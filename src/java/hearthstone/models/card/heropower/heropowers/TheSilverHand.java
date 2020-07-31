package hearthstone.models.card.heropower.heropowers;

import hearthstone.Mapper;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;

import javax.persistence.Entity;

@Entity
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
        if(object instanceof Hero && ((Hero)object).getPlayerId() == this.getPlayerId()){
            doAbility();
            numberOfAttack--;
        }
    }
}
