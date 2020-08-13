package hearthstone.models.card.heropower.heropowers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import hearthstone.models.behaviours.Upgradeable;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;
import hearthstone.util.CursorType;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class AncientBlades extends HeroPowerCard implements Upgradeable {
    @Transient
    @JsonProperty("upgraded")
    private boolean upgraded;

    public AncientBlades() {
    }

    public AncientBlades(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    public void stealFromHand(int stealerId, int stolenId, int cardId, String cardName) {
        HSServer.getInstance().getPlayer(stolenId).getFactory().removeFromHand(cardId);
        HSServer.getInstance().getPlayer(stealerId).getFactory().makeAndPutDeck(ServerData.getCardByName(cardName));
    }

    public void stealFromDeck(int stealerId, int stolenId, int cardId, String cardName) {
        HSServer.getInstance().getPlayer(stolenId).getFactory().removeFromDeck(cardId);
        HSServer.getInstance().getPlayer(stealerId).getFactory().makeAndPutDeck(ServerData.getCardByName(cardName));
    }

    private void doAbility() {
        HSServer.getInstance().getPlayer(getPlayerId()).reduceMana(getManaCost());

        if (isUpgraded()) {
            Card handCard = HSServer.getInstance().getPlayer(enemyPlayerId).getFactory().getRandomCardFromHand();

            if (handCard != null) {
                stealFromHand(playerId, enemyPlayerId, handCard.getCardGameId(), handCard.getName());
                if (handCard.getHeroType() != HeroType.ALL && handCard.getHeroType() != HSServer.getInstance().getPlayer(playerId).getHero().getType())
                    handCard.setManaCost(Math.max(0, handCard.getManaCost() - 2));
            }

            Card deckCard = HSServer.getInstance().getPlayer(enemyPlayerId).getFactory().getRandomCardFromCurrentDeck();

            if (deckCard != null) {
                stealFromDeck(playerId, enemyPlayerId, deckCard.getCardGameId(), deckCard.getName());
                if (deckCard.getHeroType() != HeroType.ALL && deckCard.getHeroType() != HSServer.getInstance().getPlayer(playerId).getHero().getType())
                    deckCard.setManaCost(Math.max(0, deckCard.getManaCost() - 2));
            }
        } else {
            int myId = getPlayerId();

            Card deckCard = HSServer.getInstance().getPlayer(enemyPlayerId).getFactory().getRandomCardFromCurrentDeck();

            if (deckCard != null) {
                stealFromDeck(myId, enemyPlayerId, deckCard.getCardGameId(), deckCard.getName());
                if (deckCard.getHeroType() != HeroType.ALL && deckCard.getHeroType() != HSServer.getInstance().getPlayer(playerId).getHero().getType())
                    deckCard.setManaCost(Math.max(0, deckCard.getManaCost() - 2));

            }
        }

        HSServer.getInstance().updateGameRequest(playerId);
    }

    @Override
    public CursorType lookingForCursorType() {
        return CursorType.SEARCH;
    }

    @Override
    public void found(Object object) {
        if(!canAttack)
            return;
        if (object instanceof Hero && ((Hero) object).getPlayerId() == this.getPlayerId()) {
            doAbility();
            numberOfAttack--;
        }
    }

    @Override
    public boolean isUpgraded() {
        return upgraded;
    }

    @Override
    public void setUpgraded(boolean upgraded) {
        this.upgraded = upgraded;
    }

    @Override
    public void updateUpgraded() {
       this.upgraded = HSServer.getInstance().getPlayer(getPlayerId()).haveWeapon();
    }
}
