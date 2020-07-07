package hearthstone.logic.models.card.reward;

import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.hero.HeroType;

public abstract class RewardCard extends Card implements RewardBehaviour{
    public RewardCard(){ }

    public RewardCard(int id, String name, String description,
                      int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    protected void log(){
        try {
            hearthstone.util.Logger.saveLog("Reward Action", this.getName() + "' " +
                    "side quest were done and it's reward has been received!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
