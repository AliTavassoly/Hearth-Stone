package hearthstone.models.card.weapon.weapons;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.behaviours.IsAttacked;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Flamereaper extends WeaponCard {
    public Flamereaper() {
    }

    public Flamereaper(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public void attack(MinionCard minionCard) throws HearthStoneException {
        ArrayList <MinionCard> neighbors = DataTransform.getInstance().getNeighbors(
                        DataTransform.getInstance().getEnemyId(getPlayerId()),
                        minionCard);

        Mapper.getInstance().damage(this.attack, minionCard);
        log(minionCard);

        try {
            Mapper.getInstance().damage(minionCard.getAttack(),
                    DataTransform.getInstance().getHero(getPlayerId()), false);
        } catch (HearthStoneException ignore) { }

        for (MinionCard minionCard1 : neighbors){
            try {
                Mapper.getInstance().damage(this.getAttack(), minionCard1, false);
            } catch (HearthStoneException ignore) { }
        }

        Mapper.getInstance().updateBoard();

        if (minionCard instanceof IsAttacked) {
            Mapper.getInstance().isAttacked((IsAttacked)minionCard);
        }
    }
}
