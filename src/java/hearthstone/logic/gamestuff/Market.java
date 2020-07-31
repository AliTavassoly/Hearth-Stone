package hearthstone.logic.gamestuff;

import hearthstone.models.card.Card;

import java.util.ArrayList;

public class Market {
    private ArrayList<Card> cards = new ArrayList<>();

    public ArrayList<Card> getCards() {
        return cards;
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
}