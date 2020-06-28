package hearthstone.logic.models.card.weapon.weapons;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.IsAttacked;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;

public class Flamereaper extends WeaponCard {
    public Flamereaper() {
    }

    public Flamereaper(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public void attack(MinionCard minionCard) throws HearthStoneException {
        ArrayList<MinionCard> neighbors = new ArrayList<>();
        neighbors = DataTransform.getInstance().getNeighbors(this.getPlayer().getEnemyPlayerId(),
                minionCard);

        Mapper.getInstance().damage(this.attack, minionCard);

        for (MinionCard minionCard1 : neighbors) {
            try {
                Mapper.getInstance().damage(this.getAttack(), minionCard1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (minionCard instanceof IsAttacked) {
            ((IsAttacked) minionCard).isAttacked();
        }
    }
}
