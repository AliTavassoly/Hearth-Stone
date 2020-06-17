package hearthstone.models.player;

import hearthstone.models.Deck;
import hearthstone.models.hero.Hero;

public class AIPlayer extends Player {
    public AIPlayer(Hero hero, Deck deck) {
        super(hero, deck);
    }

    @Override
    public void startTurn() throws Exception {
        super.startTurn();
        playTheTurn();
    }

    public void playTheTurn(){
        // AI SHOULD BE HERE
    }
}
