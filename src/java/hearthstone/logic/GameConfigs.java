package hearthstone.logic;

import java.util.Map;

public class GameConfigs {
    public static int maxCardInCollection;
    public static int initialCoins;
    public static int maxCardInDeck;
    public static int maxManaInGame;
    public static int maxCardInHand;
    public static int maxCardInLand;
    public static int maxCardOfOneType;
    public static int maxNumberOfDeck;

    public static void setConfigs(Map<String, Object> configs){
        maxCardInCollection = ((Double) configs.get("maxCardInCollection")).intValue();
        maxCardInDeck = ((Double) configs.get("maxCardInDeck")).intValue();
        initialCoins = ((Double) configs.get("initialCoins")).intValue();
        maxCardInHand = ((Double) configs.get("maxCardInHand")).intValue();
        maxCardInLand = ((Double) configs.get("maxCardInLand")).intValue();
        maxManaInGame = ((Double) configs.get("maxManaInGame")).intValue();
        maxCardOfOneType = ((Double) configs.get("maxCardOfOneType")).intValue();
        maxNumberOfDeck= ((Double) configs.get("maxNumberOfDeck")).intValue();
    }
}
