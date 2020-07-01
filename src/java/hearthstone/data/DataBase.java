package hearthstone.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hearthstone.HearthStone;
import hearthstone.gui.SizeConfigs;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.gamestuff.Market;
import hearthstone.logic.models.Account;
import hearthstone.logic.models.AccountCredential;
import hearthstone.logic.models.Passive;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.Rarity;
import hearthstone.logic.models.card.heropower.heropowers.Heal;
import hearthstone.logic.models.card.heropower.heropowers.TheSilverHand;
import hearthstone.logic.models.card.minion.MinionType;
import hearthstone.logic.models.card.minion.minions.*;
import hearthstone.logic.models.card.reward.rewards.LearnDraconic;
import hearthstone.logic.models.card.reward.rewards.StrengthInNumbers;
import hearthstone.logic.models.card.spell.spells.*;
import hearthstone.logic.models.card.weapon.weapons.*;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.hero.heroes.*;
import hearthstone.util.AbstractAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static hearthstone.HearthStone.*;

public class DataBase {
    public static Gson gson;

    private static void loadCards() throws Exception{
        int id = 0;
        // Mage
        Polymorph polymorph = new Polymorph(id++, "Polymorph", "Transform a minion into a 1/1 sheep.", 4, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(polymorph.getId(), polymorph);

        FreezingPotion freezingPotion = new FreezingPotion(id++, "Freezing Potion", "Freeze an enemy.", 0, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(freezingPotion.getId(), freezingPotion);

        // Warlock
        Dreadscale dreadscale = new Dreadscale(id++, "Dreadscale", "At the end of your turn, deal 1 damage to all other minions.", 3, HeroType.WARLOCK, Rarity.LEGENDARY, CardType.MINIONCARD, 2, 4,
                false, false, false, false, false, false, false, MinionType.BEAST);
        HearthStone.baseCards.put(dreadscale.getId(), dreadscale);

        Soulfire soulfire = new Soulfire(id++, "Soulfire", "Deal 4 damage. Discard a random card.", 1, HeroType.WARLOCK, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(soulfire.getId(), soulfire);

        // Rogue
        FriendlySmith friendlySmith = new FriendlySmith(id++, "Friendly Smith", "Discover a weapon\n" + "from any class. Add it\n" + "to your Adventure Deck\n" + "with +2/+2.", 1, HeroType.ROGUE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(friendlySmith.getId(), friendlySmith);

        LabRecruiter labRecruiter = new LabRecruiter(id++, "Lab Recruiter", "Battlecry: Shuffle 3 copies of a friendly minion into your deck.", 2, HeroType.ROGUE, Rarity.COMMON, CardType.MINIONCARD, 2, 3,
        false, false, false, false, false, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(labRecruiter.getId(), labRecruiter);

        // Paladin
        TheSilverHand theSilverHand = new TheSilverHand(id++, "The Silver Hand", "Hero Power\n" +
                "Summon two 1/1 Recruits.", 2, HeroType.PALADIN, CardType.HEROPOWER);
        HearthStone.baseCards.put(theSilverHand.getId(), theSilverHand);

        GnomishArmyKnife gnomishArmyKnife = new GnomishArmyKnife(id++, "Gnomish Army Knife", "Give a minion Charge,\n" +
                "Windfury, Divine Shield,\n" +
                "Lifesteal, Poisonous,\n" +
                "Taunt, and Stealth.", 5, HeroType.PALADIN, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(gnomishArmyKnife.getId(), gnomishArmyKnife);

        // Priest
        Heal heal = new Heal(id++, "Heal", "Hero Power\n" +
                "Restore 4 Health.", 2, HeroType.PRIEST, CardType.HEROPOWER);
        HearthStone.baseCards.put(heal.getId(), heal);

        HighPriestAmet highPriestAmet = new HighPriestAmet(id++, "High Priest Amet", "Whenever you summon a\n" +
                "minion, set its Health equal\n" +
                "to this minion's.", 4, HeroType.PRIEST, Rarity.LEGENDARY, CardType.MINIONCARD, 7, 2,
                false, false, false, false, false, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(highPriestAmet.getId(), highPriestAmet);

        // All

        // Spell
        Blur blur = new Blur(id++, "Blur", "Your hero can't take damage this turn.", 0, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(blur.getId(), blur);

        Tracking tracking = new Tracking(id++, "Tracking", "Look at the top 3 cards of your deck. Draw one and discard the others.", 1, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(tracking.getId(), tracking);

        Sprint sprint = new Sprint(id++, "Sprint", "Draw 4 cards.", 7, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(sprint.getId(), sprint);

        SwarmOfLocusts swarmOfLocusts = new SwarmOfLocusts(id++, "Swarm of Locusts", "Summon seven 1/1 \n Locusts with Rush.", 6, HeroType.ALL, Rarity.RARE, CardType.SPELL);
        HearthStone.baseCards.put(swarmOfLocusts.getId(), swarmOfLocusts);

        PharaohsBlessing pharaohsBlessing = new PharaohsBlessing(id++, "Pharaoh's Blessing", "Give a minion +4/+4,\n Divine Shield, and Taunt.", 6, HeroType.ALL, Rarity.RARE, CardType.SPELL);
        HearthStone.baseCards.put(pharaohsBlessing.getId(), pharaohsBlessing);

        BookOfSpecters bookOfSpecters = new BookOfSpecters(id++, "Book of Specters", "Draw 3 cards. Discard \n any spells drawn.", 2, HeroType.ALL, Rarity.EPIC, CardType.SPELL);
        HearthStone.baseCards.put(bookOfSpecters.getId(), bookOfSpecters);

        WeaponSteal weapon_steal = new WeaponSteal(id++, "Weapon Steal", "Choose your enemy weapon and add it to your deck", 2, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(weapon_steal.getId(), weapon_steal);

        // Minion
        HulkingOverfiend hulkingOverfiend = new HulkingOverfiend(id++, "Hulking Overfiend", "Rush. After this attacks and kills a minion, it may attack again.", 8, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 5, 10,
                false, false, false, false, false, false, true, MinionType.DEMON);
        HearthStone.baseCards.put(hulkingOverfiend.getId(), hulkingOverfiend);

        WrathscaleNaga wrathscaleNaga = new WrathscaleNaga(id++, "Wrathscale Naga", "After a friendly minion dies, deal 3 damage to a random enemy.", 3, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 1, 3,
                false, false, false, false, false, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(wrathscaleNaga.getId(), wrathscaleNaga);

        WrathspikeBrute wrathspikeBrute = new WrathspikeBrute(id++, "Wrathspike Brute", "Taunt After this is attacked, deal 1 damage to all enemies.", 5, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 6, 2,
                false, false, false, false, true, false,  false, MinionType.DEMON);
        HearthStone.baseCards.put(wrathspikeBrute.getId(), wrathspikeBrute);

        PitCommander pitCommander = new PitCommander(id++, "Pit Commander", "Taunt At the end of your turn, summon a Demon from your deck.", 9, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 9, 7,
                false, false, false, false, true, false, false, MinionType.DEMON);
        HearthStone.baseCards.put(pitCommander.getId(), pitCommander);

        GoldshireFootman goldshireFootman = new GoldshireFootman(id++, "Goldshire Footman", "Taunt", 1, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 2, 1,
                false, false, false, false,
                true, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(goldshireFootman.getId(), goldshireFootman);

        Abomination abomination = new Abomination(id++, "Abomination", "Taunt. Deathrattle: Deal 2\ndamage to ALL characters.", 5, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 4, 4,
                true, false, false, false, true, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(abomination.getId(), abomination);

        Sathrovarr sathrovarr = new Sathrovarr(id++, "Sathrovarr", "Battlecry: Choose a friendly\n minion. Add a copy of it to\n your hand, deck, and\n battlefield.", 9, HeroType.ALL, Rarity.LEGENDARY, CardType.MINIONCARD, 5, 5,
                false, false, false, false, false, false, false, MinionType.DEMON);
        HearthStone.baseCards.put(sathrovarr.getId(), sathrovarr);

        TombWarden tombWarden = new TombWarden(id++, "Tomb Warden", "Taunt \n Battlecry: Summon a copy\n of this minion.", 8, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 6, 3,
                false, false, false, false, true, false, false, MinionType.MECH);
        HearthStone.baseCards.put(tombWarden.getId(), tombWarden);

        SecurityRover securityRover = new SecurityRover(id++, "Security Rover", "Whenever this minion\n" +
                "takes damage, summon a\n" +
                "2/3 Mech with Taunt.", 6, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 6, 2,
                false, false, false, false, false, false, false, MinionType.MECH);
        HearthStone.baseCards.put(securityRover.getId(), securityRover);

        CurioCollector curioCollector = new CurioCollector(id++, "Curio Collector", "Whenever you draw a card, gain +1/+1.", 5, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 4, 4,
                false, false, false, false, false, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(curioCollector.getId(), curioCollector);

        FaerieDragon faerieDragon = new FaerieDragon(id++, "Faerie Dragon", "Nothing", 5, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 6, 6,
                false, false, false, false, false, false, false, MinionType.DRAGON);
        HearthStone.baseCards.put(faerieDragon.getId(), faerieDragon);

        Locust locust = new Locust(id++, "Locust", "Rush", 1, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 1, 1,
                false, false, false, false, false, false, true, MinionType.BEAST);
        HearthStone.baseCards.put(locust.getId(), locust);

        Sheep sheep = new Sheep(id++, "Sheep", "Just Sheep!", 1, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 1, 1,
                false, false, false, false,
                false, false, false, MinionType.BEAST);
        HearthStone.baseCards.put(sheep.getId(), sheep);

        TheHulk theHulk = new TheHulk(id++, "The Hulk", "Just Hulk!", 3, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 3, 2,
                false, false, false, false,
                true, false, false, MinionType.BEAST);
        HearthStone.baseCards.put(theHulk.getId(), theHulk);

        // Weapon
        WarglaivesOfAzzinoth warglaivesOfAzzinoth = new WarglaivesOfAzzinoth(id++, "Warglaives of Azzinoth", "After attacking a minion, your hero may attack again.", 5, HeroType.ALL, Rarity.EPIC, CardType.WEAPONCARD, 4, 3);
        HearthStone.baseCards.put(warglaivesOfAzzinoth.getId(), warglaivesOfAzzinoth);

        Flamereaper flamereaper = new Flamereaper(id++, "Flamereaper", "Also damages the minions next to whomever your hero attacks.", 7, HeroType.ALL, Rarity.EPIC, CardType.WEAPONCARD, 3, 4);
        HearthStone.baseCards.put(flamereaper.getId(), flamereaper);

        Candleshot candleshot = new Candleshot(id++, "Candleshot", "Your hero is Immune while attacking.", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 3, 1);
        HearthStone.baseCards.put(candleshot.getId(), candleshot);

        Glaivezooka glaivezooka = new Glaivezooka(id++, "Glaivezooka", "Battlecry: Give a random friendly minion +1 Attack.", 2, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 2, 2);
        HearthStone.baseCards.put(glaivezooka.getId(), glaivezooka);

        EaglehornBow eaglehornBow = new EaglehornBow(id++, "Eaglehorn Bow", "", 3, HeroType.ALL, Rarity.RARE, CardType.WEAPONCARD, 3, 3);
        HearthStone.baseCards.put(eaglehornBow.getId(), eaglehornBow);

        DesertSpear desertSpear = new DesertSpear(id++, "Desert Spear", "After your hero attacks, summon a 1/1 Locust with Rush.", 3, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 3, 1);
        HearthStone.baseCards.put(desertSpear.getId(), desertSpear);

        BattleAxe battleAxe = new BattleAxe(id++, "Battle Axe", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 2, 2);
        HearthStone.baseCards.put(battleAxe.getId(), battleAxe);

        HeavyAxe heavyAxe = new HeavyAxe(id++, "Heavy Axe", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 3, 1);
        HearthStone.baseCards.put(heavyAxe.getId(), heavyAxe);

        WickedKnife wickedKnife = new WickedKnife(id++, "Wicked Knife", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPONCARD, 2, 1);
        HearthStone.baseCards.put(wickedKnife.getId(), wickedKnife);

        // Reward
        LearnDraconic learnDraconic = new LearnDraconic(id++, "Learn Draconic", "Sidequest: Spend\n" +
                "8 Mana on spells.\n" +
                "Reward: Summon a\n" +
                "6/6 Dragon.", 1, HeroType.ALL, Rarity.COMMON, CardType.REWARDCARD);
        HearthStone.baseCards.put(learnDraconic.getId(), learnDraconic);

        StrengthInNumbers strengthInNumbers = new StrengthInNumbers(id++, "Strength in Numbers", "Sidequest: Spend 10 Mana\n +" +
                " on minions.\n" +
                "Reward: Summon a\n" +
                " minion from your deck.", 1, HeroType.ALL, Rarity.COMMON, CardType.REWARDCARD);
        HearthStone.baseCards.put(strengthInNumbers.getId(), strengthInNumbers);

        //baseCards = getBaseCards();
    }

    private static void loadHeroes() throws Exception {
        int id = 0;
        Mage mage = new Mage(id++, "Mage", HeroType.MAGE, "witchers are good with spells!\nshe pays 2 mana less for spells!", 30, new ArrayList<Integer>(
                Arrays.asList(0, 1, 10, 13, 16, 19)));
        HearthStone.baseHeroes.put(mage.getId(), mage);

        Warlock warlock = new Warlock(id++, "Warlock", HeroType.WARLOCK, "healthier than other!\nreduce 2 health and do somethings !\n", 35, new ArrayList<Integer>(
                Arrays.asList(2, 3, 11, 11, 14, 14, 17)));
        HearthStone.baseHeroes.put(warlock.getId(), warlock);

        Rogue rogue = new Rogue(id++, "Rogue", HeroType.ROGUE, "the thief!\nwith 3 mana, she can steal one opponent card!", 30, new ArrayList<Integer>(
                Arrays.asList(4, 5, 12, 15, 15, 18, 18)));
        HearthStone.baseHeroes.put(rogue.getId(), rogue);

        Paladin paladin = new Paladin(id++, "Paladin", HeroType.PALADIN, "is very good and strong", 30, new ArrayList<Integer>(
                Arrays.asList(6, 7)));
        HearthStone.baseHeroes.put(paladin.getId(), paladin);

        Priest priest = new Priest(id++, "Priest", HeroType.PRIEST, "", 30, new ArrayList<Integer>(
                Arrays.asList(8, 9)));
        HearthStone.baseHeroes.put(priest.getId(), priest);

        //baseHeroes = getBaseHeroes();
    }

    private static void loadPassives() throws Exception {
        int id = 0;
        Passive twiceDraw = new Passive(id++, "Twice Draw");
        basePassives.put(twiceDraw.getId(), twiceDraw);

        Passive offCards = new Passive(id++, "Off Cards");
        basePassives.put(offCards.getId(), offCards);

        Passive freePower = new Passive(id++, "Free Power");
        basePassives.put(freePower.getId(), freePower);

        Passive manaJump = new Passive(id++, "Mana Jump");
        basePassives.put(manaJump.getId(), manaJump);

        Passive warriors = new Passive(id++, "Warriors");
        basePassives.put(warriors.getId(), warriors);

        //basePassives = getBasePassives();
    }

    private static Map<String, AccountCredential> getCredentials() throws Exception {
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

    private static Map<String, Object> getGameConfigs() throws Exception {
        File json = new File(dataPath + "/game_configs.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/game_configs.json");
        return gson.fromJson(fileReader, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    private static Map<String, Object> getSizeConfigs() throws Exception {
        File json = new File(dataPath + "/size_configs.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/size_configs.json");
        return gson.fromJson(fileReader, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    private static Market getMarket() throws Exception {
        File json = new File(dataPath + "/market.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/market.json");
        Market ans = gson.fromJson(fileReader, Market.class);
        if (ans == null)
            return market;
        return ans;
    }

    private static Map<Integer, Card> getBaseCards() throws Exception {
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

    private static Map<Integer, Hero> getBaseHeroes() throws Exception {
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

    private static Map<Integer, Passive> getBasePassives() throws Exception {
        File json = new File(dataPath + "/base_passives.json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileReader fileReader = new FileReader(dataPath + "/base_passives.json");
        Map<Integer, Passive> ans = gson.fromJson(fileReader, new TypeToken<Map<Integer, Passive>>() {
        }.getType());
        if (ans == null)
            return basePassives;
        return ans;
    }

    private static void saveCurrentAccount() throws Exception {
        File json = new File(dataPath + "/accounts" + "/account_" + currentAccount.getId() + ".json");
        json.getParentFile().mkdirs();
        json.createNewFile();
        FileWriter fileWriter = new FileWriter(dataPath + "/accounts" + "/account_" + currentAccount.getId() + ".json");
        gson.toJson(currentAccount, Account.class, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    private static void saveCredentials() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/credentials.json");
        gson.toJson(Data.getAccounts(), new TypeToken<Map<Integer, AccountCredential>>() {}.getType(), fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    private static void saveMarket() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/market.json");
        gson.toJson(market, Market.class, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void save() throws Exception {
        saveCredentials();
        saveMarket();
        if (currentAccount != null) {
            saveCurrentAccount();
        }
    }

    private static void loadConfigs() throws Exception {
        var gameConfigs = getGameConfigs();
        GameConfigs.setConfigs(gameConfigs);

        var sizeConfigs = getSizeConfigs();
        SizeConfigs.setConfigs(sizeConfigs);
    }

    private static void loadMarket() throws Exception {
        market = getMarket();
    }

    private static void loadAccounts() throws Exception {
        Data.setAccounts(getCredentials());
    }

    private static void saveCards() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/base_cards.json");
        gson.toJson(baseCards, new TypeToken<Map<Integer, Card>>() {}.getType(), fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    private static void saveHeroes() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/base_heroes.json");
        gson.toJson(baseHeroes, new TypeToken<Map<Integer, Hero>>() {}.getType(), fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    private static void savePassives() throws Exception {
        FileWriter fileWriter = new FileWriter(dataPath + "/base_passives.json");
        gson.toJson(basePassives, new TypeToken<Map<Integer, Passive>>() {}.getType(), fileWriter);
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
        loadPassives();

        saveCards();
        saveHeroes();
        savePassives();

        loadAccounts();
        loadMarket();
    }
}