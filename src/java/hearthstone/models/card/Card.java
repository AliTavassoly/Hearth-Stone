package hearthstone.models.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import hearthstone.server.data.ServerData;
import hearthstone.models.hero.HeroType;
import hearthstone.util.HearthStoneException;

import javax.persistence.*;

@Entity
public abstract class Card implements CardBehaviour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int allId;
    @Column
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

    @Transient
    private int playerId, cardGameId;

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

    public int getCardGameId(){
        return cardGameId;
    }
    public void setCardGameId(int cardGameId){
        this.cardGameId = cardGameId;
    }

    public Card copy() {
        ObjectMapper mapper = ServerData.getObjectCloneMapper();
        Card card = null;
        try {
            String json = mapper.writeValueAsString(this);
            card = mapper.readValue(json, Card.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        card.allId = 0;
        return card;
    }

    @Override
    public void found(Object object) throws HearthStoneException { }
}