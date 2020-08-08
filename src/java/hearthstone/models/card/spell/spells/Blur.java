package hearthstone.models.card.spell.spells;

import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;

import javax.persistence.Entity;

@Entity
public class Blur  extends SpellCard{
    public Blur() { }

    public Blur(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        //Mapper.addImmunity(getPlayerId(), 1, Mapper.getHero(getPlayerId()));
        HSServer.getInstance().getPlayer(playerId).getHero().addImmunity(1);
        //Mapper.handleImmunities(getPlayerId(), Mapper.getHero(getPlayerId()));
        HSServer.getInstance().getPlayer(playerId).getHero().handleImmunities();
    }
}
