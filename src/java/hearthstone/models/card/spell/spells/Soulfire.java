package hearthstone.models.card.spell.spells;

import hearthstone.Mapper;
import hearthstone.models.behaviours.Character;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Soulfire extends SpellCard {
    @Transient
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
        Mapper.makeNewMouseWaiting(getCursorType(), this);
        Mapper.updateBoard();
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
                //Mapper.getPlayer(getPlayerId()).discardCard(DataTransform.getRandomCardFromHand(getPlayerId()).getCardGameId());
                Mapper.getPlayer(getPlayerId()).discardCard(Mapper.getPlayer(getPlayerId()).getFactory().getRandomCardFromHand().getCardGameId());
                Mapper.updateBoard();
            } catch (Exception ignore) {
            }
        }
    }
}
