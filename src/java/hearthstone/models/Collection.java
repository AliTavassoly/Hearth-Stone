package hearthstone.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hearthstone.server.data.GameConfigs;
import hearthstone.models.card.Card;
import hearthstone.util.HearthStoneException;
import hearthstone.util.jacksonserializers.CardListSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name = "Collection";

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonSerialize(converter = CardListSerializer.class)
    private List<Card> cards;

    @PostLoad
    void postLoad() {
        this.cards = new ArrayList<>(this.cards);
    }

    public Collection() {
    }

    public Collection(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int numberOfCards(Card baseCard) {
        int ans = 0;
        for (Card card : cards) {
            if (card.getId() == baseCard.getId()) {
                ans++;
            }
        }
        return ans;
    }

    public boolean canAdd(Card baseCard, int cnt) {
        return numberOfCards(baseCard) + cnt <= GameConfigs.maxCardOfOneType &&
                cards.size() + cnt <= GameConfigs.maxCardInCollection;
    }

    public void add(Card baseCard, int cnt) throws Exception {
        if (numberOfCards(baseCard) + cnt > GameConfigs.maxCardOfOneType) {
            throw new HearthStoneException("Can not have " +
                    (numberOfCards(baseCard) + cnt) + " numbers of " + baseCard.getName() + " card!");
        }
        if (cards.size() + cnt > GameConfigs.maxCardInCollection) {
            throw new HearthStoneException("Collection is full!");
        }
        for (int i = 0; i < cnt; i++)
            cards.add(baseCard.copy());
    }

    public boolean canRemove(Card baseCard, int cnt) {
        return numberOfCards(baseCard) - cnt >= 0;
    }

    public void remove(Card baseCard, int cnt) throws Exception {
        if (numberOfCards(baseCard) - cnt < 0) {
            throw new HearthStoneException("You don't have " + cnt + " numbers of " +
                    baseCard.getName() + " in your collection!");
        }
        for (int i = 0; i < cnt; i++) {
            for (int j = 0; j < cards.size(); j++) {
                if (cards.get(j).getId() == baseCard.getId()) {
                    cards.remove(j);
                    break;
                }
            }
        }
    }
}