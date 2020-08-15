import hearthstone.models.Deck;
import hearthstone.models.behaviours.FriendlyMinionDies;
import hearthstone.models.card.Card;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.player.Player;
import hearthstone.server.network.HSServer;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class TBPlayer extends Player {
    public TBPlayer() {
        super();
    }

    public TBPlayer(Hero hero, Deck deck, String username) {
        super(hero, deck, username);
    }

    @Override
    protected boolean isMinionDeath(MinionCard minionCard) {
        return minionCard.getHealth() < minionCard.getInitialHealth(); // One Shot
    }

    @Override
    protected int calculateMaxMana() {
        return 11;
    }

    @Override
    protected int calculateMana() {
        return hand.size() + 5;
    }

    @Override
    protected int maxCardInLand() {
        return 7;
    }

    @Override
    protected int maxCardInHand() {
        return 8;
    }

    @Override
    protected void initialChangesOnDeck() {
        // can change every thing on "deck" field!
    }
}
