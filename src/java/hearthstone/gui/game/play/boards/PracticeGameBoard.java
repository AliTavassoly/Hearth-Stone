package hearthstone.gui.game.play.boards;

import hearthstone.logic.gamestuff.Game;
import hearthstone.models.player.Player;

public class PracticeGameBoard extends GameBoard {

    public PracticeGameBoard(Player myPlayer, Player enemyPlayer, Game game) {
        super(myPlayer, enemyPlayer, game);
    }
}