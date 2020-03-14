package hearthstone.game;

import hearthstone.game.cards.Card;
import hearthstone.game.heroes.Hero;
import hearthstone.Properties;

public class CollectionManager {

    public static void showAllHeroes() {
        for (Hero hero : Properties.currentUser.getHeroes()) {
            System.out.println(hero.getName());
        }
    }

    public static void showMyHero() {
        System.out.println(Properties.currentUser.getHero().getName());
    }

    //public static void selectHero(){ }

    public static void showCollectionCards() {
        if (Properties.currentUser.getCurrentCollection() == null) {
            System.out.println("No cards is in your hand");
        } else {
            System.out.println(Properties.currentUser.getCurrentCollection());
        }
    }

    public static void showDeckCards() {
        if (Properties.currentUser.getCurrentDeck() == null) {
            System.out.println("No cards is in your hand");
        } else {
            System.out.println(Properties.currentUser.getCurrentDeck());
        }
    }

    public static void showAddableCards() {
        for (Card card : Properties.currentUser.getCurrentCollection().getCards()) {
            if (Properties.currentUser.getCurrentDeck().getNumberOfCard().get(card.getId()) < Properties.currentUser.getCurrentCollection().getNumberOfCard().get(card.getId())) {
                System.out.println(card.getName());
            }
        }
    }

    public static void addToDeck(Card card, int cnt) {
        if(Properties.currentUser.getCurrentDeck().canAdd(card, cnt)){
            Properties.currentUser.getCurrentDeck().add(card, cnt);
        }
        else{
            //System.out.println("Can not add this card !");
        }
    }

    public static void removeFromDeck(Card card, int cnt) {
        if(Properties.currentUser.getCurrentDeck().canRemove(card, cnt)){
            Properties.currentUser.getCurrentDeck().remove(card, cnt);
        }
        else{
            //System.out.println("Can not remove this card !");
        }
    }
}
