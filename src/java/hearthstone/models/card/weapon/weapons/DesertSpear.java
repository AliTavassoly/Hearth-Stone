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
        //Mapper.damage(this.attack, minionCard);
        minionCard.gotDamage(this.attack);
        // Mapper.updateBoard();
        HSServer.getInstance().updateGameRequest(playerId);

        try {
            /*Mapper.damage(minionCard.getAttack(),
                    Mapper.getHero(getPlayerId()));*/
            HSServer.getInstance().getPlayer(playerId).getHero().gotDamage(minionCard.getAttack());
            // Mapper.updateBoard();
            HSServer.getInstance().updateGameRequest(playerId);
        } catch (HearthStoneException ignore) { }

        //Mapper.makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName("Locust"));
        HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndSummonMinion(ServerData.getCardByName("Locust"));

        if (minionCard instanceof IsAttacked) {
            //Mapper.isAttacked((IsAttacked)minionCard);
            ((IsAttacked) minionCard).isAttacked();
        }
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (/*DataTransform.haveTaunt(hero.getPlayerId())*/HSServer.getInstance().getPlayer(hero.getPlayerId()).haveTaunt()) {
            throw new HearthStoneException("There is taunt in front of you!");
        }
        //Mapper.damage(this.attack, hero);
        hero.gotDamage(this.attack);
        // Mapper.updateBoard();
        // HSServer.getInstance().updateGameRequest(playerId);

        //Mapper.makeAndSummonMinion(getPlayerId(), HearthStone.getCardByName("Locust"));
        HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndSummonMinion(ServerData.getCardByName("Locust"));

        // Mapper.updateBoard();
        HSServer.getInstance().updateGameRequest(playerId);
    }
}
