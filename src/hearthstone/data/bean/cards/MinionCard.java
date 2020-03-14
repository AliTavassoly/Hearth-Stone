package hearthstone.data.bean.cards;

import hearthstone.data.bean.heroes.HeroType;

public class MinionCard extends Card {
    private int health, attack;

    public MinionCard(String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType, int health, int attack) {
        super(name, description, manaCost, heroType, rarity, cardType);
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
