package hearthstone.models.card.minion.interfaces;

import hearthstone.models.card.Card;
import hearthstone.util.HearthStoneException;

public interface WaitDrawingCard {
    boolean waitDrawingCard(Card card) throws HearthStoneException;
}
