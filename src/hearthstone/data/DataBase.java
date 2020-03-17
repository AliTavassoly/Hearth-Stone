package hearthstone.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hearthstone.data.bean.Account;
import hearthstone.data.bean.AccountCredential;
import hearthstone.modules.cards.*;
import hearthstone.modules.heroes.Hero;
import hearthstone.modules.heroes.HeroType;
import hearthstone.modules.heroes.Rogue;
import hearthstone.HearthStone;
import hearthstone.gamestuff.Market;
import hearthstone.util.InterfaceAdapter;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static hearthstone.HearthStone.*;

public class DataBase {
    public static Gson gson;

    public static void loadMinionCards() {
        MinionCard ali = new MinionCard("ali", "Kill all!", 10, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 3, 5);
        HearthStone.baseCards.put(ali.getId(), ali);
    }

    public static void loadWeaponCards() {
    }

    public static void loadSpellCards() {
        SpellCard rage = new SpellCard("rage", "speed", 2, HeroType.MAGE, Rarity.EPIC, CardType.SPELL);
        HearthStone.baseCards.put(rage.getId(), rage);
    }

    public static void loadHeroCards() {
    }

    public static void loadHeroes() {
        Rogue rogue = new Rogue("Warlock", "more health", 35, HeroType.WARLOCK);
        HearthStone.baseHeroes.put(rogue.getId(), rogue);
    }

    public static Map<String, AccountCredential> getCredentials() throws Exception {
        File json = new File(dataPath + "/credentials.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/credentials.json");
        Map<String, AccountCredential> ans = gson.fromJson(fileReader, new TypeToken<Map<String, AccountCredential>>() {
        }.getType());
        if (ans == null)
            return Data.getAccounts();
        return ans;
    }

    public static Account getAccount(int id) throws Exception {
        File json = new File(dataPath + "/accounts" + "/account_" + id + ".json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/accounts" + "/account_" + id + ".json");
        return gson.fromJson(fileReader, Account.class);
    }

    public static Map<String, Integer> getConfigs() throws Exception {
        File json = new File(dataPath + "/configs.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/configs.json");
        return gson.fromJson(fileReader, new TypeToken<Map<String, Integer>>() {
        }.getType());
    }

    public static Market getMarket() throws Exception {
        File json = new File(dataPath + "/market.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/market.json");
        Market ans = gson.fromJson(fileReader, Market.class);
        if (ans == null)
            return market;
        return ans;
    }

    public static void saveLog(String title, String description, String username) throws Exception {
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        FileWriter fileWriter = new FileWriter(dataPath + "/logs" + "/account_" + Data.getAccountId(username) + ".txt", true);
        String newLog = username + "@" + "HearthStone" + "\n";
        fileWriter.append(newLog);

        newLog = title + " @ " + ts + " -> " + description + "\n";
        fileWriter.append(newLog);

        fileWriter.close();
    }

    public static void saveLog(String title, String description) throws Exception {
        saveLog(title, description, currentAccount.getUsername());
    }

    public static void createAccountLog(String username) throws Exception {
        File logFile = new File(dataPath + "/logs" + "/account_" + Data.getAccountId(username) + ".txt");
        logFile.getParentFile().mkdirs();
        logFile.createNewFile();
        saveLog("Register", username, username);
    }

    public static void saveCurrentAccount() throws Exception {
        File json = new File(dataPath + "/accounts" + "/account_" + currentAccount.getId() + ".json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileWriter fileWriter = new FileWriter(dataPath + "/accounts" + "/account_" + currentAccount.getId() + ".json");
        gson.toJson(currentAccount, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void saveCredentials() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/credentials.json");
        gson.toJson(Data.getAccounts(), fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void saveMarket() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/market.json");
        gson.toJson(market, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    /*public static void saveConfifs(Map<String, Integer> s) throws Exception{
        FileWriter fileWriter = new FileWriter(dataPath + "/configs.json");
        gson.toJson(s, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }*/

    public static void save() throws Exception {
        saveCredentials();
        saveMarket();
        if (currentAccount != null)
            saveCurrentAccount();
    }

    public static void loadConfigs() throws Exception {
        var configs = getConfigs();
        maxCollectionSize = (int) configs.get("maxCollectionSize");
        maxDeckSize = (int) configs.get("maxDeckSize");
        initialCoins = (int) configs.get("initialCoins");
        maxNumberOfCard = (int) configs.get("maxNumberOfCard");
    }

    public static void loadMarket() throws Exception {
        market = getMarket();
    }

    public static void loadAccounts() throws Exception {
        Data.setAccounts(getCredentials());
    }

    public static void load() throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Card.class, new InterfaceAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new InterfaceAdapter<Hero>());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

        loadAccounts();
        loadMarket();
        loadConfigs();
    }
}