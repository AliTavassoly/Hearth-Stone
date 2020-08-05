package hearthstone.models.card.spell.spells;

import hearthstone.Mapper;
import hearthstone.client.gui.controls.dialogs.CardSelectionDialog;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Tracking extends SpellCard {
    public Tracking() { }

    public Tracking(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        ArrayList<Card> cards = Mapper.getTopCards(getPlayerId(), 3);
        if(cards == null || cards.size() == 0)
            return;

        CardSelectionDialog dialog = new CardSelectionDialog(GameFrame.getInstance(), cards);
        Card card = dialog.getCard();
        try {
            //Mapper.drawCard(getPlayerId(), card);
            Mapper.getPlayer(getPlayerId()).drawCard(card);
            Mapper.updateBoard();
        } catch (HearthStoneException ignore) {}
    }
}