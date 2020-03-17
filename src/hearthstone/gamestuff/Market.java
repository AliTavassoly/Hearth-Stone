package hearthstone.gamestuff;

import hearthstone.HearthStone;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.HeroType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Market {
    private ArrayList<Card> cards = new ArrayList<>();
    private Map<Integer, Integer> numberOfCard = new HashMap<>();

    public void showAllCards() {
        for (Card card : cards) {
            System.out.println(card.getName() + " " + numberOfCard.get(card.getId()));
        }
    }

    public void showWallet() {
        System.out.println("Your coins :" + HearthStone.currentAccount.getCoins());
    }

    public void showCardsCanBuy() {
        for (Card card : cards) {
            if (card.getHeroType() == HeroType.ALL || card.getHeroType() == HearthStone.currentAccount.getCurrentHero().getType()) {
                if (HearthStone.currentAccount.canBuy(card, 1)) {
                    System.out.println(card.getName() + " " + numberOfCard.get(card.getId()));
                }
            }
        }
    }

    public void showCardsCanSell() {
        for (Card card : cards) {
            if (HearthStone.currentAccount.canSell(card, 1)) {
                System.out.println(card.getName() + " " + numberOfCard.get(card.getId()));
            }
        }
    }

    public void removeCard(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) - cnt);
        if (numberOfCard.get(card.getId()) == 0) {
            cards.remove(card);
        }
    }

    public void addCard(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (numberOfCard.get(card.getId()) == 0) {
            cards.add(card);
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) + cnt);
    }

    public void buy(String cardName, int cnt) throws Exception {
        Card currentCard = null;
        for (Card card : HearthStone.baseCards.values()) {
            if (card.getName().equals(cardName)) {
                currentCard = card;
                break;
            }
        }
        if(currentCard == null){
            throw new HearthStoneException("please enter correct name !");
        }
        if (numberOfCard.get(currentCard.getId()) > cnt) {
            throw new HearthStoneException("There is not this amount from this card !");
        }
        HearthStone.currentAccount.buyCards(currentCard, cnt);
        removeCard(currentCard, cnt);
    }

    public void sell(String cardName, int cnt) throws Exception {
        Card currentCard = null;
        for (Card card : HearthStone.baseCards.values()) {
            if (card.getName().equals(cardName)) {
                currentCard = card;
                break;
            }
        }
        if(currentCard == null){
            throw new HearthStoneException("please enter correct name !");
        }
        HearthStone.currentAccount.sellCards(currentCard, cnt);
        addCard(currentCard, cnt);
    }

    public static void cli() {
        Scanner scanner = new Scanner(System.in);
        String command, cardName, sure;
        int number;

        while (true) {
            try {
                System.out.print(HearthStone.ANSI_YELLOW + HearthStone.currentAccount.getUsername() + "@" + "HearthStone @Market : " + HearthStone.ANSI_RESET);
                command = scanner.nextLine().trim();

                switch (command) {
                    case "help":
                        System.out.println("wallet : see your coins!");
                        System.out.println("cs : show all cards you can sell!");
                        System.out.println("cb : show all cards you can buy!");
                        System.out.println("buy : to buy cards, then you should enter card name and number of card you want to buy");
                        System.out.println("sell : to sell cards, then you should enter card name and number of card you want to sell");
                        System.out.println("exit : exit from market and back to main menu!");
                        System.out.println("EXIT : exit from hearth stone!");
                        break;
                    case "wallet":
                        HearthStone.market.showWallet();
                        break;
                    case "cs":
                        HearthStone.market.showCardsCanSell();
                        break;
                    case "cb":
                        HearthStone.market.showCardsCanBuy();
                        break;
                    case "buy":
                        System.out.print("card name : ");
                        cardName = scanner.nextLine().trim();
                        System.out.print("how many you want to buy : ");
                        number = scanner.nextInt();
                        scanner.nextLine().trim();
                        HearthStone.market.buy(cardName, number);
                        //LOG : buy
                        break;
                    case "sell":
                        System.out.print("card name : ");
                        cardName = scanner.nextLine().trim();
                        System.out.print("how many you want to sell : ");
                        number = scanner.nextInt();
                        scanner.nextLine().trim();
                        HearthStone.market.sell(cardName, number);
                        //LOG : sell
                        break;
                    case "exit" :
                        HearthStone.cli();
                        break;
                    case "EXIT" :
                        System.out.print(HearthStone.ANSI_RED + "are you sure you want to EXIT?! (y/n) " + HearthStone.ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if(sure.equals("y")) {
                            //LOG : registration
                            HearthStone.logout();
                            System.exit(0);
                        }
                        break;
                    default :
                        System.out.println("please enter correct command! (enter help for more info)");
                }
            } catch (HearthStoneException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred!");
            }
        }
    }

}

