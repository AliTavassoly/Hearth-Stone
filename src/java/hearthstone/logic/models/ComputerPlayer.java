package hearthstone.logic.models;

import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.hero.Hero;

public class ComputerPlayer extends Player {
    public ComputerPlayer(Hero hero, Deck deck) {
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
