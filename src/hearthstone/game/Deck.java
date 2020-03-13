package hearthstone.game;

import hearthstone.game.cards.Card;
import hearthstone.game.heroes.Hero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Deck {
    private Hero hero;
    public ArrayList<Card> cards = new ArrayList<>();
    public Map<Integer, Integer> numberOfCard = new HashMap<>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Map<Integer, Integer> getNumberOfCard() { return numberOfCard; }

}
