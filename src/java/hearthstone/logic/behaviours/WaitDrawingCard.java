package hearthstone.logic.behaviours;

import hearthstone.models.card.Card;
import hearthstone.util.HearthStoneException;

public interface WaitDrawingCard {
    boolean waitDrawingCard(Card card) throws HearthStoneException;
}
