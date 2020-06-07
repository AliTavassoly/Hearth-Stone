package hearthstone.gui.game.play.boards;

import hearthstone.logic.gamestuff.Game;
import hearthstone.models.player.Player;

public class GameBoardSelfPlay extends GameBoard {

    public GameBoardSelfPlay(Player myPlayer, Player enemyPlayer, Game game) {
        super(myPlayer, enemyPlayer, game);
    }
}