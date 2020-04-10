package hearthstone.gamestuff;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.models.cards.Card;
import hearthstone.models.heroes.Hero;
import hearthstone.util.GetByName;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Logger;

import java.util.Scanner;

public class CollectionManager {

    public static void showAllHeroes() throws Exception {
        for (Hero hero : HearthStone.currentAccount.getHeroes()) {
            System.out.println(hero.getName());
        }
    }

    public static void showMyHero() throws Exception {
        if (HearthStone.currentAccount.getCurrentHero() == null) {
            System.out.println("You did not choose a hero!");
            return;
        }
        System.out.println(HearthStone.currentAccount.getCurrentHero().getName());
    }

    public static void selectHero(String heroName) throws Exception {
        HearthStone.currentAccount.setCurrentHero(GetByName.getHeroByName(heroName).copy());
    }

    public static void showCollectionCards() throws Exception {
        if (HearthStone.currentAccount.getCurrentHero() == null)
            throw new HearthStoneException("You did not choose a hero!");
        boolean isEmpty =  true;
        for (Card baseCard : HearthStone.currentAccount.getCurrentHero().getCollection()) {
            System.out.println(baseCard.getName());
            isEmpty = false;
        }
        if(isEmpty){
            System.out.println("Your collection is empty!");
        }
    }

    public static void showDeckCards() throws Exception {
        if(HearthStone.currentAccount.getCurrentHero() == null)
            throw new HearthStoneException("You did not choose a hero!");
        boolean isEmpty = true;
        for (Card baseCard : HearthStone.currentAccount.getCurrentHero().getDeck()) {
            System.out.println(baseCard.getName());
            isEmpty = false;
        }
        if(isEmpty) {
            System.out.println("Your deck is empty!");
        }
    }

    public static void showAddableCards() throws Exception {
        if(HearthStone.currentAccount.getCurrentHero() == null)
            throw new HearthStoneException("You did not choose a hero!");
        boolean isEmpty = true;
        for (Card baseCard : HearthStone.currentAccount.getCurrentHero().getCollection()) {
            if (HearthStone.currentAccount.getCurrentHero().numberInDeck(baseCard) < HearthStone.currentAccount.getCurrentHero().numberInCollection(baseCard)) {
                System.out.println(baseCard.getName());
                isEmpty = false;
            }
        }
        if(isEmpty){
            System.out.println("You can not add any card to your deck!");
        }
    }

    public static void addToDeck(String cardName, int cnt) throws Exception {
        if(HearthStone.currentAccount.getCurrentHero() == null)
            throw new HearthStoneException("You did not choose a hero!");
        Card baseCard = GetByName.getCardByName(cardName).copy();
        HearthStone.currentAccount.getCurrentHero().addDeck(baseCard, cnt);
        Logger.saveLog("Add Card To Deck", "Card Name : " + cardName + ", Number " + cnt);
    }

    public static void removeFromDeck(String cardName, int cnt) throws Exception {
        if(HearthStone.currentAccount.getCurrentHero() == null)
            throw new HearthStoneException("You did not choose a hero!");
        Card baseCard = GetByName.getCardByName(cardName).copy();
        HearthStone.currentAccount.getCurrentHero().removeDeck(baseCard, cnt);
        Logger.saveLog("Remove Card From Deck", "Card Name : " + cardName + ", Number " + cnt);
    }

    /*public static void cli() {
        Scanner scanner = new Scanner(System.in);
        String command, cardName, heroName, sure;
        int number;

        while (true) {
            try {
                System.out.print(HearthStone.ANSI_YELLOW + HearthStone.currentAccount.getUsername() + "@" + "HearthStone @CollectionManager : " + HearthStone.ANSI_RESET);
                command = scanner.nextLine().trim();

                switch (command) {
                    case "help":
                        hearthstone.util.Logger.saveLog("help", "In collection manager, requested help!");
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
                        hearthstone.util.Logger.saveLog("hero select", "in collection manager, requested to select hero");
                        System.out.print("select your hero name (Start with capital letter): ");
                        heroName = scanner.nextLine().trim();
                        selectHero(heroName);
                        hearthstone.util.Logger.saveLog("hero select", "in collection manager, " + heroName + " was selected to be current hero.");
                        break;
                    case "all heroes":
                        hearthstone.util.Logger.saveLog("all heroes", "in collection manager, requested to show all heroes");
                        showAllHeroes();
                        break;
                    case "my hero":
                        hearthstone.util.Logger.saveLog("selected hero", "in collection manager, requested to show selected hero");
                        showMyHero();
                        break;
                    case "my cards":
                        hearthstone.util.Logger.saveLog("all cards", "in collection manager, requested to show all cards.");
                        showCollectionCards();
                        break;
                    case "my deck":
                        hearthstone.util.Logger.saveLog("deck", "in collection manager, requested to show deck.");
                        showDeckCards();
                        break;
                    case "can add":
                        hearthstone.util.Logger.saveLog("can add", "in collection manager, requested to show the cards that can be added.");
                        showAddableCards();
                        break;
                    case "add":
                        hearthstone.util.Logger.saveLog("add", "in collection manager, requested to add to deck.");
                        System.out.print("please enter card name : ");
                        cardName = scanner.nextLine().trim();
                        System.out.print("please enter number of it you want to add : ");
                        number = scanner.nextInt();
                        scanner.nextLine().trim();
                        addToDeck(cardName, number);
                        hearthstone.util.Logger.saveLog("add", "in collection manager, added" + number + " of " + cardName + "!");
                        break;
                    case "remove":
                        hearthstone.util.Logger.saveLog("remove", "in collection manager, requested to remove from deck.");
                        System.out.print("please enter card name : ");
                        cardName = scanner.nextLine().trim();
                        System.out.print("please enter number of it you want to remove : ");
                        number = scanner.nextInt();
                        scanner.nextLine().trim();
                        removeFromDeck(cardName, number);
                        hearthstone.util.Logger.saveLog("remove", "in collection manager, removed" + number + " of " + cardName + "!");
                        break;
                    case "exit":
                        hearthstone.util.Logger.saveLog("exit", "exited from collection manager");
                        HearthStone.cli();
                        break;
                    case "EXIT":
                        hearthstone.util.Logger.saveLog("EXIT", "requested to EXIT");
                        System.out.print(HearthStone.ANSI_RED + "are you sure you want to EXIT?! (y/n) " + HearthStone.ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if (sure.equals("y")) {
                            hearthstone.util.Logger.saveLog("EXIT", "EXITED from hearth stone");
                            HearthStone.logout();
                            System.exit(0);
                        }
                        break;
                    default:
                        System.out.println("please enter correct command!");
                }
                DataBase.save();
            } catch (HearthStoneException e) {
                try {
                    hearthstone.util.Logger.saveLog("ERROR", hearthstone.util.Logger.exceptionToLog(e));
                } catch (Exception f) { }
                System.out.println(e.getMessage());
            } catch (Exception e) {
                try {
                    hearthstone.util.Logger.saveLog("ERROR", hearthstone.util.Logger.exceptionToLog(e));
                } catch (Exception f) { }
                System.out.println("An error occurred!");
            }
        }
    }*/
}
