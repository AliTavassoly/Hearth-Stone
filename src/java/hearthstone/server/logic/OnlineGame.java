package hearthstone.server.logic;

import hearthstone.models.player.Player;
import hearthstone.server.model.GameType;

public class OnlineGame extends Game{
    public OnlineGame(Player player0, Player player1, int id0, int id1, GameType gameType) {
        super(player0, player1, id0, id1, gameType);
    }
}
