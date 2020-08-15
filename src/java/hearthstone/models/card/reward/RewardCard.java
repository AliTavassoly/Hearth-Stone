package hearthstone.models.card.reward;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public abstract class RewardCard extends Card implements RewardBehaviour {
    @Transient
    @JsonProperty("percentage")
    protected int percentage;

    public RewardCard(){ }

    public RewardCard(int id, String name, String description,
                      int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    public int getPercentage(){
        return percentage;
    }

    public void setPercentage(int percentage){
        this.percentage = percentage;
    }
}
