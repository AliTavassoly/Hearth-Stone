package hearthstone.logic.models.card.weapon.weapons;

import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.interfaces.WhileAlive;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.logic.models.hero.HeroType;

public class Candleshot extends WeaponCard implements WhileAlive {
    public Candleshot() {
    }

    public Candleshot(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int durability, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType, durability, attack);
    }

    @Override
    public void startTurnBehave() {
        super.startTurnBehave();

        doWhileAlive();
    }

    @Override
    public void doWhileAlive() {
        if (canAttack())
            Mapper.getInstance().setHeroImmune(getPlayerId(), true);
    }
}
