package hearthstone.gamestuff;

import hearthstone.HearthStone;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;

import java.util.Properties;

public class CollectionManager {

    public static void showAllHeroes() {
        for (Hero hero : HearthStone.currentAccount.getHeroes()) {
            System.out.println(hero.getName());
        }
    }

    public static void showMyHero() {
        System.out.println(HearthStone.currentAccount.getCurrentHero().getName());
    }

    //public static void selectHero(){ }

    public static void showCollectionCards() {
        if (HearthStone.currentAccount.getCurrentCollection() == null) {
            System.out.println("No cards is in your hand");
        } else {
            System.out.println(HearthStone.currentAccount.getCurrentCollection());
        }
    }

    public static void showDeckCards() {
        if (HearthStone.currentAccount.getCurrentDeck() == null) {
            System.out.println("No cards is in your hand");
        } else {
            System.out.println(HearthStone.currentAccount.getCurrentDeck());
        }
    }

    public static void showAddableCards() {
        for (Card card : HearthStone.currentAccount.getCurrentCollection().getCards()) {
            if (HearthStone.currentAccount.getCurrentDeck().getNumberOfCard().get(card.getId()) < HearthStone.currentAccount.getCurrentCollection().getNumberOfCard().get(card.getId())) {
                System.out.println(card.getName());
            }
        }
    }

    public static void addToDeck(Card card, int cnt) {
        if(HearthStone.currentAccount.getCurrentDeck().canAdd(card, cnt)){
            HearthStone.currentAccount.getCurrentDeck().add(card, cnt);
        }
        else{
            //System.out.println("Can not add this card !");
        }
    }

    public static void removeFromDeck(Card card, int cnt) {
        if(HearthStone.currentAccount.getCurrentDeck().canRemove(card, cnt)){
            HearthStone.currentAccount.getCurrentDeck().remove(card, cnt);
        }
        else{
            //System.out.println("Can not remove this card !");
        }
    }
}
