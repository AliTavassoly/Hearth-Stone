package hearthstone.gamestuff;

import hearthstone.HearthStone;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.util.HearthStoneException;

import java.awt.*;
import java.util.Scanner;

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

    public static void addToDeck(String cardName, int cnt) throws Exception {
        Card currentCard = null;
        for(Card card : HearthStone.currentAccount.getCurrentCollection().getCards()){
            if(card.getName().equals(cardName)){
                currentCard = card;
            }
        }
        if(currentCard == null){
            throw new HearthStoneException("this card does not exist in your collection or does not exist at all !");
        }
        HearthStone.currentAccount.getCurrentDeck().add(currentCard, cnt);
    }

    public static void removeFromDeck(String cardName, int cnt) throws Exception {
        Card currentCard = null;
        for(Card card : HearthStone.currentAccount.getCurrentCollection().getCards()){
            if(card.getName().equals(cardName)){
                currentCard = card;
            }
        }
        if(currentCard == null){
            throw new HearthStoneException("this card does not exist in your collection or does not exist at all !");
        }
        HearthStone.currentAccount.getCurrentDeck().remove(currentCard, cnt);
    }

    public static void cli() {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                String command, cardName, heroName, sure;
                int number;

                System.out.print("@" + HearthStone.currentAccount.getUsername() + " "+ " account @collection manager, please enter your command : ");
                command = scanner.next();

                switch (command){
                    case "help" :
                        System.out.println("select hero : for choose a hero !");
                        System.out.println("all heroes : show all heroes in your account !");
                        System.out.println("my hero : show your hero !");
                        System.out.println("my cards : show all your cards according to this hero!");
                        System.out.println("my deck : show cards in your deck(hand) !");
                        System.out.println("can add : show cards you can add to your deck !");
                        System.out.println("add : you can add cards to your deck !");
                        System.out.println("remove : you can remove cards from you deck !");
                        System.out.println("exit : exit from collection manager !");
                        System.out.println("EXIT : exit from hearth stone !");
                    case "select hero" :
                        System.out.print("select your hero name : ");
                        heroName = scanner.next();
                        selectHero(heroName);
                    case "all heroes" :
                        showAllHeroes();
                    case "my hero" :
                        showMyHero();
                    case "my cards" :
                        showCollectionCards();
                    case "my deck" :
                        showDeckCards();
                    case "can add" :
                        showAddableCards();
                    case "add" :
                        System.out.print("please enter card name : ");
                        cardName = scanner.next();
                        System.out.print("please enter number of it you want to add : ");
                        number = scanner.nextInt();
                        addToDeck(cardName, number);
                    case "remove" :
                        System.out.print("please enter card name : ");
                        cardName = scanner.next();
                        System.out.print("please enter number of it you want to remove : ");
                        number = scanner.nextInt();
                        removeFromDeck(cardName, number);
                    case "exit" :
                        HearthStone.cli();
                    case "EXIT" :
                        System.out.print("are you sure you want to EXIT ?!(y/n) ");
                        sure = scanner.next();
                        if(sure.equals("y")) {
                            HearthStone.logout();
                            System.exit(0);
                        }
                    default :
                        System.out.println("please enter correct command !");
                }
            } catch (HearthStoneException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred !");
            }
        }
    }
}
