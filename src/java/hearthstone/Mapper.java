package hearthstone;

import hearthstone.models.card.Card;
import hearthstone.models.player.Player;
import hearthstone.util.HearthStoneException;

public class Mapper {
    private static Mapper instance;

    private Mapper(){
    }

    public static Mapper getInstance(){
        if(instance == null){
            return instance = new Mapper();
        } else {
            return instance;
        }
    }

    public void playCard(Player player, Card card) throws Exception{
        player.playCard(card);
    }

    public void doPassive(Player player){
        player.doPassives();
    }

    public void updatePlayer(Player player){
        player.updatePlayer();
    }

    public void endTurn(){
        HearthStone.currentGame.endTurn();
    }

    public void startGame(){HearthStone.currentGame.startGame();}

    public void foundObjectForObject(Object waited, Object founded) throws HearthStoneException {
        if(waited instanceof Card){
            Card card = (Card)waited;
            card.found(founded);
        }
    }
}
