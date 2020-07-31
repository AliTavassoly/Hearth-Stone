package hearthstone.models.card;

import hearthstone.util.HearthStoneException;

public interface CardBehaviour {
    void found(Object object) throws HearthStoneException;
}
