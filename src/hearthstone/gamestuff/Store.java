package hearthstone.gamestuff;

import hearthstone.data.bean.Account;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.HeroType;
import hearthstone.util.HearthStoneException;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Store {
    private ArrayList<Card> cards = new ArrayList<>();
    private Map<Integer, Integer> numberOfCard = new HashMap<>();

    public void showAllCards() {
        for (int i = 0; i < cards.size(); i++) {
            System.out.println(cards.get(i).getName() + " " + numberOfCard.get(cards.get(i).getId()));
        }
    }

    public void showCardsCanBuy(Account account) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getHeroType() == HeroType.ALL || cards.get(i).getHeroType() == account.getCurrentHero().getType()) {
                if (account.canBuy(cards.get(i), 1)) {
                    //System.out.println(cards.get(i).getName());
                }
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

    public void buy(Card card, int cnt, Account account) throws Exception{
        if(numberOfCard.get(card.getId()) > cnt){
            throw new HearthStoneException("There is not this amount from this card !");
        }
        account.buyCards(card, cnt);
        removeCard(card, cnt);
    }

    public void sell(Card card, int cnt, Account account) throws Exception{
        account.sellCards(card, cnt);
        addCard(card, cnt);
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

