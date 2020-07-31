package hearthstone.logic.gamestuff;

import hearthstone.models.card.Card;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
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