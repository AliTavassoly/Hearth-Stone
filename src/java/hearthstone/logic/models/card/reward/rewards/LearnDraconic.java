package hearthstone.logic.models.card.reward.rewards;

import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.reward.RewardCard;
import hearthstone.logic.models.hero.HeroType;

public class LearnDraconic extends RewardCard {
    public LearnDraconic(){ }

    public LearnDraconic(int id, String name, String description,
                      int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }
}
