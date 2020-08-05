package hearthstone.logic;

import hearthstone.models.card.Card;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name = "Market";

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(CascadeType.SAVE_UPDATE)
    //@JsonSerialize(using = HibernateDeckListSerializer.class)
    private List<Card> cards = new ArrayList<>();

    @PostLoad
    void postLoad() {
        this.cards = new ArrayList<>(this.cards);
    }

    public Market(){}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setCards(List<Card> cards){
        this.cards = cards;
    }
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