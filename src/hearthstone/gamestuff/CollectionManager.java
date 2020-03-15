package hearthstone.gamestuff;

import hearthstone.HearthStone;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.util.HearthStoneException;

public class CollectionManager {

    public static void showAllHeroes() {
        for (Hero hero : HearthStone.currentAccount.getHeroes()) {
            System.out.println(hero.getName());
        }
    }

    public static void showMyHero() {
        System.out.println(HearthStone.currentAccount.getCurrentHero().getName());
    }

    public static void selectHero(String heroName) throws Exception {
        for (Hero hero : HearthStone.currentAccount.getHeroes()) {
            if (hero.getName().equals(heroName)) {
                HearthStone.currentAccount.setCurrentHero(hero);
                return;
            }
        }
        throw new HearthStoneException("This hero does not exist or you can not choose it !");
    }

    public static void showCollectionCards() {
        if (HearthStone.currentAccount.getCurrentCollection() == null || HearthStone.currentAccount.getCurrentCollection().getCards().size() == 0) {
            System.out.println("No cards is in your Collection !");
        } else {
            for (Card card : HearthStone.currentAccount.getCurrentCollection().getCards()) {
                System.out.println(card.getName() + " " + HearthStone.currentAccount.getCurrentCollection().getNumberOfCard().get(card.getId()));
            }
        }
    }

    public static void showDeckCards() {
        if (HearthStone.currentAccount.getCurrentDeck() == null || HearthStone.currentAccount.getCurrentDeck().getCards().size() == 0) {
            System.out.println("No cards is in your Deck !");
        } else {
            for (Card card : HearthStone.currentAccount.getCurrentDeck().getCards()) {
                System.out.println(card.getName() + " " + HearthStone.currentAccount.getCurrentDeck().getNumberOfCard().get(card.getId()));
            }
        }
    }

    public static void showAddableCards() {
        for (Card card : HearthStone.currentAccount.getCurrentCollection().getCards()) {
            if (HearthStone.currentAccount.getCurrentDeck().getNumberOfCard().get(card.getId()) < HearthStone.currentAccount.getCurrentCollection().getNumberOfCard().get(card.getId())) {
                System.out.println(card.getName());
            }
        }
    }

    public static void addToDeck(Card card, int cnt) throws Exception {
        HearthStone.currentAccount.getCurrentDeck().add(card, cnt);
    }

    public static void removeFromDeck(Card card, int cnt) throws Exception {
        HearthStone.currentAccount.getCurrentDeck().remove(card, cnt);
    }

    public static void cli() {
        while (true) {
            try {

            } catch (HearthStoneException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred !");
            }
        }
    }
}
