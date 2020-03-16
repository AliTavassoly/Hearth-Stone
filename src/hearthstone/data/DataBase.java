package hearthstone.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hearthstone.data.bean.Account;
import hearthstone.data.bean.AccountCredential;
import hearthstone.data.bean.cards.*;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.data.bean.heroes.HeroType;
import hearthstone.data.bean.heroes.Rogue;
import hearthstone.HearthStone;
import hearthstone.gamestuff.Market;
import hearthstone.util.InterfaceAdapter;

import java.io.*;
import java.util.Map;

import static hearthstone.HearthStone.*;

public class DataBase {
    public static Gson gson;

    public static void loadMinionCards() {
        MinionCard ali = new MinionCard("ali", "Kill all !", 10, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 3, 5);
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
        System.out.println("safafafaf");
        FileReader fileReader = new FileReader("credentials.json");
        System.out.println("safafafaf");
        return gson.fromJson(fileReader, new TypeToken<Map<String, AccountCredential>>() {}.getType());
    }

    public static Account getAccount(int id) throws Exception {
        File json = new File( "/accounts" + "/account_" + id + ".json");
        json.createNewFile();
        FileReader fileReader = new FileReader("/accounts" + "/account_" + id + ".json");
        return gson.fromJson(fileReader, Account.class);
    }

    public static Map<String, Object> getConfigs() throws Exception {
        File json = new File("/configs.json");
        json.createNewFile();
        FileReader fileReader = new FileReader("/configs.json");
        return gson.fromJson(fileReader, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static Market getMarket() throws Exception {
        File json = new File("/market.json");
        json.createNewFile();
        FileReader fileReader = new FileReader("/market.json");
        return gson.fromJson(fileReader, Market.class);
    }

    public static void saveCurrentAccount() throws Exception {
        saveAccount(currentAccount);
    }

    public static void saveAccount(Account account) throws Exception {
        FileWriter fileWriter = new FileWriter("/accounts" + "/account_" + account.getId() + ".json");

        gson.toJson(account, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void saveCredentials() throws Exception {
        System.out.println("hi");
        FileWriter fileWriter = new FileWriter("/credentials.json");
        System.out.println("hi");
        gson.toJson(Data.getAccounts(), fileWriter);
        System.out.println("hi");
        fileWriter.flush();
        fileWriter.close();
    }

    public static void saveMarket() throws Exception {
        FileWriter fileWriter = new FileWriter("/market.json");
        gson.toJson(market, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void save() throws Exception {
        saveCredentials();
        saveMarket();
        getCredentials().size();
        //saveCurrentAccount();
    }

    public static void load() throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Card.class, new InterfaceAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new InterfaceAdapter<Hero>());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

        Data.setAccounts(getCredentials());

        /*var configs = getConfigs();
        maxCollectionSize = (int) configs.get("maxCollectionSize");
        maxDeckSize = (int) configs.get("maxDeckSize");
        initialCoins = (int) configs.get("initialCoins");
        maxNumberOfCard = (int) configs.get("maxNumberOfCard");*/

        market = getMarket();
    }
}