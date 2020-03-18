package hearthstone.model.cards;

import hearthstone.model.heroes.HeroType;

public class SpellCard extends Card {
    public SpellCard() { }

    public SpellCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }
}
