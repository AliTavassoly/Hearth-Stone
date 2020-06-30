package hearthstone.logic.models.card.weapon.weapons;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.Battlecry;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.HeroType;

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
