package hearthstone.util.mappers;

import hearthstone.logic.gamestuff.Game;

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
}
