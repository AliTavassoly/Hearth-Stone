package hearthstone.logic.models.card.cards;

import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.hero.HeroType;

public class HeroPowerCard extends Card {
    public HeroPowerCard() { }

    public HeroPowerCard(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }
}
