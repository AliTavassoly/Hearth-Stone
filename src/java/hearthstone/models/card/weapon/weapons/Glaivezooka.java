package hearthstone.models.card.weapon.weapons;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.Battlecry;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;

@Entity
public class Glaivezooka extends WeaponCard implements Battlecry {
    public Glaivezooka() {
    }

    public Glaivezooka(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public void battlecry() {
        MinionCard minionCard = DataTransform.getInstance().getRandomMinionFromLand(getPlayerId());
        if (minionCard != null)
            Mapper.getInstance().addAttack(1, minionCard);
    }
}
