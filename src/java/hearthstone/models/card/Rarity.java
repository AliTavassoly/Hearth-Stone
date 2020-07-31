package hearthstone.models.card;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public enum Rarity {
    COMMON(0),
    RARE(1),
    EPIC(2),
    LEGENDARY(3);

    private final int rarityValue;

    Rarity(int rarityValue){
        this.rarityValue = rarityValue;
    }

    public int getValue(){
        return rarityValue;
    }
}
