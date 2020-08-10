package hearthstone.models.player;

import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.Hero;
import hearthstone.server.data.ServerData;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;
import hearthstone.util.timer.HSDelayTask;
import hearthstone.util.timer.HSDelayTimerTask;

import java.util.ArrayList;

public class AIPlayer extends Player {
    public AIPlayer(){}

    public AIPlayer(Hero hero, Deck deck, String username) {
        super(hero, deck, username);
    }

    @Override
    public void startTurn() {
        super.startTurn();

        playWeapon();

        playMinions();

        new HSDelayTimerTask(4000, new HSDelayTask() {
            @Override
            public void delayAction() {

                if(Rand.getInstance().getProbability(1, 2)){
                    attackToHero();
                    attackToMinions();
                    attackToHero();
                    attackToMinions();
                } else {
                    attackToMinions();
                    attackToHero();
                    attackToMinions();
                    attackToHero();
                }

                game.endTurn();
            }
        }).start();
    }

    public void choosePassive(ArrayList<Integer> passives){
        int ind = Rand.getInstance().getRandomNumber(passives.size());
        this.passive = ServerData.basePassives.get(ind);
    }

    private void attackToMinions(){
        attackMinionToMinion();
        attackWeaponToMinion();
    }

    private void attackToHero(){
        attackMinionsToHero();
        attackWeaponToHero();
    }

    private void playWeapon(){
        ArrayList<Card> hand = new ArrayList<>(this.hand);
        for(Card card: hand){
            if(card.getCardType() == CardType.WEAPON_CARD && weapon == null) {
                try {
                    playCard(card);
                } catch (HearthStoneException ignore) {
                }
            }
        }
    }

    private void playMinions(){
        ArrayList<Card> hand = new ArrayList<>(this.hand);
        for(Card card: hand){
            if(card.getCardType() == CardType.MINION_CARD) {
                try {
                    playCard(card);
                } catch (HearthStoneException ignore) {
                }
            }
        }
    }

    private void attackMinionsToHero(){
        ArrayList<Card> land = new ArrayList<>(this.land);
        for (Card myCard : land) {
            MinionCard myMinion = (MinionCard) myCard;
            if(myMinion.canAttack() && myMinion.getHealth() > 0){
                try {
                    myMinion.found(game.getPlayerById(enemyPlayerId).getHero());
                } catch (HearthStoneException ignore) {}
            }
        }
    }

    private void attackMinionToMinion(){
        ArrayList<Card> myLand = new ArrayList<>(this.land);
        ArrayList<Card> enemyLand = new ArrayList<>(game.getPlayerById(enemyPlayerId).getLand());

        for(int i = 0; i < 10; i++) {
            for (Card myCard : myLand) {
                MinionCard myMinion = (MinionCard) myCard;
                for (Card enemyCard : enemyLand) {
                    MinionCard enemyMinion = (MinionCard) enemyCard;
                    if (myMinion.canAttack() && myMinion.getHealth() > 0 && enemyMinion.getHealth() > 0) {
                        try {
                            myMinion.found(enemyMinion);
                        } catch (HearthStoneException ignore) {
                        }
                    }
                }
            }
        }
    }

    private void attackWeaponToMinion(){
        WeaponCard myWeapon = weapon;
        if(myWeapon == null || !myWeapon.canAttack())
            return;
        ArrayList<Card> enemyLand = new ArrayList<>(game.getPlayerById(enemyPlayerId).getLand());
        for (Card enemyCard: enemyLand){
            MinionCard enemyMinion = (MinionCard)enemyCard;
            if(enemyMinion.getHealth() > 0 && myWeapon.canAttack()){
                try {
                    myWeapon.found(enemyMinion);
                } catch (HearthStoneException ignore) {}
            }
        }
    }

    private void attackWeaponToHero(){
        WeaponCard myWeapon = weapon;
        if(myWeapon == null || !myWeapon.canAttack())
            return;

        try {
            myWeapon.found(game.getPlayerById(enemyPlayerId).getHero());
        } catch (HearthStoneException ignore) {}
    }
}
