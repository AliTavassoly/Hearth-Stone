package hearthstone.gui;

import java.util.Map;

public class SizeConfigs {
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

    public static int settingsWidth;
    public static int settingsHeight;

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

    static public void setConfigs(Map<String, Object> configs){
        // General
        credentialFrameWidth = ((Double) configs.get("credentialFrameWidth")).intValue();
        credentialFrameHeight = ((Double) configs.get("credentialFrameHeight")).intValue();

        smallButtonWidth = ((Double) configs.get("smallButtonWidth")).intValue();
        smallButtonHeight = ((Double) configs.get("smallButtonHeight")).intValue();

        medButtonWidth = ((Double) configs.get("medButtonWidth")).intValue();
        medButtonHeight = ((Double) configs.get("medButtonHeight")).intValue();

        largeButtonWidth = ((Double) configs.get("largeButtonWidth")).intValue();
        largeButtonHeight = ((Double) configs.get("largeButtonHeight")).intValue();

        gameFrameWidth = ((Double) configs.get("gameFrameWidth")).intValue();
        gameFrameHeight = ((Double) configs.get("gameFrameHeight")).intValue();

        iconWidth = ((Double) configs.get("iconWidth")).intValue();
        iconHeight = ((Double) configs.get("iconHeight")).intValue();

        mainMenuLogoWidth = ((Double) configs.get("mainMenuLogoWidth")).intValue();
        mainMenuLogoHeight = ((Double) configs.get("mainMenuLogoHeight")).intValue();

        smallCardWidth = ((Double) configs.get("smallCardWidth")).intValue();
        smallCardHeight = ((Double) configs.get("smallCardHeight")).intValue();

        medCardWidth = ((Double) configs.get("medCardWidth")).intValue();
        medCardHeight = ((Double) configs.get("medCardHeight")).intValue();

        marketListWidth = ((Double) configs.get("marketListWidth")).intValue();
        marketListHeight = ((Double) configs.get("marketListHeight")).intValue();

        statusListWidth = ((Double) configs.get("statusListWidth")).intValue();
        statusListHeight = ((Double) configs.get("statusListHeight")).intValue();

        deckSelectionListWidth = ((Double) configs.get("deckSelectionListWidth")).intValue();
        deckSelectionListHeight = ((Double) configs.get("deckSelectionListHeight")).intValue();

        bigHeroWidth = ((Double) configs.get("bigHeroWidth")).intValue();
        bigHeroHeight = ((Double) configs.get("bigHeroHeight")).intValue();

        bigCircleHeroWidth = ((Double) configs.get("bigCircleHeroWidth")).intValue();
        bigCircleHeroHeight = ((Double) configs.get("bigCircleHeroHeight")).intValue();

        bigHeroDetailWidth = ((Double) configs.get("bigHeroDetailWidth")).intValue();
        bigHeroDetailHeight = ((Double) configs.get("bigHeroDetailHeight")).intValue();

        heroesListWidth = ((Double) configs.get("heroesListWidth")).intValue();
        heroesListHeight = ((Double) configs.get("heroesListHeight")).intValue();

        smallGemButtonWidth = ((Double) configs.get("smallGemButtonWidth")).intValue();
        smallGemButtonHeight = ((Double) configs.get("smallGemButtonHeight")).intValue();

        bigGemButtonWidth = ((Double) configs.get("bigGemButtonWidth")).intValue();
        bigGemButtonHeight = ((Double) configs.get("bigGemButtonHeight")).intValue();

        deckWidth = ((Double) configs.get("deckWidth")).intValue();
        deckHeight = ((Double) configs.get("deckHeight")).intValue();

        dialogWidth = ((Double) configs.get("dialogWidth")).intValue();
        dialogHeight = ((Double) configs.get("dialogHeight")).intValue();

        arrangementListWidth = ((Double) configs.get("arrangementListWidth")).intValue();
        arrangementListHeight = ((Double) configs.get("arrangementListHeight")).intValue();

        errorWidth = ((Double) configs.get("errorWidth")).intValue();
        errorHeight = ((Double) configs.get("errorHeight")).intValue();

        settingsWidth = ((Double) configs.get("settingsWidth")).intValue();
        settingsHeight = ((Double) configs.get("settingsHeight")).intValue();

        numberOfCardFlagWidth = ((Double) configs.get("numberOfCardFlagWidth")).intValue();
        numberOfCardFlagHeight = ((Double) configs.get("numberOfCardFlagHeight")).intValue();

        marketInfoHeight = ((Double) configs.get("marketInfoHeight")).intValue();

        // GameBoard

        endTurnButtonWidth = ((Double) configs.get("endTurnButtonWidth")).intValue();
        endTurnButtonHeight = ((Double) configs.get("endTurnButtonHeight")).intValue();

        manaWidth = ((Double) configs.get("manaWidth")).intValue();
        manaHeight = ((Double) configs.get("manaHeight")).intValue();

        medHeroWidth = ((Double) configs.get("medHeroWidth")).intValue();
        medHeroHeight = ((Double) configs.get("medHeroHeight")).intValue();

        healthWidth = ((Double) configs.get("healthWidth")).intValue();
        healthHeight = ((Double) configs.get("healthHeight")).intValue();

        heroPowerWidth = ((Double) configs.get("heroPowerWidth")).intValue();
        heroPowerHeight = ((Double) configs.get("heroPowerHeight")).intValue();

        weaponWidth = ((Double) configs.get("weaponWidth")).intValue();
        weaponHeight = ((Double) configs.get("weaponHeight")).intValue();

        weaponDetailWidth = ((Double) configs.get("weaponDetailWidth")).intValue();
        weaponDetailHeight = ((Double) configs.get("weaponDetailHeight")).intValue();
    }
}
