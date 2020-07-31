package hearthstone.models.card.heropower;

import hearthstone.util.CursorType;

public interface HeroPowerBehaviour {
    void startTurnBehave();
    boolean canAttack();
    CursorType lookingForCursorType();
}
