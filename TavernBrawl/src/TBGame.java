import hearthstone.models.player.Player;
import hearthstone.server.logic.Game;
import hearthstone.server.model.GameType;

public class TBGame extends Game {
    public TBGame() {}

    public TBGame(Player player0, Player player1, int id0, int id1, GameType gameType){
        super(player0, player1, id0, id1, gameType);
    }
}
