package hearthstone.models.card.minion.minions;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.behaviours.DeathRattle;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;

@Entity
public class Abomination extends MinionCard implements DeathRattle {
    public Abomination(){ }

    public Abomination(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                            boolean isDeathRattle, boolean isTriggeredEffect,
                       boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                            boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }

    @Override
    public void deathRattle() {
        Hero hero = DataTransform.getHero(DataTransform.getEnemyId(getPlayerId()));

        try {
            //Mapper.damage(2, hero);
            hero.gotDamage(2);
            Mapper.updateBoard();
        } catch (HearthStoneException ignore) { }

        for(Card card: DataTransform.getLand(DataTransform.getEnemyId(getPlayerId()))){
            try {
                //Mapper.damage(2, (MinionCard) card, false);
                ((MinionCard) card).gotDamage(2);
            } catch (HearthStoneException ignore){ }
        }

        Mapper.updateBoard();
    }
}
