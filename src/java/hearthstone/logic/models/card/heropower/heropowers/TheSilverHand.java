package hearthstone.logic.models.card.heropower.heropowers;

import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerBehaviour;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.hero.HeroType;

public class TheSilverHand extends HeroPowerCard implements HeroPowerBehaviour {
    public TheSilverHand() { }

    public TheSilverHand(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }
}
