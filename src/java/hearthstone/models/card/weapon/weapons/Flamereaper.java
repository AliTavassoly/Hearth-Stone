package hearthstone.models.card.weapon.weapons;

import hearthstone.Mapper;
import hearthstone.models.behaviours.IsAttacked;
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
        /*DataTransform.getNeighbors(
                        Mapper.getEnemyId(getPlayerId()),
                        minionCard);*/
        ArrayList <MinionCard> neighbors = Mapper.getPlayer(getPlayerId()).neighborCards(minionCard);

        //Mapper.damage(this.attack, minionCard);
        minionCard.gotDamage(this.attack);
        Mapper.updateBoard();

        log(minionCard);

        try {
            /*Mapper.damage(minionCard.getAttack(),
                    Mapper.getHero(getPlayerId()), false);*/
            Mapper.getHero(getPlayerId()).gotDamage(minionCard.getAttack());

        } catch (HearthStoneException ignore) { }

        for (MinionCard minionCard1 : neighbors){
            try {
                //Mapper.damage(this.getAttack(), minionCard1, false);
                minionCard1.gotDamage(this.getAttack());
            } catch (HearthStoneException ignore) { }
        }

        Mapper.updateBoard();

        if (minionCard instanceof IsAttacked) {
            //Mapper.isAttacked((IsAttacked)minionCard);
            ((IsAttacked) minionCard).isAttacked();
        }
    }
}
