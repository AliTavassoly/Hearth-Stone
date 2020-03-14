package hearthstone.data.bean.cards;

import hearthstone.data.bean.heroes.HeroType;

public abstract class Card {
    private static int lastId;
    private int id;
    private String name;
    private String description;
    private int manaCost;
    private HeroType heroType;
    private Rarity rarity;
    private CardType cardType;
    private int buyCost, sellCost;

    Card(String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        id = lastId;
        this.name = name;
        this.description = description;
        this.manaCost = manaCost;
        this.heroType = heroType;
        this.rarity = rarity;
        this.cardType = cardType;

        lastId++;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getId() {
        return id;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public CardType getType() {
        return cardType;
    }

    public String getName(){
        return name;
    }

    public int getBuyCost(){
        return buyCost;
    }
    public void setBuyCost(int buyCost){
        this.buyCost = buyCost;
    }

    public int getSellCost(){
        return sellCost;
    }
    public void setSellCost(int sellCost){
        this.sellCost = sellCost;
    }

}
