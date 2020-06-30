package hearthstone.logic.models.card.spell;

import hearthstone.util.CursorType;

public interface SpellBehaviour {
    void doAbility();
    CursorType getCursorType();
}
