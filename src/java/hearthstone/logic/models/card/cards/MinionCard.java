package hearthstone.logic.models.card.cards;

import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.hero.HeroType;

public class MinionCard extends Card {
    private int health, attack;

    public MinionCard(){ }

    public MinionCard(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack) {
        super(id, name, description, manaCost, heroType, rarity, cardType);
        this.health = health;
        this.attack = attack;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }
}
