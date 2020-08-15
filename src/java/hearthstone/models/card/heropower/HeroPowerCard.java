package hearthstone.models.card.heropower;

import com.fasterxml.jackson.annotation.JsonProperty;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public abstract class HeroPowerCard extends Card implements HeroPowerBehaviour {
    @Transient
    @JsonProperty("extraNumberOfAttack")
    protected int extraNumberOfAttack;

    @Transient
    @JsonProperty("numberOfAttack")
    protected int numberOfAttack;

    @Transient
    @JsonProperty("isFirstTurn")
    protected boolean canAttack;

    public HeroPowerCard() {
    }

    public HeroPowerCard(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    public void setExtraNumberOfAttack(int extraNumberOfAttack) {
        this.extraNumberOfAttack = extraNumberOfAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack = 1 + extraNumberOfAttack;
    }

    @Override
    public boolean canAttack() {
        return numberOfAttack > 0 &&
                getManaCost() <= HSServer.getInstance().getPlayer(getPlayerId()).getMana() &&
                HSServer.getInstance().getPlayer(getPlayerId()).isMyTurn();
    }

    @Override
    public boolean isCanAttack() {
        return canAttack;
    }
}
