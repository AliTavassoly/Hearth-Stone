package hearthstone.models.card.heropower;

import hearthstone.models.card.spell.spells.BookOfSpecters;
import hearthstone.util.CursorType;

public interface HeroPowerBehaviour {
    void startTurnBehave();
    boolean canAttack();
    boolean isCanAttack();
    CursorType lookingForCursorType();
}
