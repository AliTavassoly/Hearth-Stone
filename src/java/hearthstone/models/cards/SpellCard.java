package hearthstone.models.cards;

import hearthstone.models.heroes.HeroType;

public class SpellCard extends Card implements Cloneable{
    public SpellCard() { }

    public SpellCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }
}
