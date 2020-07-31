package hearthstone.models.card.weapon.weapons;

import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;

@Entity
public class WickedKnife extends WeaponCard {
    public WickedKnife() { }

    public WickedKnife(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack){
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }
}
