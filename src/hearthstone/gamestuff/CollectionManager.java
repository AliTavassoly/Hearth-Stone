package hearthstone.gamestuff;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.modules.cards.Card;
import hearthstone.modules.heroes.Hero;
import hearthstone.util.HearthStoneException;

import java.util.Scanner;

public class CollectionManager {

    public static void showAllHeroes() throws Exception{
        /*for (Hero hero : HearthStone.currentAccount.getHeroes()) {
            System.out.println(hero.getName());
        }*/
        DataBase.saveLog("Show Hero", "Nothing");
    }

    public static void showMyHero() {
        System.out.println(HearthStone.currentAccount.getCurrentHero().getName());
    }

    public static void selectHero(String heroName) throws Exception {
        for (Hero hero : HearthStone.currentAccount.getHeroes()) {
            if (hero.getName().equals(heroName)) {
                HearthStone.currentAccount.setCurrentHero(hero);
                DataBase.saveLog("Select Hero", "Hero Name : " + heroName);
                return;
            }
        }
        throw new HearthStoneException("This hero does not exist or you can not choose it!");
    }

    public static void showCollectionCards() {
        for (Card card : HearthStone.currentAccount.getCurrentCollection().getCards()) {
            System.out.println(card.getName() + " " + HearthStone.currentAccount.getCurrentCollection().getNumberOfCard().get(card.getId()));
        }
    }

    public static void showDeckCards() {
        for (Card card : HearthStone.currentAccount.getCurrentDeck().getCards()) {
            System.out.println(card.getName() + " " + HearthStone.currentAccount.getCurrentDeck().getNumberOfCard().get(card.getId()));
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
        for (Card card : HearthStone.currentAccount.getCurrentCollection().getCards()) {
            if (card.getName().equals(cardName)) {
                currentCard = card;
            }
        }
        if (currentCard == null) {
            throw new HearthStoneException("this card does not exist in your collection or does not exist at all!");
        }
        HearthStone.currentAccount.getCurrentDeck().add(currentCard, cnt);
        DataBase.saveLog("Add Card To Deck", "Card Name : " + cardName + ", Number " + cnt);
    }

    public static void removeFromDeck(String cardName, int cnt) throws Exception {
        Card currentCard = null;
        for (Card card : HearthStone.currentAccount.getCurrentCollection().getCards()) {
            if (card.getName().equals(cardName)) {
                currentCard = card;
            }
        }
        if (currentCard == null) {
            throw new HearthStoneException("this card does not exist in your collection or does not exist at all!");
        }
        HearthStone.currentAccount.getCurrentDeck().remove(currentCard, cnt);
        DataBase.saveLog("Remove Card From Deck", "Card Name : " + cardName + ", Number " + cnt);
    }

    public static void cli() {
        Scanner scanner = new Scanner(System.in);
        String command, cardName, heroName, sure;
        int number;

        while (true) {
            try {
                System.out.print(HearthStone.ANSI_YELLOW + HearthStone.currentAccount.getUsername() + "@" + "HearthStone @CollectionManager : " + HearthStone.ANSI_RESET);
                command = scanner.nextLine().trim();

                switch (command) {
                    case "help":
                        System.out.println("select hero : for choose a hero!");
                        System.out.println("all heroes : show all heroes in your account!");
                        System.out.println("my hero : show your hero!");
                        System.out.println("my cards : show all your cards according to this hero!");
                        System.out.println("my deck : show cards in your deck!");
                        System.out.println("can add : show cards you can add to your deck!");
                        System.out.println("add : you can add cards to your deck!");
                        System.out.println("remove : you can remove cards from you deck!");
                        System.out.println("exit : exit from collection manager!");
                        System.out.println("EXIT : exit from hearth stone!");
                        break;
                    case "select hero":
                        System.out.print("select your hero name : ");
                        heroName = scanner.nextLine().trim();
                        selectHero(heroName);
                        break;
                    case "all heroes":
                        showAllHeroes();
                        break;
                    case "my hero":
                        showMyHero();
                        break;
                    case "my cards":
                        showCollectionCards();
                        break;
                    case "my deck":
                        showDeckCards();
                        break;
                    case "can add":
                        showAddableCards();
                        break;
                    case "add":
                        System.out.print("please enter card name : ");
                        cardName = scanner.nextLine().trim();
                        System.out.print("please enter number of it you want to add : ");
                        number = scanner.nextInt();
                        scanner.nextLine().trim();
                        addToDeck(cardName, number);
                        //LOG : add to deck
                        break;
                    case "remove":
                        System.out.print("please enter card name : ");
                        cardName = scanner.nextLine().trim();
                        System.out.print("please enter number of it you want to remove : ");
                        number = scanner.nextInt();
                        scanner.nextLine().trim();
                        removeFromDeck(cardName, number);
                        //LOG : remove from deck
                        break;
                    case "exit":
                        HearthStone.cli();
                        break;
                    case "EXIT":
                        System.out.print(HearthStone.ANSI_RED + "are you sure you want to EXIT?! (y/n) " + HearthStone.ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if (sure.equals("y")) {
                            HearthStone.logout();
                            System.exit(0);
                        }
                        break;
                    default:
                        System.out.println("please enter correct command!");
                }
            } catch (HearthStoneException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred!");
            }
        }
    }
}
