package hearthstone;

import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.Passive;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.reward.RewardCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.Hero;

import java.util.ArrayList;

public class DataTransform {
    private static DataTransform instance;

    private DataTransform() {
    }

    public static DataTransform getInstance() {
        if (instance == null) {
            return instance = new DataTransform();
        } else {
            return instance;
        }
    }

    public int getWhoseTurn() {
        return HearthStone.currentGame.getWhoseTurn();
    }

    public ArrayList<Card> getLand(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getLand();
    }

    public ArrayList<Card> getHand(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getHand();
    }

    public ArrayList<MinionCard> getNeighbors(int playerId, Card card) {
        return HearthStone.currentGame.getPlayerById(playerId).neighborCards(card);
    }

    public Deck getDeck(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getDeck();
    }

    public int getMana(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getMana();
    }

    public int getTurnNumber(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getTurnNumber();
    }

    public Passive getPassive(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getPassive();
    }

    public void setPassive(int playerId, Passive passive) {
        HearthStone.currentGame.getPlayerById(playerId).setPassive(passive);
    }

    public HeroPowerCard getHeroPower(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getHeroPower();
    }

    public WeaponCard getWeapon(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getWeapon();
    }

    public RewardCard getReward(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getReward();
    }

    public Hero getHero(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getHero();
    }

    public void restartSpentManaOnMinions(int playerId) {
        HearthStone.currentGame.getPlayerById(playerId).setManaSpentOnMinions(0);
    }

    public void restartSpentManaOnSpells(int playerId) {
        HearthStone.currentGame.getPlayerById(playerId).setManaSpentOnSpells(0);
    }

    public int spentManaOnMinions(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getManaSpentOnMinions();
    }

    public int spentManaOnSpells(int playerId) {
        return HearthStone.currentGame.getPlayerById(playerId).getManaSpentOnSpells();
    }

    public int getMaxManaInGame() {
        return GameConfigs.maxManaInGame;
    }

    public int getNumberOfPassive() {
        return GameConfigs.initialPassives;
    }
}
