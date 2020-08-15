package hearthstone.models.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.reward.RewardCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;
import hearthstone.server.data.ServerData;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PlayerModel {
    protected Hero hero;

    protected Deck deck;
    protected Passive passive;

    protected String username;

    protected int mana, extraMana;
    protected int turnNumber;

    protected boolean isStarted;
    protected boolean myTurn;

    protected int playerId;
    protected int enemyPlayerId;

    protected ArrayList<Card> hand;
    protected ArrayList<Card> land;

    protected HeroPowerCard heroPower;
    protected RewardCard reward;

    public Hero getHero() {
        return hero;
    }
    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Deck getDeck() {
        return deck;
    }
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Passive getPassive() {
        return passive;
    }
    public void setPassive(Passive passive) {
        this.passive = passive;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getMana() {
        return mana;
    }
    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getExtraMana() {
        return extraMana;
    }
    public void setExtraMana(int extraMana) {
        this.extraMana = extraMana;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public boolean isStarted() {
        return isStarted;
    }
    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isMyTurn() {
        return myTurn;
    }
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getEnemyPlayerId() {
        return enemyPlayerId;
    }
    public void setEnemyPlayerId(int enemyPlayerId) {
        this.enemyPlayerId = enemyPlayerId;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public ArrayList<Card> getLand() {
        return land;
    }
    public void setLand(ArrayList<Card> land) {
        this.land = land;
    }

    public HeroPowerCard getHeroPower() {
        return heroPower;
    }
    public void setHeroPower(HeroPowerCard heroPower) {
        this.heroPower = heroPower;
    }

    public RewardCard getReward() {
        return reward;
    }
    public void setReward(RewardCard reward) {
        this.reward = reward;
    }

    public WeaponCard getWeapon() {
        return weapon;
    }
    public void setWeapon(WeaponCard weapon) {
        this.weapon = weapon;
    }

    protected WeaponCard weapon;

    public static PlayerModel getPlayerModel(Player player){
        PlayerModel playerModel = new PlayerModel();
        for(Field field: PlayerModel.class.getDeclaredFields()){
            try {
                field.setAccessible(true);
                Field fieldInPlayer = Player.class.getDeclaredField(field.getName());
                fieldInPlayer.setAccessible(true);

                field.set(playerModel, fieldInPlayer.get(player));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return playerModel;
    }

    public PlayerModel copy() {
        ObjectMapper mapper = ServerData.getObjectCloneMapper();
        PlayerModel playerModel = null;
        try {
            String json = mapper.writeValueAsString(this);
            playerModel = mapper.readValue(json, PlayerModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerModel;
    }
}
