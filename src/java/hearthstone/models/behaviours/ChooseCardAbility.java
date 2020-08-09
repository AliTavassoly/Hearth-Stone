package hearthstone.models.behaviours;

import hearthstone.models.card.Card;

public interface ChooseCardAbility {
    void doAfterChoosingCard(Card card);
}
