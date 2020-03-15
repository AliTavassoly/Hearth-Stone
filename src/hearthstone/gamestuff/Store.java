package hearthstone.gamestuff;

import hearthstone.data.bean.Account;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.HeroType;
import hearthstone.util.HearthStoneException;

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
                if (account.canBuy(cards.get(i), 1)) {
                    //System.out.println(cards.get(i).getName());
                }
            }
        }
    }

    public static void removeCard(Card card, int cnt) {
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) - cnt);
        if (numberOfCard.get(card.getId()) == 0) {
            cards.remove(card);
        }
    }

    public static void addCard(Card card, int cnt) {
        numberOfCard.putIfAbsent(card.getId(), 0);
        if (numberOfCard.get(card.getId()) == 0) {
            cards.add(card);
        }
        numberOfCard.put(card.getId(), numberOfCard.get(card.getId()) + cnt);
    }

    public static void buy(Card card, int cnt, Account account) throws Exception{
        if(numberOfCard.get(card.getId()) > cnt){
            throw new HearthStoneException("There is not this amount from this card !");
        }
        account.buyCards(card, cnt);
        removeCard(card, cnt);
    }

    public static void sell(Card card, int cnt, Account account) throws Exception{
        account.sellCards(card, cnt);
        addCard(card, cnt);
    }

    public static void main(String[] args) {
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

