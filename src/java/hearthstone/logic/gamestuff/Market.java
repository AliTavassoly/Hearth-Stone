package hearthstone.logic.gamestuff;

import hearthstone.HearthStone;
import hearthstone.logic.models.card.Card;
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
}

