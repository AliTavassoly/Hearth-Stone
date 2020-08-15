package hearthstone.models.card.weapon.weapons;

import hearthstone.models.behaviours.IsAttacked;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;
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
        minionCard.gotDamage(this.attack);

        HSServer.getInstance().updateGame(playerId);

        try {
            HSServer.getInstance().getPlayer(playerId).getHero().gotDamage(minionCard.getAttack());
            HSServer.getInstance().updateGame(playerId);
        } catch (HearthStoneException ignore) { }

        HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndSummonMinion(ServerData.getCardByName("Locust"));

        if (minionCard instanceof IsAttacked) {
            ((IsAttacked) minionCard).isAttacked();
        }
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (HSServer.getInstance().getPlayer(hero.getPlayerId()).haveTaunt()) {
            throw new HearthStoneException("There is taunt in front of you!");
        }
        hero.gotDamage(this.attack);

        HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndSummonMinion(ServerData.getCardByName("Locust"));

        HSServer.getInstance().updateGame(playerId);
    }
}
