package hearthstone.models.card.heropower;

import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.hero.HeroType;

public abstract class HeroPowerCard extends Card {
    public HeroPowerCard() { }

    public HeroPowerCard(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }
}
