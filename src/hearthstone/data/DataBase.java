package hearthstone.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hearthstone.data.bean.Account;
import hearthstone.data.bean.AccountCredential;
import hearthstone.model.cards.*;
import hearthstone.model.heroes.*;
import hearthstone.HearthStone;
import hearthstone.gamestuff.Market;
import hearthstone.util.InterfaceAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static hearthstone.HearthStone.*;

public class DataBase {
    public static Gson gson;

    public static void loadCards() {
        int id = 0;
        //Mage
        SpellCard polymorph = new SpellCard(id++, "Polymorph", "Transform a minion into a 1/1 sheep.", 4, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(polymorph.getId(), polymorph);

        SpellCard freezingPotion = new SpellCard(id++, "Freezing Potion", "Freeze an enemy.", 0, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(freezingPotion.getId(), freezingPotion);

        //Warlock
        MinionCard dreadscale = new MinionCard(id++, "Dreadscale", "At the end of your turn, deal 1 damage to all other minions.", 3, HeroType.WARLOCK, Rarity.LEGENDARY, CardType.MINIONCARD, 2, 4);
        HearthStone.baseCards.put(dreadscale.getId(), dreadscale);

        SpellCard soulfire = new SpellCard(id++, "Soulfire", "Deal 4 damage. Discard a random card.", 1, HeroType.WARLOCK, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(soulfire.getId(), soulfire);

        //Rogue
        SpellCard friendlySmith = new SpellCard(id++, "Friendly Smith", "Discover a weapon\n" + "from any class. Add it\n" + "to your Adventure Deck\n" + "with +2/+2.", 1, HeroType.ROGUE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(friendlySmith.getId(), friendlySmith);

        MinionCard labRecruiter = new MinionCard(id++, "Lab Recruiter", "Battlecry: Shuffle 3 copies of a friendly minion into your deck.", 2, HeroType.ROGUE, Rarity.COMMON, CardType.MINIONCARD, 2, 3);
        HearthStone.baseCards.put(labRecruiter.getId(), labRecruiter);

        //All
        SpellCard blur = new SpellCard(id++, "Blur", "Your hero can't take damage this turn.", 0, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(blur.getId(), blur);

        SpellCard chaosNova = new SpellCard(id++, "Chaos Nova", "Deal 4 damage to all minions.", 5, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(chaosNova.getId(), chaosNova);

        MinionCard hulkingOverfiend = new MinionCard(id++, "Hulking Overfiend", "Rush. After this attacks and kills a minion, it may attack again.", 8, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 5, 10);
        HearthStone.baseCards.put(hulkingOverfiend.getId(), hulkingOverfiend);

        MinionCard wrathscaleNaga = new MinionCard(id++, "Wrathscale Naga", "After a friendly minion dies, deal 3 damage to a random enemy.", 3, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 1, 3);
        HearthStone.baseCards.put(wrathscaleNaga.getId(), wrathscaleNaga);

    }

    public static void loadHeroes() throws Exception {
        int id = 0;
        Warlock warlock = new Warlock(id++, "Warlock", HeroType.WARLOCK, "healthier than other!\nreduce 2 health and do somethings !\n", 35, new ArrayList<Integer>(
                Arrays.asList(2, 3, 4)));
        HearthStone.baseHeroes.put(warlock.getId(), warlock);

        Mage mage = new Mage(id++, "Mage", HeroType.MAGE, "witchers are good with spells!\nshe pays 2 mana less for spells!", 30, new ArrayList<Integer>(
                Arrays.asList(2, 3, 4)));
        HearthStone.baseHeroes.put(mage.getId(), mage);

        Rogue rogue = new Rogue(id++, "Rogue", HeroType.ROGUE, "the thief!\nwith 3 mana, she can steal one opponent card!", 30, new ArrayList<Integer>(
                Arrays.asList(2, 3, 4)));
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

    public static Map<String, Object> getConfigs() throws Exception {
        File json = new File(dataPath + "/configs.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/configs.json");
        return gson.fromJson(fileReader, new TypeToken<Map<String, Object>>() {
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
        maxCollectionSize = ((Double) configs.get("maxCollectionSize")).intValue();
        maxDeckSize = ((Double) configs.get("maxDeckSize")).intValue();
        initialCoins = ((Double) configs.get("initialCoins")).intValue();
        maxNumberOfCard = ((Double) configs.get("maxNumberOfCard")).intValue();
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

        loadHeroes();
        loadHeroes();
        loadAccounts();
        loadMarket();
        loadConfigs();
    }
}