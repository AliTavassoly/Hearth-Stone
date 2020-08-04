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
    public static int initialPassives;
    public static int initialDiscardCards;
    public static int initialCardsBack;

    public static void setConfigs(Map<String, Integer> configs) {
        maxCardInCollection = configs.get("maxCardInCollection");
        maxCardInDeck = configs.get("maxCardInDeck");
        initialCoins = configs.get("initialCoins");
        maxCardInHand = configs.get("maxCardInHand");
        maxCardInLand = configs.get("maxCardInLand");
        maxManaInGame = configs.get("maxManaInGame");
        maxCardOfOneType = configs.get("maxCardOfOneType");
        maxNumberOfDeck = configs.get("maxNumberOfDeck");
        initialPassives = configs.get("initialPassives");
        initialDiscardCards = configs.get("initialDiscardCards");
        initialCardsBack = configs.get("initialCardsBack");
    }
}
