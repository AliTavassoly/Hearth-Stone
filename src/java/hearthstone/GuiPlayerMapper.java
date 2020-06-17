package hearthstone;

import hearthstone.models.Deck;
import hearthstone.models.Passive;
import hearthstone.models.card.Card;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.weapons.WeaponCard;
import hearthstone.models.player.Player;

import java.util.ArrayList;

public class GuiPlayerMapper {
    private static GuiPlayerMapper instance;

    private GuiPlayerMapper(){
    }

    public static GuiPlayerMapper getInstance(){
        if(instance == null){
            return instance = new GuiPlayerMapper();
        } else {
            return instance;
        }
    }

    public void playCard(Player player, Card card) throws Exception{
        player.playCard(card);
    }

    public ArrayList<Card> getLand(Player player){
        return player.getLand();
    }

    public ArrayList<Card> getHand(Player player){
        return player.getHand();
    }

    public Deck getDeck(Player player){
        return player.getDeck();
    }

    public int getMana(Player player){
        return player.getMana();
    }

    public int getTurnNumber(Player player){
        return player.getTurnNumber();
    }

    public Passive getPassive(Player player){
        return player.getPassive();
    }

    public void doPassive(Player player){
        player.doPassives();
    }

    public void setPassive(Player player, Passive passive){
        player.setPassive(passive);
    }

    public HeroPowerCard getHeroPower(Player player){
        return player.getHeroPower();
    }

    public WeaponCard getWeapon(Player player){
        return player.getWeapon();
    }
}
