package hearthstone.logic.models.card.weapon.weapons;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.IsAttacked;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

public class WarglaivesOfAzzinoth extends WeaponCard {
    private int numberOfThisTurnAttacked;

    public WarglaivesOfAzzinoth() {
    }

    public WarglaivesOfAzzinoth(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public boolean canAttack() {
        return numberOfAttack > 0 &&
                DataTransform.getInstance().getWhoseTurn() == getPlayerId() &&
                numberOfThisTurnAttacked < 2;
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
        Mapper.getInstance().damage(this.attack, minionCard, false);
        numberOfAttack++;

        try {
            Mapper.getInstance().damage(minionCard.getAttack(),
                    DataTransform.getInstance().getHero(getPlayerId()), false);
        } catch (HearthStoneException ignore) { }

        Mapper.getInstance().updateBoard();

        if (minionCard instanceof IsAttacked) {
            Mapper.getInstance().isAttacked((IsAttacked) minionCard);
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
