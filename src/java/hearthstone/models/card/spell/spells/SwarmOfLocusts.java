package hearthstone.models.card.spell.spells;

import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class SwarmOfLocusts extends SpellCard {
    public SwarmOfLocusts() {
    }

    public SwarmOfLocusts(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        for (int i = 0; i < 7; i++) {
            HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndSummonMinion(ServerData.getCardByName("Locust"));
        }
        HSServer.getInstance().updateGame(playerId);
    }
}
