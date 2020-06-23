package hearthstone.models.card;

public enum Rarity {
    COMMON(0),
    RARE(1),
    EPIC(2),
    LEGENDARY(3);

    private final int rarityValue;

    private Rarity(int rarityValue){
        this.rarityValue = rarityValue;
    }

    public int getValue(){
        return rarityValue;
    }
}
