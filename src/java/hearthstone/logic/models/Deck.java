package hearthstone.logic.models;

import hearthstone.logic.models.cards.Card;

import java.util.ArrayList;

public class Deck {
    private String name;
    private ArrayList<Card> cards = new ArrayList<>();

    public Deck(){ }

    public Deck(String name){
        this.name = name;
    }
}
