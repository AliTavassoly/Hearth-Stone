package hearthstone.model.cards;

import hearthstone.model.heroes.HeroType;

public class HeroCard extends Card{
    public HeroCard(){

    }
    public HeroCard(int id,  String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }
}
