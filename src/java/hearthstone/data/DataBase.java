package hearthstone.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hearthstone.HearthStone;
import hearthstone.logic.models.Account;
import hearthstone.logic.models.AccountCredential;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.cards.*;
import hearthstone.logic.models.hero.*;
import hearthstone.logic.gamestuff.Market;
import hearthstone.logic.models.hero.heroes.*;
import hearthstone.util.AbstractAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static hearthstone.HearthStone.*;

public class DataBase {
    public static Gson gson;

    public static void loadCards() throws Exception{
        int id = 0;
        // Mage
        SpellCard polymorph = new SpellCard(id++, "Polymorph", "Transform a minion into a 1/1 sheep.", 4, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(polymorph.getId(), polymorph);

        SpellCard freezingPotion = new SpellCard(id++, "Freezing Potion", "Freeze an enemy.", 0, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(freezingPotion.getId(), freezingPotion);

        // Warlock
        MinionCard dreadscale = new MinionCard(id++, "Dreadscale", "At the end of your turn, deal 1 damage to all other minions.", 3, HeroType.WARLOCK, Rarity.LEGENDARY, CardType.MINIONCARD, 2, 4);
        HearthStone.baseCards.put(dreadscale.getId(), dreadscale);

        SpellCard soulfire = new SpellCard(id++, "Soulfire", "Deal 4 damage. Discard a random card.", 1, HeroType.WARLOCK, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(soulfire.getId(), soulfire);

        // Rogue
        SpellCard friendlySmith = new SpellCard(id++, "Friendly Smith", "Discover a weapon\n" + "from any class. Add it\n" + "to your Adventure Deck\n" + "with +2/+2.", 1, HeroType.ROGUE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(friendlySmith.getId(), friendlySmith);

        MinionCard labRecruiter = new MinionCard(id++, "Lab Recruiter", "Battlecry: Shuffle 3 copies of a friendly minion into your deck.", 2, HeroType.ROGUE, Rarity.COMMON, CardType.MINIONCARD, 2, 3);
        HearthStone.baseCards.put(labRecruiter.getId(), labRecruiter);

        // Paladin
        HeroPower theSilverHand = new HeroPower(id++, "The Silver Hand", "Hero Power\n" +
                "Summon two 1/1 Recruits.", 2, HeroType.PALADIN, CardType.HEROPOWER);
        HearthStone.baseCards.put(theSilverHand.getId(), theSilverHand);

        SpellCard gnomishArmyKnife = new SpellCard(id++, "Gnomish Army Knife", "Give a minion Charge,\n" +
                "Windfury, Divine Shield,\n" +
                "Lifesteal, Poisonous,\n" +
                "Taunt, and Stealth.", 5, HeroType.PALADIN, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(gnomishArmyKnife.getId(), gnomishArmyKnife);

        // Priest
        HeroPower heal = new HeroPower(id++, "Heal", "Hero Power\n" +
                "Restore 4 Health.", 2, HeroType.PRIEST, CardType.HEROPOWER);
        HearthStone.baseCards.put(heal.getId(), heal);

        MinionCard highPriestAmet = new MinionCard(id++, "High Priest Amet", "Whenever you summon a\n" +
                "minion, set its Health equal\n" +
                "to this minion's.", 4, HeroType.PRIEST, Rarity.LEGENDARY, CardType.MINIONCARD, 7, 2);
        HearthStone.baseCards.put(highPriestAmet.getId(), highPriestAmet);

        // All

        // Spell
        SpellCard blur = new SpellCard(id++, "Blur", "Your hero can't take damage this turn.", 0, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(blur.getId(), blur);

        SpellCard tracking = new SpellCard(id++, "Tracking", "Look at the top 3 cards of your deck. Draw one and discard the others.", 1, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(tracking.getId(), tracking);

        SpellCard sprint = new SpellCard(id++, "Sprint", "Draw 4 cards.", 7, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(sprint.getId(), sprint);

        SpellCard swarmOfLocusts = new SpellCard(id++, "Swarm of Locusts", "Summon seven 1/1 \n Locusts with Rush.", 6, HeroType.ALL, Rarity.RARE, CardType.SPELL);
        HearthStone.baseCards.put(swarmOfLocusts.getId(), swarmOfLocusts);

        SpellCard pharaohsBlessing = new SpellCard(id++, "Pharaoh's Blessing", "Give a minion +4/+4,\n Divine Shield, and Taunt.", 6, HeroType.ALL, Rarity.RARE, CardType.SPELL);
        HearthStone.baseCards.put(pharaohsBlessing.getId(), pharaohsBlessing);

        SpellCard bookOfSpecters = new SpellCard(id++, "Book of Specters", "Draw 3 cards. Discard \n any spells drawn.", 2, HeroType.ALL, Rarity.EPIC, CardType.SPELL);
        HearthStone.baseCards.put(bookOfSpecters.getId(), bookOfSpecters);

        // Minion
        MinionCard hulkingOverfiend = new MinionCard(id++, "Hulking Overfiend", "Rush. After this attacks and kills a minion, it may attack again.", 8, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 5, 10);
        HearthStone.baseCards.put(hulkingOverfiend.getId(), hulkingOverfiend);

        MinionCard wrathscaleNaga = new MinionCard(id++, "Wrathscale Naga", "After a friendly minion dies, deal 3 damage to a random enemy.", 3, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 1, 3);
        HearthStone.baseCards.put(wrathscaleNaga.getId(), wrathscaleNaga);

        MinionCard wrathspikeBrute = new MinionCard(id++, "Wrathspike Brute", "Taunt After this is attacked, deal 1 damage to all enemies.", 5, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 6, 2);
        HearthStone.baseCards.put(wrathspikeBrute.getId(), wrathspikeBrute);

        MinionCard pitCommander = new MinionCard(id++, "Pit Commander", "Taunt At the end of your turn, summon a Demon from your deck.", 9, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 9, 7);
        HearthStone.baseCards.put(pitCommander.getId(), pitCommander);

        MinionCard goldshireFootman = new MinionCard(id++, "Goldshire Footman", "Taunt", 1, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 2, 1);
        HearthStone.baseCards.put(goldshireFootman.getId(), goldshireFootman);

        MinionCard abomination = new MinionCard(id++, "Abomination", "Taunt. Deathrattle: Deal 2\ndamage to ALL characters.", 5, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 4, 4);
        HearthStone.baseCards.put(abomination.getId(), abomination);

        MinionCard sathrovarr = new MinionCard(id++, "Sathrovarr", "Battlecry: Choose a friendly\n minion. Add a copy of it to\n your hand, deck, and\n battlefield.", 9, HeroType.ALL, Rarity.LEGENDARY, CardType.MINIONCARD, 5, 5);
        HearthStone.baseCards.put(sathrovarr.getId(), sathrovarr);

        MinionCard tombWarden = new MinionCard(id++, "Tomb Warden", "Taunt \n Battlecry: Summon a copy\n of this minion.", 8, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 6, 3);
        HearthStone.baseCards.put(tombWarden.getId(), tombWarden);

        MinionCard securityRover = new MinionCard(id++, "Security Rover", "Whenever this minion\n" +
                "takes damage, summon a\n" +
                "2/3 Mech with [b]Taunt[/b].", 6, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 6, 2);
        HearthStone.baseCards.put(securityRover.getId(), securityRover);

        MinionCard curioCollector = new MinionCard(id++, "Curio Collector", "Whenever you draw a card, gain +1/+1.", 5, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 4, 4);
        HearthStone.baseCards.put(curioCollector.getId(), curioCollector);

        // Weapon
        WeaponCard warglaivesOfAzzinoth = new WeaponCard(id++, "Warglaives of Azzinoth", "After attacking a minion, your hero may attack again.", 5, HeroType.ALL, Rarity.EPIC, CardType.WEAPONCARD, 4, 3);
        HearthStone.baseCards.put(warglaivesOfAzzinoth.getId(), warglaivesOfAzzinoth);

        WeaponCard flamereaper = new WeaponCard(id++, "Flamereaper", "Also damages the minions next to whomever your hero attacks.", 7, HeroType.ALL, Rarity.EPIC, CardType.WEAPONCARD, 3, 4);
        HearthStone.baseCards.put(flamereaper.getId(), flamereaper);

        WeaponCard candleshot = new WeaponCard(id++, "Candleshot", "Your hero is Immune while attacking.", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 3, 1);
        HearthStone.baseCards.put(candleshot.getId(), candleshot);

        WeaponCard glaivezooka = new WeaponCard(id++, "Glaivezooka", "Battlecry: Give a random friendly minion +1 Attack.", 2, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 2, 2);
        HearthStone.baseCards.put(glaivezooka.getId(), glaivezooka);

        WeaponCard eaglehornBow = new WeaponCard(id++, "Eaglehorn Bow", "Whenever a friendly Secret is revealed, gain +1 Durability.", 3, HeroType.ALL, Rarity.RARE, CardType.WEAPONCARD, 2, 3);
        HearthStone.baseCards.put(eaglehornBow.getId(), eaglehornBow);

        WeaponCard desertSpear = new WeaponCard(id++, "Desert Spear", "After your hero attacks, summon a 1/1 Locust with Rush.", 3, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 3, 1);
        HearthStone.baseCards.put(desertSpear.getId(), desertSpear);

        WeaponCard battleAxe = new WeaponCard(id++, "Battle Axe", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 2, 2);
        HearthStone.baseCards.put(battleAxe.getId(), battleAxe);

        WeaponCard heavyAxe = new WeaponCard(id++, "Heavy Axe", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 3, 1);
        HearthStone.baseCards.put(heavyAxe.getId(), heavyAxe);

        WeaponCard wickedKnife = new WeaponCard(id++, "Wicked Knife", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 2, 1);
        HearthStone.baseCards.put(wickedKnife.getId(), wickedKnife);

        // Reward
        RewardCard learnDraconic = new RewardCard(id++, "Learn Draconic", "Sidequest: Spend\n" +
                "8 Mana on spells.\n" +
                "Reward: Summon a\n" +
                "6/6 Dragon.", 1, HeroType.ALL, Rarity.COMMON, CardType.REWARDCARD);
        HearthStone.baseCards.put(learnDraconic.getId(), learnDraconic);

        RewardCard strengthInNumbers = new RewardCard(id++, "Strength in Numbers", "Sidequest: Spend 10 Mana\n +" +
                " on minions.\n" +
                "Reward: Summon a\n" +
                " minion from your deck.", 1, HeroType.ALL, Rarity.COMMON, CardType.REWARDCARD);
        HearthStone.baseCards.put(strengthInNumbers.getId(), strengthInNumbers);

        //baseCards = getBaseCards();
    }

    public static void loadHeroes() throws Exception {
        int id = 0;
        Mage mage = new Mage(id++, "Mage", HeroType.MAGE, "witchers are good with spells!\nshe pays 2 mana less for spells!", 30, new ArrayList<Integer>(
                Arrays.asList(0, 1, 6, 9, 9, 10, 13, 16, 19)));
        HearthStone.baseHeroes.put(mage.getId(), mage);

        Warlock warlock = new Warlock(id++, "Warlock", HeroType.WARLOCK, "healthier than other!\nreduce 2 health and do somethings !\n", 35, new ArrayList<Integer>(
                Arrays.asList(2, 3, 7, 11, 11, 14, 14, 17)));
        HearthStone.baseHeroes.put(warlock.getId(), warlock);

        Rogue rogue = new Rogue(id++, "Rogue", HeroType.ROGUE, "the thief!\nwith 3 mana, she can steal one opponent card!", 30, new ArrayList<Integer>(
                Arrays.asList(4, 5, 8, 12, 15, 15, 18, 18)));
        HearthStone.baseHeroes.put(rogue.getId(), rogue);

        Paladin paladin = new Paladin(id++, "Paladin", HeroType.PALADIN, "is very good and strong", 30, new ArrayList<Integer>(
                Arrays.asList(6, 7)));
        HearthStone.baseHeroes.put(paladin.getId(), paladin);

        Priest priest = new Priest(id++, "Priest", HeroType.PRIEST, "", 30, new ArrayList<Integer>(
                Arrays.asList(8, 9)));
        HearthStone.baseHeroes.put(priest.getId(), priest);

        //baseHeroes = getBaseHeroes();
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

    public static Map<Integer, Card> getBaseCards() throws Exception {
        File json = new File(dataPath + "/base_cards.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/base_cards.json");
        Map<Integer, Card> ans = gson.fromJson(fileReader, new TypeToken<Map<Integer, Card>>() {
        }.getType());
        if (ans == null)
            return baseCards;
        return ans;
    }

    public static Map<Integer, Hero> getBaseHeroes() throws Exception {
        File json = new File(dataPath + "/base_heroes.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/base_heroes.json");
        Map<Integer, Hero> ans = gson.fromJson(fileReader, new TypeToken<Map<Integer, Hero>>() {
        }.getType());
        if (ans == null)
            return baseHeroes;
        return ans;
    }

    public static void saveCurrentAccount() throws Exception {
        File json = new File(dataPath + "/accounts" + "/account_" + currentAccount.getId() + ".json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileWriter fileWriter = new FileWriter(dataPath + "/accounts" + "/account_" + currentAccount.getId() + ".json");
        gson.toJson(currentAccount, Account.class, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void saveCredentials() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/credentials.json");
        gson.toJson(Data.getAccounts(), new TypeToken<Map<Integer, AccountCredential>>() {}.getType(), fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void saveMarket() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/market.json");
        gson.toJson(market, Market.class, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

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

    public static void save1() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/base_cards.json");
        gson.toJson(baseCards, new TypeToken<Map<Integer, Card>>() {}.getType(), fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void save2() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/base_heroes.json");
        gson.toJson(baseHeroes, new TypeToken<Map<Integer, Hero>>() {}.getType(), fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void load() throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Card.class, new AbstractAdapter<Card>());
        gsonBuilder.registerTypeAdapter(Hero.class, new AbstractAdapter<Hero>());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

        loadConfigs();

        loadCards();
        loadHeroes();
        save1();
        save2();
        loadAccounts();
        loadMarket();
    }
}