package hearthstone.models.card.heropower.heropowers;

import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerBehaviour;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.hero.HeroType;

public class TheSilverHand extends HeroPowerCard implements HeroPowerBehaviour {
    public TheSilverHand() { }

    public TheSilverHand(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }
}
