package hearthstone.logic.models.card.minion.minions;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.interfaces.DeathRattle;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

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
        Hero hero = DataTransform.getInstance().getHero(DataTransform.getInstance().getEnemyId(getPlayerId()));

        try {
            Mapper.getInstance().damage(2, hero);
        } catch (HearthStoneException ignore) { }

        for(Card card: DataTransform.getInstance().getLand(DataTransform.getInstance().getEnemyId(getPlayerId()))){
            try {
                Mapper.getInstance().damage(2, (MinionCard) card, false);
            } catch (HearthStoneException ignore){ }
        }

        Mapper.getInstance().updateBoard();
    }
}
