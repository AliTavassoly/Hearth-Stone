package hearthstone.models.card;

import hearthstone.util.HearthStoneException;

public interface CardBehaviour {
    public void found(Object object) throws HearthStoneException;
    boolean drawCard(Card card) throws HearthStoneException;
}
