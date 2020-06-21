package hearthstone.models.card.minion.minions;

import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;

public class GoldshireFootman extends MinionCard {

    public GoldshireFootman(){ }

    public GoldshireFootman(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                            boolean isDeathRattle, boolean isTriggeredEffect, boolean isSpellDamage, boolean isDivineShield,
                            boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellDamage, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public boolean attack(MinionCard minionCard) {
        if(numberOfAttack == 0)
            return false;

        minionCard.setHealth(minionCard.getHealth() - this.attack);
        this.health -= minionCard.getAttack();
        numberOfAttack--;

        return true;
    }

    @Override
    public boolean attack(Hero hero) {
        if(hero.getPlayer().haveTaunt())
            return false;
        if(numberOfAttack == 0)
            return false;
        hero.setHealth(hero.getHealth() - this.attack);
        numberOfAttack--;
        return true;
    }

    @Override
    public boolean found(Object object) {
        if(object instanceof MinionCard){
            if(((Card)object).getPlayer() == this.getPlayer()){
                return false;
            } else {
                return this.attack((MinionCard) object);
            }
        } else if(object instanceof Hero){
            if(((Hero) object).getPlayer() == this.getPlayer()){
                return false;
            } else {
                return this.attack((Hero)object);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean pressed() {
        return true;
    }
}
