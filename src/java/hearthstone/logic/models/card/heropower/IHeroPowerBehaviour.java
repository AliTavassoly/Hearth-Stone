package hearthstone.logic.models.card.heropower;

import hearthstone.util.CursorType;

public interface IHeroPowerBehaviour {
    void startTurnBehave();
    boolean canAttack();
    CursorType lookingForCursorType();
}
