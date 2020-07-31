package hearthstone.logic.behaviours;

import hearthstone.models.card.Card;
import hearthstone.util.HearthStoneException;

public interface WaitSummonCard {
    boolean waitSummonCard(Card card) throws HearthStoneException;
}
