package hearthstone.models.card.heropower.heropowers;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.hero.Hero;
import hearthstone.logic.behaviours.*;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;

public class AncientBlades extends HeroPowerCard implements Upgradeable {
    public AncientBlades() {
    }

    public AncientBlades(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    private void doAbility() {
        Mapper.getInstance().reduceMana(getPlayerId(), getManaCost());

        if (isUpgraded()) {
            int myId = getPlayerId();
            int enemyId = DataTransform.getInstance().getEnemyId(myId);

            Card handCard = DataTransform.getInstance().getRandomCardFromHand(enemyId);
            if (handCard != null) {
                Mapper.getInstance().stealFromHand(myId, enemyId, handCard);
                if (handCard.getHeroType() != HeroType.ALL && handCard.getHeroType() != DataTransform.getInstance().getHero(getPlayerId()).getType())
                    Mapper.getInstance().setCardMana(handCard, Math.max(0, handCard.getManaCost() - 2));
            }

            Card deckCard = DataTransform.getInstance().getRandomCardFromCurrentDeck(enemyId);
            if (deckCard != null) {
                Mapper.getInstance().stealFromDeck(myId, enemyId, deckCard);
                if (deckCard.getHeroType() != HeroType.ALL && deckCard.getHeroType() != DataTransform.getInstance().getHero(getPlayerId()).getType())
                    Mapper.getInstance().setCardMana(deckCard, Math.max(0, deckCard.getManaCost() - 2));
            }
        } else {
            int myId = getPlayerId();
            int enemyId = DataTransform.getInstance().getEnemyId(myId);

            Card deckCard = DataTransform.getInstance().getRandomCardFromCurrentDeck(enemyId);
            if (deckCard != null) {
                Mapper.getInstance().stealFromDeck(myId, enemyId, deckCard);
                if (deckCard.getHeroType() != HeroType.ALL && deckCard.getHeroType() != DataTransform.getInstance().getHero(getPlayerId()).getType())
                    Mapper.getInstance().setCardMana(deckCard, Math.max(0, deckCard.getManaCost() - 2));

            }
        }
        log();
        Mapper.getInstance().updateBoard();
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
        return DataTransform.getInstance().haveWeapon(getPlayerId());
    }
}
