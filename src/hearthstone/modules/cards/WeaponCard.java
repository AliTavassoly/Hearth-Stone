package hearthstone.modules.cards;

import hearthstone.modules.heroes.HeroType;

public class WeaponCard extends Card {
    public WeaponCard() { }

    public WeaponCard(String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(name, description, manaCost, heroType, rarity, cardType);
    }
}
