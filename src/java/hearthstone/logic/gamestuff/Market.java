package hearthstone.logic.gamestuff;

import hearthstone.HearthStone;
import hearthstone.logic.models.cards.Card;
import hearthstone.util.GetByName;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class Market {
    private ArrayList<Card> cards = new ArrayList<>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int numberOfCard(Card card) {
        int ans = 0;
        for (Card baseCard : cards) {
            if (baseCard.getId() == card.getId()) {
                ans++;
            }
        }
        return ans;
    }

    public void showAllCards() {
        for (Card baseCard : cards) {
            System.out.println(baseCard.getName());
        }
    }

    public void showWallet() {
        System.out.println("Your coins : " + HearthStone.currentAccount.getGem());
    }

    public void showCardsCanBuy() throws Exception {
        boolean isEmpty = true;
        for (Card card : cards) {
            if (HearthStone.currentAccount.canBuy(card, 1)) {
                System.out.println(card.getName());
                isEmpty = false;
            }
        }
        if(isEmpty){
            System.out.println("You can not buy any card!");
        }
    }

    public void showCardsCanSell() throws Exception {
        boolean isEmpty = true;
        for (Card card : HearthStone.baseCards.values()) {
            if (HearthStone.currentAccount.canSell(card, 1)) {
                System.out.println(card.getName());
                isEmpty = false;
            }
        }
        if (isEmpty){
            System.out.println("You can not sell any card!");
        }
    }

    public void removeCard(Card baseCard, int cnt) {
        for (int i = 0; i < cnt; i++) {
            for (int j = 0; j < cards.size(); j++) {
                if (baseCard.getId() == cards.get(j).getId()) {
                    cards.remove(j);
                    break;
                }
            }
        }
    }

    public void addCard(Card baseCard, int cnt) {
        for (int i = 0; i < cnt; i++) {
            cards.add(baseCard.copy());
        }
    }

    public void buy(String cardName, int cnt) throws Exception {
        Card baseCard = GetByName.getCardByName(cardName);
        if (numberOfCard(baseCard) < cnt) {
            throw new HearthStoneException("It does not exist " + cnt + " of this card in the market!");
        }
        HearthStone.currentAccount.buyCards(baseCard, cnt);
        removeCard(baseCard, cnt);
    }

    public void sell(String cardName, int cnt) throws Exception {
        Card baseCard = GetByName.getCardByName(cardName);
        HearthStone.currentAccount.sellCards(baseCard, cnt);
        addCard(baseCard, cnt);
    }

    /*public static void cli() {
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
                    case "exit":
                        hearthstone.util.Logger.saveLog("exit", "exited from market");
                        HearthStone.cli();
                        break;
                    case "EXIT":
                        hearthstone.util.Logger.saveLog("EXIT", "requested to EXIT");
                        System.out.print(HearthStone.ANSI_RED + "are you sure you want to EXIT?! (y/n) " + HearthStone.ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if (sure.equals("y")) {
                            //LOG : registration
                            hearthstone.util.Logger.saveLog("EXIT", "EXITED from hearth stone");
                            HearthStone.logout();
                            System.exit(0);
                        }
                        break;
                    default:
                        System.out.println("please enter correct command! (enter help for more info)");
                }
                DataBase.save();
            } catch (HearthStoneException e) {
                try {
                    hearthstone.util.Logger.saveLog("ERROR", hearthstone.util.Logger.exceptionToLog(e));
                } catch (Exception f) {
                }
                System.out.println(e.getMessage());
            } catch (Exception e) {
                try {
                    hearthstone.util.Logger.saveLog("ERROR", hearthstone.util.Logger.exceptionToLog(e));
                } catch (Exception f) {
                }
                System.out.println("An error occurred!");
            }
        }
    }*/
}

