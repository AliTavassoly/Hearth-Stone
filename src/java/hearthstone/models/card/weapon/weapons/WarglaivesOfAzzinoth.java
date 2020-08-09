package hearthstone.models.card.weapon.weapons;

import com.fasterxml.jackson.annotation.JsonProperty;
import hearthstone.models.behaviours.IsAttacked;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class WarglaivesOfAzzinoth extends WeaponCard {
    @Transient
    @JsonProperty("numberOfThisTurnAttacked")
    private int numberOfThisTurnAttacked;

    public WarglaivesOfAzzinoth() {
    }

    public WarglaivesOfAzzinoth(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public boolean canAttack() {
        boolean weaponCanAttack = super.canAttack();
        return numberOfThisTurnAttacked < 2 && weaponCanAttack;
    }

    @Override
    public void startTurnBehave() {
        numberOfThisTurnAttacked = 0;
        numberOfAttack = 1;
        durability--;
        isFirstTurn = false;
    }

    @Override
    public void attack(MinionCard minionCard) throws HearthStoneException {
        //Mapper.damage(this.attack, minionCard, false);
        minionCard.gotDamage(this.attack);

        log(minionCard);
        numberOfAttack++;

        try {
            /*Mapper.damage(minionCard.getAttack(),
                    Mapper.getHero(getPlayerId()), false);*/
            HSServer.getInstance().getPlayer(playerId).getHero().gotDamage(minionCard.getAttack());
        } catch (HearthStoneException ignore) { }

        // Mapper.updateBoard();
        HSServer.getInstance().updateGameRequest(playerId);

        if (minionCard instanceof IsAttacked) {
            //Mapper.isAttacked((IsAttacked) minionCard);
            ((IsAttacked) minionCard).isAttacked();
        }
    }

    @Override
    public void found(Object object) throws HearthStoneException {
        if (object instanceof MinionCard) {
            if (((Card) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((MinionCard) object);
                numberOfAttack--;
                numberOfThisTurnAttacked++;
            }
        } else if (object instanceof Hero) {
            if (((Hero) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((Hero) object);
                numberOfAttack--;
                numberOfThisTurnAttacked++;
            }
        }
    }
}
