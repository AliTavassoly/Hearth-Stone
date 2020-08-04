package hearthstone.models.card.heropower.heropowers;

import hearthstone.DataTransform;
import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.data.Data;
import hearthstone.models.behaviours.Upgradeable;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;

import javax.persistence.Entity;

@Entity
public class AncientBlades extends HeroPowerCard implements Upgradeable {
    public AncientBlades() {
    }

    public AncientBlades(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    public void stealFromHand(int stealerId, int stolenId, int cardId) {
        DataTransform.getPlayer(stolenId).getFactory().removeFromHand(cardId);
        DataTransform.getPlayer(stealerId).getFactory().makeAndPutDeck(HearthStone.getCardById(cardId));
    }

    public void stealFromDeck(int stealerId, int stolenId, int cardId) {
        DataTransform.getPlayer(stolenId).getFactory().removeFromDeck(cardId);
        DataTransform.getPlayer(stealerId).getFactory().makeAndPutDeck(HearthStone.getCardById(cardId));
    }

    private void doAbility() {
        //Mapper.reduceMana(getPlayerId(), getManaCost());
        DataTransform.getPlayer(getPlayerId()).reduceMana(getManaCost());

        if (isUpgraded()) {
            int myId = getPlayerId();
            int enemyId = DataTransform.getEnemyId(myId);

            Card handCard = DataTransform.getRandomCardFromHand(enemyId);
            if (handCard != null) {
                stealFromHand(myId, enemyId, handCard.getCardGameId());
                if (handCard.getHeroType() != HeroType.ALL && handCard.getHeroType() != DataTransform.getHero(getPlayerId()).getType())
                    //Mapper.setCardMana(handCard, Math.max(0, handCard.getManaCost() - 2));
                    handCard.setManaCost(Math.max(0, handCard.getManaCost() - 2));
            }

            Card deckCard = DataTransform.getRandomCardFromCurrentDeck(enemyId);
            if (deckCard != null) {
                stealFromDeck(myId, enemyId, deckCard.getCardGameId());
                if (deckCard.getHeroType() != HeroType.ALL && deckCard.getHeroType() != DataTransform.getHero(getPlayerId()).getType())
                    //Mapper.setCardMana(deckCard, Math.max(0, deckCard.getManaCost() - 2));
                    deckCard.setManaCost(Math.max(0, deckCard.getManaCost() - 2));
            }
        } else {
            int myId = getPlayerId();
            int enemyId = DataTransform.getEnemyId(myId);

            Card deckCard = DataTransform.getRandomCardFromCurrentDeck(enemyId);
            if (deckCard != null) {
                stealFromDeck(myId, enemyId, deckCard.getCardGameId());
                if (deckCard.getHeroType() != HeroType.ALL && deckCard.getHeroType() != DataTransform.getHero(getPlayerId()).getType())
                    //Mapper.setCardMana(deckCard, Math.max(0, deckCard.getManaCost() - 2));
                    deckCard.setManaCost(Math.max(0, deckCard.getManaCost() - 2));

            }
        }
        log();
        Mapper.updateBoard();
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
        return DataTransform.haveWeapon(getPlayerId());
    }
}
