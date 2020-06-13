package hearthstone;

import hearthstone.logic.gamestuff.Game;
import hearthstone.models.card.Card;
import hearthstone.models.player.Player;

public class GuiLogicMapper {
    private static GuiLogicMapper instance;

    private GuiLogicMapper(){
    }

    public static GuiLogicMapper getInstance(){
        if(instance == null){
            return instance = new GuiLogicMapper();
        } else {
            return instance;
        }
    }

    public void endTurn(Game game){
        game.endTurn();
    }

    public void playCard(Player player, Card card) throws Exception{
        player.playCard(card);
    }
}
