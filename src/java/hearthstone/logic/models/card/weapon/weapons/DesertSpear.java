package hearthstone.logic.models.card.weapon.weapons;

import hearthstone.DataTransform;
import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.IsAttacked;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

public class DesertSpear extends WeaponCard {
    public DesertSpear() {
    }

    public DesertSpear(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public void attack(MinionCard minionCard) throws HearthStoneException {
        Mapper.getInstance().damage(this.attack, minionCard);
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
