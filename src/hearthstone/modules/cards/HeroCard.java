package hearthstone.modules.cards;

import hearthstone.modules.heroes.HeroType;

public class HeroCard extends Card{
    public HeroCard(){

    }
    public HeroCard(String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(name, description, manaCost, heroType, rarity, cardType);
    }
}
