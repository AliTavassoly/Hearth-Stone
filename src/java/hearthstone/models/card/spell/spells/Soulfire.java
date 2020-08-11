package hearthstone.models.card.spell.spells;

import com.fasterxml.jackson.annotation.JsonProperty;
import hearthstone.models.behaviours.Character;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Soulfire extends SpellCard {
    @Transient
    @JsonProperty("damage")
    private int damage;

    public Soulfire() {
    }

    public Soulfire(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    public void setDamage(int damage){
        this.damage = damage;
    }
    public int getDamage(){
        return damage;
    }

    @Override
    public void doAbility() {
        HSServer.getInstance().createMouseWaiting(playerId, getCursorType(), this);
        // Mapper.updateBoard();
        HSServer.getInstance().updateGameRequest(playerId);
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.ATTACK;
    }

    @Override
    public void found(Object object) {
        if (object instanceof Character) {
            Character character = (Character) object;
            try {
                if (!character.isSpellSafe())
                    character.gotDamage(4);
            } catch (HearthStoneException ignore) { }

            try {
                //HSServer.getInstance().getPlayer(getPlayerId()).discardCard(DataTransform.getRandomCardFromHand(getPlayerId()).getCardGameId());
                HSServer.getInstance().getPlayer(getPlayerId()).discardCard(HSServer.getInstance().getPlayer(getPlayerId()).getFactory().getRandomCardFromHand().getCardGameId());
                // Mapper.updateBoard();
                HSServer.getInstance().updateGameRequest(playerId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
