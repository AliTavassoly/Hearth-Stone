package hearthstone.logic.models.player;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.IHero;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;
import hearthstone.util.timer.HSDelayTimerTask;
import hearthstone.util.timer.IHSDelayTask;

import java.util.ArrayList;

public class AIPlayer extends Player {
    public AIPlayer(IHero hero, Deck deck, String username) {
        super(hero, deck, username);
    }

    @Override
    public void startTurn() throws Exception {
        super.startTurn();

        playWeapon();

        playMinions();

        new HSDelayTimerTask(4000, new IHSDelayTask() {
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

                Mapper.getInstance().endTurn();
            }
        }).start();
    }

    public void choosePassive(ArrayList<Integer> passives){
        int ind = Rand.getInstance().getRandomNumber(passives.size());
        Mapper.getInstance().setPassive(getPlayerId(), DataTransform.getInstance().getBasePassive(ind));
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
        ArrayList<Card> hand = new ArrayList<>(DataTransform.getInstance().getHand(getPlayerId()));
        for(Card card: hand){
            if(card.getCardType() == CardType.WEAPONCARD && DataTransform.getInstance().getWeapon(getPlayerId()) == null) {
                try {
                    playCard(card);
                } catch (HearthStoneException ignore) {
                }
            }
        }
    }

    private void playMinions(){
        ArrayList<Card> hand = new ArrayList<>(DataTransform.getInstance().getHand(getPlayerId()));
        for(Card card: hand){
            if(card.getCardType() == CardType.MINIONCARD) {
                try {
                    playCard(card);
                } catch (HearthStoneException ignore) {
                }
            }
        }
    }

    private void attackMinionsToHero(){
        ArrayList<Card> land = new ArrayList<>(DataTransform.getInstance().getLand(getPlayerId()));
        for (Card myCard : land) {
            MinionCard myMinion = (MinionCard) myCard;
            if(myMinion.canAttack() && myMinion.getHealth() > 0){
                try {
                    myMinion.found(DataTransform.getInstance().getHero(DataTransform.getInstance().getEnemyId(getPlayerId())));
                } catch (HearthStoneException ignore) {}
            }
        }
    }

    private void attackMinionToMinion(){
        ArrayList<Card> myLand = new ArrayList<>(DataTransform.getInstance().getLand(getPlayerId()));
        ArrayList<Card> enemyLand = new ArrayList<>(DataTransform.getInstance().getLand(DataTransform.getInstance().getEnemyId(getPlayerId())));

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
        WeaponCard myWeapon = DataTransform.getInstance().getWeapon(getPlayerId());
        if(myWeapon == null || !myWeapon.canAttack())
            return;
        ArrayList<Card> enemyLand = new ArrayList<>(DataTransform.getInstance().getLand(DataTransform.getInstance().getEnemyId(getPlayerId())));
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
        WeaponCard myWeapon = DataTransform.getInstance().getWeapon(getPlayerId());
        if(myWeapon == null || !myWeapon.canAttack())
            return;

        try {
            myWeapon.found(DataTransform.getInstance().getHero(DataTransform.getInstance().getEnemyId(getPlayerId())));
        } catch (HearthStoneException ignore) {}
    }
}
