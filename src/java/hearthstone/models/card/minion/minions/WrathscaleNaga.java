package hearthstone.models.card.minion.minions;

import hearthstone.models.behaviours.FriendlyMinionDies;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class WrathscaleNaga extends MinionCard implements FriendlyMinionDies {
    public WrathscaleNaga() {
    }

    public WrathscaleNaga(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack,
                          boolean isDeathRattle, boolean isTriggeredEffect,
                          boolean isSpellSafe, boolean isHeroPowerSafe, boolean isDivineShield,
                          boolean isTaunt, boolean isCharge, boolean isRush, MinionType minionType) {
        super(id, name, description, manaCost, heroType, rarity, cardType, health, attack,
                isDeathRattle, isTriggeredEffect, isSpellSafe, isHeroPowerSafe, isDivineShield,
                isTaunt, isCharge, isRush, minionType);
    }


    @Override
    public void friendlyMinionDies() {
        ArrayList<Card> land = HSServer.getInstance().getPlayer(enemyPlayerId).getLand();
        if (land.size() == 0)
            return;

        MinionCard card = HSServer.getInstance().getPlayer(getPlayerId()).getFactory().getRandomMinionFromLand();

        try {
            if (card != null) {
                card.gotDamage(3);

                HSServer.getInstance().updateGame(playerId);
            }
        } catch (HearthStoneException ignore) {
        }
    }
}
