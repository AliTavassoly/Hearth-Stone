package hearthstone.logic.interfaces;

import hearthstone.logic.models.card.Card;
import hearthstone.util.HearthStoneException;

public interface WaitDrawingCard {
    boolean waitDrawingCard(Card card) throws HearthStoneException;
}
