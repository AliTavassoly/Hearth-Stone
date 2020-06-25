package hearthstone.logic.models.card.weapon.weapons;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.IsAttacked;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.HeroType;

public class Flamereaper extends WeaponCard {
    public Flamereaper() { }

    public Flamereaper(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack){
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public void attack(MinionCard minionCard) {
        for (MinionCard minionCard1: DataTransform.getInstance().getNeighbors(this.getPlayer().getEnemyPlayerId(),
                minionCard)){
            try {
                Mapper.getInstance().damage(this.getAttack(), minionCard1);
            } catch (Exception ignore) {}
        }

        try {
            Mapper.getInstance().damage(this.attack, minionCard);
        } catch (Exception ignore){ }

        if(minionCard instanceof IsAttacked){
            ((IsAttacked)minionCard).isAttacked();
        }
    }
}
