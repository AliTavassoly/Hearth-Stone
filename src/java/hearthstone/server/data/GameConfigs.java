package hearthstone.server.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GameConfigs {
    public static String dataPath = "data";

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

    private static Map<String, Integer> getConfigs() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(dataPath + "/game_configs.json");
        file.getParentFile().mkdirs();
        file.createNewFile();
        return mapper.readValue(file, new TypeReference<HashMap<String, Integer>>() {});
    }

    public static void loadConfigs() throws Exception{
        var gameConfigs = getConfigs();
        setConfigs(gameConfigs);
    }
}

