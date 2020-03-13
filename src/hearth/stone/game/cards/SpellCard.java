package hearth.stone.game.cards;

import hearth.stone.game.heroes.HeroType;

public class SpellCard extends Card {
    public SpellCard(String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(name, description, manaCost, heroType, rarity, cardType);
    }
}
