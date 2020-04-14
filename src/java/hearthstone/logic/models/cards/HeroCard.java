package hearthstone.logic.models.cards;

import hearthstone.logic.models.heroes.HeroType;

public class HeroCard extends Card implements Cloneable{
    public HeroCard(){

    }
    public HeroCard(int id,  String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }
}