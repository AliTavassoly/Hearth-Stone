package hearthstone.models.card.minion.minions;

import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class HulkingOverfiend extends MinionCard {
    @Transient
    private int thisTurnAttack;

    public HulkingOverfiend() {
    }

    public HulkingOverfiend(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                            boolean isDeathRattle, boolean isTriggeredEffect,
                            boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                            boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void startTurnBehave() {
        super.startTurnBehave();
        thisTurnAttack = 0;
    }

    @Override
    public void found(Object object) throws HearthStoneException {
        if (object instanceof MinionCard) {
            if (((Card) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((MinionCard) object);
                if (!isFirstTurn)
                    numberOfAttack--;

                if (((MinionCard) object).getHealth() <= 0)
                    numberOfAttack++;

                numberOfAttackedMinion++;
                thisTurnAttack++;
            }
        } else if (object instanceof Hero) {
            if (((Hero) object).getPlayerId() == this.getPlayerId()) {
                throw new HearthStoneException("Choose enemy!");
            } else if(isFirstTurn){
                throw new HearthStoneException("Rush card can not attack to hero in first turn!");
            } else {
                this.attack((Hero) object);
                numberOfAttack--;
                numberOfAttackedHero++;
                thisTurnAttack++;
            }
        }
    }

    @Override
    public boolean canAttack() {
        return ((numberOfAttack > 0 && thisTurnAttack != 2) || (isFirstTurn && numberOfAttackedMinion == 0)) && !isFreeze;
    }
}
