package hearthstone.logic.models.card.heropower.heropowers;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.card.interfaces.Upgradeable;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.CursorType;

public class AncientBlades extends HeroPowerCard implements Upgradeable {
    public AncientBlades() {
    }

    public AncientBlades(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    private void doAbility() {
        if (isUpgraded()) {
            int myId = getPlayerId();
            int enemyId = DataTransform.getInstance().getEnemyId(myId);

            Card handCard = DataTransform.getInstance().getRandomCardFromHand(enemyId);
            if (handCard != null)
                Mapper.getInstance().stealFromHand(myId, enemyId, handCard);

            Card deckCard = DataTransform.getInstance().getRandomCardFromCurrentDeck(enemyId);
            if(deckCard != null)
                Mapper.getInstance().stealFromDeck(myId, enemyId, deckCard);
        } else {
            int myId = getPlayerId();
            int enemyId = DataTransform.getInstance().getEnemyId(myId);

            Card deckCard = DataTransform.getInstance().getRandomCardFromCurrentDeck(enemyId);
            if(deckCard != null)
                Mapper.getInstance().stealFromDeck(myId, enemyId, deckCard);
        }
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
