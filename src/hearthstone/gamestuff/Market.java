package hearthstone.gamestuff;

import hearthstone.HearthStone;
import hearthstone.models.cards.Card;
import hearthstone.models.heroes.HeroType;
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
        System.out.println("Your coins : " + HearthStone.currentAccount.getCoins());
    }

    public void showCardsCanBuy() throws Exception{
        for (Card card : cards) {
            if (card.getHeroType() == HeroType.ALL || card.getHeroType() == HearthStone.currentAccount.getCurrentHero().getType()) {
                if (HearthStone.currentAccount.canBuy(card, 1)) {
                    System.out.println(card.getName() + " " + numberOfCard.get(card.getId()));
                }
            }
        }
    }

    public void showCardsCanSell() throws Exception{
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
        Card baseCard = HearthStone.getCardByName(cardName);
        if (numberOfCard.get(baseCard.getId()) < cnt) {
            throw new HearthStoneException("It does not exist " + cnt + " of this card in the market!");
        }
        HearthStone.currentAccount.buyCards(baseCard, cnt);
        removeCard(baseCard, cnt);
    }

    public void sell(String cardName, int cnt) throws Exception {
        Card baseCard = HearthStone.getCardByName(cardName);
        HearthStone.currentAccount.sellCards(baseCard, cnt);
        addCard(baseCard, cnt);
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
                        hearthstone.util.Logger.saveLog("help", "in market, requested help!");
                        System.out.println("wallet : see your coins!");
                        System.out.println("cs : show all cards you can sell!");
                        System.out.println("cb : show all cards you can buy!");
                        System.out.println("buy : to buy cards, then you should enter card name and number of card you want to buy");
                        System.out.println("sell : to sell cards, then you should enter card name and number of card you want to sell");
                        System.out.println("exit : exit from market and back to main menu!");
                        System.out.println("EXIT : exit from hearth stone!");
                        break;
                    case "wallet":
                        hearthstone.util.Logger.saveLog("wallet", "in market, requested wallet.");
                        HearthStone.market.showWallet();
                        break;
                    case "cs":
                        hearthstone.util.Logger.saveLog("can sell", "in market, requested that cards can be soled.");
                        HearthStone.market.showCardsCanSell();
                        break;
                    case "cb":
                        hearthstone.util.Logger.saveLog("can buy", "in market, requested cards that can be bought.");
                        HearthStone.market.showCardsCanBuy();
                        break;
                    case "buy":
                        hearthstone.util.Logger.saveLog("buy", "in market, requested to buy.");
                        System.out.print("card name : ");
                        cardName = scanner.nextLine().trim();
                        System.out.print("how many you want to buy : ");
                        number = scanner.nextInt();
                        scanner.nextLine().trim();
                        HearthStone.market.buy(cardName, number);
                        hearthstone.util.Logger.saveLog("buy", "in market, bought " + number + " of " + cardName + "!");
                        break;
                    case "sell":
                        hearthstone.util.Logger.saveLog("sell", "in market, requested to sell.");
                        System.out.print("card name : ");
                        cardName = scanner.nextLine().trim();
                        System.out.print("how many you want to sell : ");
                        number = scanner.nextInt();
                        scanner.nextLine().trim();
                        HearthStone.market.sell(cardName, number);
                        hearthstone.util.Logger.saveLog("sell", "in market, sold " + number + " of " + cardName + "!");
                        break;
                    case "exit" :
                        hearthstone.util.Logger.saveLog("exit", "exited from market");
                        HearthStone.cli();
                        break;
                    case "EXIT" :
                        hearthstone.util.Logger.saveLog("EXIT", "requested to EXIT");
                        System.out.print(HearthStone.ANSI_RED + "are you sure you want to EXIT?! (y/n) " + HearthStone.ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if(sure.equals("y")) {
                            //LOG : registration
                            hearthstone.util.Logger.saveLog("EXIT", "EXITED from hearth stone");
                            HearthStone.logout();
                            System.exit(0);
                        }
                        break;
                    default :
                        System.out.println("please enter correct command! (enter help for more info)");
                }
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
    }
}

