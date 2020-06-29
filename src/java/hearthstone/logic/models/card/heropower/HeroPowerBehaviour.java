package hearthstone.logic.models.card.heropower;

import hearthstone.util.CursorType;

public interface HeroPowerBehaviour {
    void startTurnBehave();
    boolean canAttackThisTurn();
    boolean pressed();
    CursorType lookingForCursorType();
}
