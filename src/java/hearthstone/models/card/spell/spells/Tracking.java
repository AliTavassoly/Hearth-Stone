package hearthstone.models.card.spell.spells;

import hearthstone.models.behaviours.ChooseCardAbility;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Tracking extends SpellCard implements ChooseCardAbility {
    public Tracking() { }

    public Tracking(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        ArrayList<Card> cards = HSServer.getInstance().getPlayer(playerId).getTopCards(3);
        if(cards == null || cards.size() == 0)
            return;

        HSServer.getInstance().chooseCardAbilityRequest(cardGameId, cards, playerId);
    }


    @Override
    public void doAfterChoosingCard(Card card) {
        try {
            HSServer.getInstance().getPlayer(getPlayerId()).drawCard(card);
            HSServer.getInstance().updateGame(playerId);
        } catch (HearthStoneException ignore) {}
    }
}