package hearthstone.shared;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hearthstone.client.data.ClientData;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GUIConfigs {
    private static String dataPath = "data";
    // General
    public static int credentialFrameWidth;
    public static int credentialFrameHeight;

    public static int smallButtonWidth;
    public static int smallButtonHeight;

    public static int medButtonWidth;
    public static int medButtonHeight;

    public static int largeButtonWidth;
    public static int largeButtonHeight;

    public static int gameFrameWidth;
    public static int gameFrameHeight;

    public static int iconWidth;
    public static int iconHeight;

    public static int mainMenuLogoWidth;
    public static int mainMenuLogoHeight;

    public static int smallCardWidth;
    public static int smallCardHeight;

    public static int smallCardWidthOnLand;
    public static int smallCardHeightOnLand;

    public static int medCardWidth;
    public static int medCardHeight;

    public static int marketListWidth;
    public static int marketListHeight;

    public static int statusListWidth;
    public static int statusListHeight;

    public static int deckSelectionListWidth;
    public static int deckSelectionListHeight;

    public static int bigHeroWidth;
    public static int bigHeroHeight;

    public static int bigCircleHeroWidth;
    public static int bigCircleHeroHeight;

    public static int bigHeroDetailWidth;
    public static int bigHeroDetailHeight;

    public static int heroesListWidth;
    public static int heroesListHeight;

    public static int smallGemButtonWidth;
    public static int smallGemButtonHeight;

    public static int bigGemButtonWidth;
    public static int bigGemButtonHeight;

    public static int deckWidth;
    public static int deckHeight;

    public static int dialogWidth;
    public static int dialogHeight;

    public static int arrangementListWidth;
    public static int arrangementListHeight;

    public static int errorWidth;
    public static int errorHeight;

    public static int volumeSettingsWidth;
    public static int volumeSettingsHeight;

    public static int numberOfCardFlagWidth;
    public static int numberOfCardFlagHeight;

    public static int marketInfoHeight;

    // Game Board
    public static int endTurnButtonWidth;
    public static int endTurnButtonHeight;

    public static int manaWidth;
    public static int manaHeight;

    public static int medHeroWidth;
    public static int medHeroHeight;

    public static int healthWidth;
    public static int healthHeight;

    public static int heroPowerWidth;
    public static int heroPowerHeight;

    public static int weaponWidth;
    public static int weaponHeight;

    public static int weaponDetailWidth;
    public static int weaponDetailHeight;

    public static int endTurnFireWidth;
    public static int endTurnFireHeight;

    public static int endTurnRopeWidth;
    public static int endTurnRopeHeight;

    public static int minionTypeWidth;
    public static int minionTypeHeight;

    public static int inGameErrorWidth;
    public static int inGameErrorHeight;

    public static int progressManaWidth;
    public static int progressManaHeight;

    public static int bigRedMarkWidth;
    public static int bigRedMarkHeight;

    public static int accountInfoWidth;
    public static int accountInfoHeight;

    public static int rankingListWidth;
    public static int rankingListHeight;

    public static int gamesListWidth;
    public static int gamesListHeight;

    public static int gameInfoWidth;
    public static int gameInfoHeight;

    static public void setConfigs(Map<String, Integer> configs) {
        // General
        credentialFrameWidth = configs.get("credentialFrameWidth");
        credentialFrameHeight = configs.get("credentialFrameHeight");

        smallButtonWidth = configs.get("smallButtonWidth");
        smallButtonHeight = configs.get("smallButtonHeight");

        medButtonWidth = configs.get("medButtonWidth");
        medButtonHeight = configs.get("medButtonHeight");

        largeButtonWidth = configs.get("largeButtonWidth");
        largeButtonHeight = configs.get("largeButtonHeight");

        gameFrameWidth = configs.get("gameFrameWidth");
        gameFrameHeight = configs.get("gameFrameHeight");

        iconWidth = configs.get("iconWidth");
        iconHeight = configs.get("iconHeight");

        mainMenuLogoWidth = configs.get("mainMenuLogoWidth");
        mainMenuLogoHeight = configs.get("mainMenuLogoHeight");

        smallCardWidth = configs.get("smallCardWidth");
        smallCardHeight = configs.get("smallCardHeight");

        smallCardWidthOnLand = configs.get("smallCardWidthOnLand");
        smallCardHeightOnLand = configs.get("smallCardHeightOnLand");

        medCardWidth = configs.get("medCardWidth");
        medCardHeight = configs.get("medCardHeight");

        marketListWidth = configs.get("marketListWidth");
        marketListHeight = configs.get("marketListHeight");

        statusListWidth = configs.get("statusListWidth");
        statusListHeight = configs.get("statusListHeight");

        deckSelectionListWidth = configs.get("deckSelectionListWidth");
        deckSelectionListHeight = configs.get("deckSelectionListHeight");

        bigHeroWidth = configs.get("bigHeroWidth");
        bigHeroHeight = configs.get("bigHeroHeight");

        bigCircleHeroWidth = configs.get("bigCircleHeroWidth");
        bigCircleHeroHeight = configs.get("bigCircleHeroHeight");

        bigHeroDetailWidth = configs.get("bigHeroDetailWidth");
        bigHeroDetailHeight = configs.get("bigHeroDetailHeight");

        heroesListWidth = configs.get("heroesListWidth");
        heroesListHeight = configs.get("heroesListHeight");

        smallGemButtonWidth = configs.get("smallGemButtonWidth");
        smallGemButtonHeight = configs.get("smallGemButtonHeight");

        bigGemButtonWidth = configs.get("bigGemButtonWidth");
        bigGemButtonHeight = configs.get("bigGemButtonHeight");

        deckWidth = configs.get("deckWidth");
        deckHeight = configs.get("deckHeight");

        dialogWidth = configs.get("dialogWidth");
        dialogHeight = configs.get("dialogHeight");

        arrangementListWidth = configs.get("arrangementListWidth");
        arrangementListHeight = configs.get("arrangementListHeight");

        errorWidth = configs.get("errorWidth");
        errorHeight = configs.get("errorHeight");

        volumeSettingsWidth = configs.get("volumeSettingsWidth");
        volumeSettingsHeight = configs.get("volumeSettingsHeight");

        numberOfCardFlagWidth = configs.get("numberOfCardFlagWidth");
        numberOfCardFlagHeight = configs.get("numberOfCardFlagHeight");

        marketInfoHeight = configs.get("marketInfoHeight");

        // GameBoard
        endTurnButtonWidth = configs.get("endTurnButtonWidth");
        endTurnButtonHeight = configs.get("endTurnButtonHeight");

        manaWidth = configs.get("manaWidth");
        manaHeight = configs.get("manaHeight");

        medHeroWidth = configs.get("medHeroWidth");
        medHeroHeight = configs.get("medHeroHeight");

        healthWidth = configs.get("healthWidth");
        healthHeight = configs.get("healthHeight");

        heroPowerWidth = configs.get("heroPowerWidth");
        heroPowerHeight = configs.get("heroPowerHeight");

        weaponWidth = configs.get("weaponWidth");
        weaponHeight = configs.get("weaponHeight");

        weaponDetailWidth = configs.get("weaponDetailWidth");
        weaponDetailHeight = configs.get("weaponDetailHeight");

        endTurnFireWidth = configs.get("endTurnFireWidth");
        endTurnFireHeight = configs.get("endTurnFireHeight");

        endTurnRopeWidth = configs.get("endTurnRopeWidth");
        endTurnRopeHeight = configs.get("endTurnRopeHeight");

        minionTypeWidth = configs.get("minionTypeWidth");
        minionTypeHeight = configs.get("minionTypeHeight");

        inGameErrorWidth = configs.get("inGameErrorWidth");
        inGameErrorHeight = configs.get("inGameErrorHeight");

        progressManaWidth = configs.get("progressManaWidth");
        progressManaHeight = configs.get("progressManaHeight");

        bigRedMarkWidth = configs.get("bigRedMarkWidth");
        bigRedMarkHeight = configs.get("bigRedMarkHeight");

        accountInfoWidth = configs.get("accountInfoWidth");
        accountInfoHeight = configs.get("accountInfoHeight");

        gameInfoWidth = configs.get("gameInfoWidth");
        gameInfoHeight = configs.get("gameInfoHeight");

        rankingListWidth = configs.get("rankingListWidth");
        rankingListHeight = configs.get("rankingListHeight");

        gamesListWidth = configs.get("gamesListWidth");
        gamesListHeight = configs.get("gamesListHeight");
    }

    private static Map<String, Integer> getConfigs() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(dataPath + "/gui_configs.json");
        file.getParentFile().mkdirs();
        file.createNewFile();
        return mapper.readValue(file, new TypeReference<HashMap<String, Integer>>() {});
    }

    public static void loadConfigs() throws Exception{
        var guiConfigs = getConfigs();
        setConfigs(guiConfigs);
    }
}
