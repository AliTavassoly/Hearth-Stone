package hearthstone.models.card.weapon.weapons;

import hearthstone.DataTransform;
import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.models.behaviours.IsAttacked;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;

@Entity
public class DesertSpear extends WeaponCard {
    public DesertSpear() {
    }

    public DesertSpear(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public void attack(MinionCard minionCard) throws HearthStoneException {
        Mapper.getInstance().damage(this.attack, minionCard);

        try {
            Mapper.getInstance().damage(minionCard.getAttack(),
                    DataTransform.getInstance().getHero(getPlayerId()));
        } catch (HearthStoneException ignore) { }

        Mapper.getInstance().makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName("Locust"));
        if (minionCard instanceof IsAttacked) {
            Mapper.getInstance().isAttacked((IsAttacked)minionCard);
        }
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (DataTransform.getInstance().haveTaunt(hero.getPlayerId())) {
            throw new HearthStoneException("There is taunt in front of you!");
        }
        Mapper.getInstance().damage(this.attack, hero);
        Mapper.getInstance().makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName("Locust"));

        Mapper.getInstance().updateBoard();
    }
}
