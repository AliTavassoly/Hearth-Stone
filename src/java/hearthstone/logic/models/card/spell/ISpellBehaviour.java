package hearthstone.logic.models.card.spell;

import hearthstone.util.CursorType;

public interface ISpellBehaviour {
    void doAbility();
    CursorType getCursorType();
}
