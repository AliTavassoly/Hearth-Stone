package hearthstone.models.card.heropower.heropowers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@JsonIgnoreProperties(value = {"upgraded"})
@Entity
public class AncientBlades extends HeroPowerCard implements Upgradeable {
    public AncientBlades() {
    }

    public AncientBlades(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    public void stealFromHand(int stealerId, int stolenId, int cardId) {
        HSServer.getInstance().getPlayer(stolenId).getFactory().removeFromHand(cardId);
        HSServer.getInstance().getPlayer(stealerId).getFactory().makeAndPutDeck(ServerData.getCardById(cardId));
    }

    public void stealFromDeck(int stealerId, int stolenId, int cardId) {
        HSServer.getInstance().getPlayer(stolenId).getFactory().removeFromDeck(cardId);
        HSServer.getInstance().getPlayer(stealerId).getFactory().makeAndPutDeck(ServerData.getCardById(cardId));
    }

    private void doAbility() {
        //Mapper.reduceMana(getPlayerId(), getManaCost());
        HSServer.getInstance().getPlayer(getPlayerId()).reduceMana(getManaCost());

        if (isUpgraded()) {
            int myId = getPlayerId();

            //Card handCard = DataTransform.getRandomCardFromHand(enemyId);
            Card handCard = HSServer.getInstance().getPlayer(enemyPlayerId).getFactory().getRandomCardFromHand();

            if (handCard != null) {
                stealFromHand(myId, enemyPlayerId, handCard.getCardGameId());
                if (handCard.getHeroType() != HeroType.ALL && handCard.getHeroType() != HSServer.getInstance().getPlayer(playerId).getHero().getType())
                    //Mapper.setCardMana(handCard, Math.max(0, handCard.getManaCost() - 2));
                    handCard.setManaCost(Math.max(0, handCard.getManaCost() - 2));
            }

            //Card deckCard = DataTransform.getRandomCardFromCurrentDeck(enemyId);
            Card deckCard = HSServer.getInstance().getPlayer(enemyPlayerId).getFactory().getRandomCardFromCurrentDeck();

            if (deckCard != null) {
                stealFromDeck(myId, enemyPlayerId, deckCard.getCardGameId());
                if (deckCard.getHeroType() != HeroType.ALL && deckCard.getHeroType() != HSServer.getInstance().getPlayer(playerId).getHero().getType())
                    //Mapper.setCardMana(deckCard, Math.max(0, deckCard.getManaCost() - 2));
                    deckCard.setManaCost(Math.max(0, deckCard.getManaCost() - 2));
            }
        } else {
            int myId = getPlayerId();

            //Card deckCard = DataTransform.getRandomCardFromCurrentDeck(enemyId);
            Card deckCard = HSServer.getInstance().getPlayer(enemyPlayerId).getFactory().getRandomCardFromCurrentDeck();

            if (deckCard != null) {
                stealFromDeck(myId, enemyPlayerId, deckCard.getCardGameId());
                if (deckCard.getHeroType() != HeroType.ALL && deckCard.getHeroType() != HSServer.getInstance().getPlayer(playerId).getHero().getType())
                    //Mapper.setCardMana(deckCard, Math.max(0, deckCard.getManaCost() - 2));
                    deckCard.setManaCost(Math.max(0, deckCard.getManaCost() - 2));

            }
        }
        log();
        // Mapper.updateBoard();
        HSServer.getInstance().updateGameRequest(playerId);
    }

    @Override
    public CursorType lookingForCursorType() {
        return CursorType.SEARCH;
    }

    @Override
    public void found(Object object) {
        if (object instanceof Hero && ((Hero) object).getPlayerId() == this.getPlayerId()) {
            doAbility();
            numberOfAttack--;
        }
    }

    @Override
    public boolean isUpgraded() {
        return HSServer.getInstance().getPlayer(getPlayerId()).haveWeapon();
    }
}
