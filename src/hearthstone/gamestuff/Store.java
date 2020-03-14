package hearthstone.gamestuff;

import hearthstone.data.bean.Account;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.HeroType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Store {
    public static ArrayList<Card> cards = new ArrayList<>();
    public static Map<Integer, Integer> numberOfCard = new HashMap<>();

    public static void showAllCards() {
        for (int i = 0; i < cards.size(); i++) {
            System.out.println(cards.get(i).getName() + " " + numberOfCard.get(cards.get(i).getId()));
        }
    }

    public static void showCardsCanBuy(Account account) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getHeroType() == HeroType.ALL || cards.get(i).getHeroType() == account.getCurrentHero().getType()) {
                if (account.canAddCard(cards.get(i), 1)) {
                    //System.out.println(cards.get(i).getName());
                }
            }
        }
    }

    public static void removeCard(Card card, int cnt) {
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) - cnt);
        if(numberOfCard.get(card.getId()) == 0){
            cards.remove(card);
        }
    }

    public static void addCard(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if(numberOfCard.get(card.getId()) == 0){
            cards.add(card);
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) + cnt);
    }

    public static boolean canBuy(Card card, int cnt, Account account) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (numberOfCard.get(card.getId()) < cnt || account.getCoins() < cnt * card.getBuyCost() || !account.canAddCard(card, cnt))
            return false;
        if (card.getHeroType() == HeroType.ALL || account.getCurrentHero().getType() == card.getHeroType()) {
            return true;
        } else {
            //System.err.println("This card can not be used for this hero !");
            return false;
        }
    }

    public static void buy(Card card, int cnt, Account account) {
        account.setCoins(account.getCoins() - cnt * card.getBuyCost());
        account.addCard(card, cnt);
        Store.removeCard(card, cnt);
    }

    public static boolean canSell(Card card, int cnt, Account account) {
        return account.canRemoveCard(card, cnt);
    }

    public static void sell(Card card, int cnt, Account account) {
        account.setCoins(account.getCoins() + cnt * card.getSellCost());
        account.removeCard(card, cnt);
        Store.addCard(card, cnt);
    }
}
