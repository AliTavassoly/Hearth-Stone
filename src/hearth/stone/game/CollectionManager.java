package hearth.stone.game;

import hearth.stone.credentials.User;
import hearth.stone.game.cards.Card;
import hearth.stone.game.heroes.Hero;

import static hearth.stone.Properties.currentUser;

public class CollectionManager {

    public static void showAllHeroes() {
        for (Hero hero : currentUser.getHeroes()) {
            System.out.println(hero.getName());
        }
    }

    public static void showMyHero() {
        System.out.println(currentUser.getHero().getName());
    }

    //public static void selectHero(){ }

    public static void showCollectionCards() {
        if (currentUser.getCurrentCollection() == null) {
            System.out.println("No cards is in your hand");
        } else {
            System.out.println(currentUser.getCurrentCollection());
        }
    }

    public static void showDeckCards() {
        if (currentUser.getCurrentDeck() == null) {
            System.out.println("No cards is in your hand");
        } else {
            System.out.println(currentUser.getCurrentDeck());
        }
    }

    public static void showAddableCards() {
        for (Card card : currentUser.getCurrentCollection().getCards()) {
            if (currentUser.getCurrentDeck().getNumberOfCard().get(card.getId()) < currentUser.getCurrentCollection().getNumberOfCard().get(card.getId())) {
                System.out.println(card.getName());
            }
        }
    }

    public static void addToDeck() {

    }

    public static void removeFromDeck() {
    }
}
