package hearthstone.logic.models.card.spell.spells;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.hero.HeroType;

public class SwarmOfLocusts extends SpellCard {
    public SwarmOfLocusts() {
    }

    public SwarmOfLocusts(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        for (int i = 0; i < 7; i++) {
            Mapper.getInstance().makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName("Locust"));
        }
        Mapper.getInstance().updateBoard();
    }
}
