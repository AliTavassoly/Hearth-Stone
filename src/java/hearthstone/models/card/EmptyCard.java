package hearthstone.models.card;

import hearthstone.models.hero.HeroType;

import javax.persistence.Entity;

@Entity
public class EmptyCard extends Card {
    public EmptyCard() { }

    public EmptyCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }
}
