package hearthstone.util;


import hearthstone.HearthStone;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;

public class GetByName {
    public static Hero getHeroByName(String heroName) throws Exception {
        for (Hero hero : HearthStone.baseHeroes.values()) {
            if (hero.getName().equals(heroName)) {
                return hero.copy();
            }
        }
        throw new HearthStoneException("please enter correct hero name!");
    }

    public static Card getCardByName(String cardName) throws Exception {
        for (Card baseCard : HearthStone.baseCards.values()) {
            if (baseCard.getName().equals(cardName)) {
                return baseCard.copy();
            }
        }
        throw new HearthStoneException("please enter correct card name!");
    }
}
