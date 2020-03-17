package hearthstone.modules.cards;

import hearthstone.modules.heroes.HeroType;

public class SpellCard extends Card {
    public SpellCard() { }

    public SpellCard(String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(name, description, manaCost, heroType, rarity, cardType);
    }
}
