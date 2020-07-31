package hearthstone.models.card;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.passive.Passive;
import hearthstone.models.specialpower.SpecialHeroPower;
import hearthstone.util.AbstractAdapter;
import hearthstone.util.HearthStoneException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Card implements CardBehaviour {
    @Id
    private int id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private int manaCost;
    @Column
    private HeroType heroType;
    @Column
    private Rarity rarity;
    @Column
    private CardType cardType;
    @Column
    private int buyPrice, sellPrice;

    private int playerId;

    public Card() {
    }

    public Card(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manaCost = manaCost;
        this.heroType = heroType;
        this.cardType = cardType;

        sellPrice = buyPrice = 5;
    }

    public Card(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manaCost = manaCost;
        this.heroType = heroType;
        this.rarity = rarity;
        this.cardType = cardType;

        sellPrice = buyPrice = 5;
    }

    public void setPlayerId(int playerId){
        this.playerId = playerId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getManaCost() {
        return manaCost;
    }
    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public HeroType getHeroType() {
        return heroType;
    }
    public void setHeroType(HeroType heroType) {
        this.heroType = heroType;
    }

    public Rarity getRarity() {
        return rarity;
    }
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public CardType getCardType() {
        return cardType;
    }
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public int getBuyPrice() {
        return buyPrice;
    }
    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }
    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Card copy() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Card.class, new AbstractAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new AbstractAdapter<Hero>());
        gsonBuilder.registerTypeAdapter(Passive.class, new AbstractAdapter<Passive>());
        gsonBuilder.registerTypeAdapter(SpecialHeroPower.class, new AbstractAdapter<SpecialHeroPower>());

        Gson gson = gsonBuilder.create();
        return gson.fromJson(gson.toJson(this, Card.class), Card.class);
    }

    @Override
    public void found(Object object) throws HearthStoneException { }
}