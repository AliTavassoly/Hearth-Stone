package hearthstone.models.card.spell.spells;

import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;

@Entity
public class Sprint extends SpellCard {
    public Sprint() {
    }

    public Sprint(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        try {
            for (int i = 0; i < 4; i++)
                HSServer.getInstance().getPlayer(getPlayerId()).drawCard();
        } catch (HearthStoneException ignore) {
        }
    }
}
