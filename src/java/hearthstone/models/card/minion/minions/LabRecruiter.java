package hearthstone.models.card.minion.minions;

import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.card.minion.interfaces.Battlecry;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

public class LabRecruiter extends MinionCard implements Battlecry {
    public LabRecruiter(){ }

    public LabRecruiter(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                        boolean isDeathRattle, boolean isTriggeredEffect, boolean isSpellDamage, boolean isDivineShield,
                        boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void battlecry() {
        MinionCard minionCard = (MinionCard) getPlayer().getRandomCardFromOriginalDeck(CardType.MINIONCARD);

        if(minionCard == null)
            return;

        getPlayer().makeAndPutDeck(minionCard.copy());
        getPlayer().makeAndPutDeck(minionCard.copy());
        getPlayer().makeAndPutDeck(minionCard.copy());
    }

    @Override
    public void attack(MinionCard minionCard) {
        minionCard.setHealth(minionCard.getHealth() - this.attack);
        this.health -= minionCard.getAttack();
    }

    @Override
    public void attack(Hero hero) throws HearthStoneException {
        if (hero.getPlayer().haveTaunt()) {
            throw new HearthStoneException("There is taunt in front of you!");
        }
        hero.setHealth(hero.getHealth() - this.attack);

    }

    @Override
    public void found(Object object) throws HearthStoneException{
        if (object instanceof MinionCard) {
            if (((Card) object).getPlayer() == this.getPlayer()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((MinionCard) object);
                numberOfAttack--;
                numberOfAttackedMinion++;
            }
        } else if (object instanceof Hero) {
            if (((Hero) object).getPlayer() == this.getPlayer()) {
                throw new HearthStoneException("Choose enemy!");
            } else {
                this.attack((Hero) object);
                numberOfAttack--;
                numberOfAttackedHero++;
            }
        }
    }

    @Override
    public boolean pressed() {
        return numberOfAttack > 0;
    }
}