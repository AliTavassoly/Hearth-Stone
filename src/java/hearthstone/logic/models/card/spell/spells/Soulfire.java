package hearthstone.logic.models.card.spell.spells;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.Character;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.spell.SpellCard;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

public class Soulfire extends SpellCard {
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
        Mapper.getInstance().makeNewMouseWaiting(getCursorType(), this);
        Mapper.getInstance().updateBoard();
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
                Mapper.getInstance().discardCard(getPlayerId(), DataTransform.getInstance().getRandomCardFromHand(getPlayerId()));
                Mapper.getInstance().updateBoard();
            } catch (Exception ignore) {
            }
        }
    }
}
