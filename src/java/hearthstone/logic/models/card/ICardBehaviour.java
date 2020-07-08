package hearthstone.logic.models.card;

import hearthstone.util.HearthStoneException;

public interface ICardBehaviour {
    void found(Object object) throws HearthStoneException;
}
