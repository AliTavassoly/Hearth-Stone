package hearthstone.models.card.weapon.weapons;

import hearthstone.Mapper;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;

@Entity
public class Candleshot extends WeaponCard {
    public Candleshot() {
    }

    public Candleshot(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public void startTurnBehave() {
        super.startTurnBehave();

        //Mapper.addImmunity(getPlayerId(), 1, Mapper.getHero(getPlayerId()));
        Mapper.getHero(getPlayerId()).addImmunity(1);

        //Mapper.handleImmunities(getPlayerId(), Mapper.getHero(getPlayerId()));
        Mapper.getHero(getPlayerId()).handleImmunities();

        Mapper.updateBoard();
    }
}
