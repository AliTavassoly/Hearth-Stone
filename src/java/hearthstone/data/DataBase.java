package hearthstone.data;

import com.fasterxml.jackson.core.type.TypeReference;
import hearthstone.client.configs.GUIConfigs;
import hearthstone.client.configs.GameConfigs;
import hearthstone.logic.Market;
import hearthstone.models.Account;
import hearthstone.models.AccountCredential;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.minion.MinionType;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.hero.heroes.*;
import hearthstone.models.passive.Passive;
import hearthstone.models.passive.passives.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import hearthstone.models.card.minion.minions.*;
import hearthstone.models.card.spell.spells.*;
import hearthstone.models.card.reward.rewards.*;
import hearthstone.models.card.heropower.heropowers.*;
import hearthstone.models.card.weapon.weapons.*;


import java.io.*;
import java.util.*;

import static hearthstone.HearthStone.*;

public class DataBase {
    private static SessionFactory sessionFactory = buildSessionFactory();
    private static Session session = sessionFactory.openSession();

    private static SessionFactory buildSessionFactory() {
        /*PrintStream err = System.err;
        try {
            PrintStream printStream = new PrintStream(new File("./l.txt"));
            System.setErr(printStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        //System.out.println(err);
        return sessionFactory;
    }

    private static void loadCards() throws Exception {
        /*int id = 0;
        // Mage
        Polymorph polymorph = new Polymorph(id++, "Polymorph", "Transform a minion into a 1/1 sheep.", 4, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(polymorph.getId(), polymorph);

        Fireblast fireblast = new Fireblast(id++, "Fireblast", "Deal 1 damage.", 2, HeroType.MAGE, CardType.HEROPOWER);
        HearthStone.baseCards.put(fireblast.getId(), fireblast);

        FreezingPotion freezingPotion = new FreezingPotion(id++, "Freezing Potion", "Freeze an enemy.", 0, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(freezingPotion.getId(), freezingPotion);

        // Warlock
        Dreadscale dreadscale = new Dreadscale(id++, "Dreadscale", "At the end of your turn, deal 1 damage to all other minions.", 3, HeroType.WARLOCK, Rarity.LEGENDARY, CardType.MINIONCARD, 2, 4,
                false, false, false, false, false, false, false, false, MinionType.BEAST);
        HearthStone.baseCards.put(dreadscale.getId(), dreadscale);

        Sacrificer sacrificer = new Sacrificer(id++, "Sacrificer", "Sacrifice.", 2, HeroType.WARLOCK, CardType.HEROPOWER);
        HearthStone.baseCards.put(sacrificer.getId(), sacrificer);

        Soulfire soulfire = new Soulfire(id++, "Soulfire", "Deal 4 damage. Discard a random card.", 1, HeroType.WARLOCK, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(soulfire.getId(), soulfire);

        // Rogue
        FriendlySmith friendlySmith = new FriendlySmith(id++, "Friendly Smith", "Discover a weapon\n" + "from any class. Add it\n" + "to your Adventure Deck\n" + "with +2/+2.", 1, HeroType.ROGUE, Rarity.COMMON, CardType.SPELL);
        HearthStone.baseCards.put(friendlySmith.getId(), friendlySmith);

        AncientBlades ancientBlades = new AncientBlades(id++, "Ancient Blades", "Steal cards.", 3, HeroType.ROGUE, CardType.HEROPOWER);
        HearthStone.baseCards.put(ancientBlades.getId(), ancientBlades);

        LabRecruiter labRecruiter = new LabRecruiter(id++, "Lab Recruiter", "Battlecry: Shuffle 3 copies of a friendly minion into your deck.", 2, HeroType.ROGUE, Rarity.COMMON, CardType.MINIONCARD, 2, 3,
        false, false, false, false, false, false, false, false, MinionType.NORMAL);
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
                false, false, false, false, false, false, false, false, MinionType.NORMAL);
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
                false, false, false, false, false,false, false, true, MinionType.DEMON);
        HearthStone.baseCards.put(hulkingOverfiend.getId(), hulkingOverfiend);

        WrathscaleNaga wrathscaleNaga = new WrathscaleNaga(id++, "Wrathscale Naga", "After a friendly minion dies, deal 3 damage to a random enemy.", 3, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 1, 3,
                false, false, false, false, false, false, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(wrathscaleNaga.getId(), wrathscaleNaga);

        WrathspikeBrute wrathspikeBrute = new WrathspikeBrute(id++, "Wrathspike Brute", "Taunt After this is attacked, deal 1 damage to all enemies.", 5, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 6, 2,
                false, false, false, false, false, true, false,  false, MinionType.DEMON);
        HearthStone.baseCards.put(wrathspikeBrute.getId(), wrathspikeBrute);

        PitCommander pitCommander = new PitCommander(id++, "Pit Commander", "Taunt At the end of your turn, summon a Demon from your deck.", 9, HeroType.ALL, Rarity.EPIC, CardType.MINIONCARD, 9, 7,
                false, false, false, false, false, true, false, false, MinionType.DEMON);
        HearthStone.baseCards.put(pitCommander.getId(), pitCommander);

        GoldshireFootman goldshireFootman = new GoldshireFootman(id++, "Goldshire Footman", "Taunt", 1, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 2, 1,
                false, false, false, false, false,
                true, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(goldshireFootman.getId(), goldshireFootman);

        Abomination abomination = new Abomination(id++, "Abomination", "Taunt. Deathrattle: Deal 2\ndamage to ALL characters.", 5, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 4, 4,
                true, false, false, false, false, true, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(abomination.getId(), abomination);

        Sathrovarr sathrovarr = new Sathrovarr(id++, "Sathrovarr", "Battlecry: Choose a friendly\n minion. Add a copy of it to\n your hand, deck, and\n battlefield.", 9, HeroType.ALL, Rarity.LEGENDARY, CardType.MINIONCARD, 5, 5,
                false, false, false, false, false, false, false, false, MinionType.DEMON);
        HearthStone.baseCards.put(sathrovarr.getId(), sathrovarr);

        TombWarden tombWarden = new TombWarden(id++, "Tomb Warden", "Taunt \n Battlecry: Summon a copy\n of this minion.", 8, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 6, 3,
                false, false, false, false, false, true, false, false, MinionType.MECH);
        HearthStone.baseCards.put(tombWarden.getId(), tombWarden);

        SecurityRover securityRover = new SecurityRover(id++, "Security Rover", "Whenever this minion\n" +
                "takes damage, summon a\n" +
                "2/3 Mech with Taunt.", 6, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 6, 2,
                false, false, false, false, false, false, false, false, MinionType.MECH);
        HearthStone.baseCards.put(securityRover.getId(), securityRover);

        CurioCollector curioCollector = new CurioCollector(id++, "Curio Collector", "Whenever you draw a card, gain +1/+1.", 5, HeroType.ALL, Rarity.RARE, CardType.MINIONCARD, 4, 4,
                false, false, false, false, false, false, false, false, MinionType.NORMAL);
        HearthStone.baseCards.put(curioCollector.getId(), curioCollector);

        FaerieDragon faerieDragon = new FaerieDragon(id++, "Faerie Dragon", "Nothing", 5, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 6, 6,
                false, false, true, true, false, false, false, false, MinionType.DRAGON);
        HearthStone.baseCards.put(faerieDragon.getId(), faerieDragon);

        Locust locust = new Locust(id++, "Locust", "Rush", 1, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 1, 1,
                false, false, false, false, false, false, false, true, MinionType.BEAST);
        HearthStone.baseCards.put(locust.getId(), locust);

        Sheep sheep = new Sheep(id++, "Sheep", "Just Sheep!", 1, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 1, 1,
                false, false, false, false, false,
                false, false, false, MinionType.BEAST);
        HearthStone.baseCards.put(sheep.getId(), sheep);

        TheHulk theHulk = new TheHulk(id++, "The Hulk", "Just Hulk!", 3, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 3, 2,
                false, false, false, false, false,
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

        SecurityReward securityReward = new SecurityReward(id++, "Security Reward", "Sidequest: Spend 10 Mana\n +" +
                " on minions.\n" +
                "Reward: Summon a\n" +
                " security rover from your deck.", 1, HeroType.ALL, Rarity.COMMON, CardType.REWARDCARD);
        HearthStone.baseCards.put(securityReward.getId(), securityReward);*/
        baseCards = getBaseCards();
    }

    private static void loadHeroes() throws Exception {
        /*int id = 0;
        Mage mage = new Mage(id++, "Mage", HeroType.MAGE,
                "witchers are good with spells!\nshe pays 2 mana less for spells!", "Fireblast",
                30);
        HearthStone.baseHeroes.put(mage.getId(), mage);

        Warlock warlock = new Warlock(id++, "Warlock", HeroType.WARLOCK,
                "healthier than other!\nreduce 2 health and do somethings !\n", "Sacrificer",
                35);
        HearthStone.baseHeroes.put(warlock.getId(), warlock);

        Rogue rogue = new Rogue(id++, "Rogue", HeroType.ROGUE,
                "the thief!\nwith 3 mana, she can steal one opponent card!", "Ancient Blades",
                30);
        HearthStone.baseHeroes.put(rogue.getId(), rogue);

        Paladin paladin = new Paladin(id++, "Paladin", HeroType.PALADIN, "Minions",
                "The Silver Hand",
                30);
        HearthStone.baseHeroes.put(paladin.getId(), paladin);

        Priest priest = new Priest(id++, "Priest", HeroType.PRIEST, "", "Heal",
                30);
        HearthStone.baseHeroes.put(priest.getId(), priest);*/
        baseHeroes = getBaseHeroes();
    }

    private static void loadPassives() throws Exception {
        /*int id = 0;
        TwiceDraw twiceDraw = new TwiceDraw(id++, "Twice Draw");
        basePassives.put(twiceDraw.getId(), twiceDraw);

        OffCards offCards = new OffCards(id++, "Off Cards");
        basePassives.put(offCards.getId(), offCards);

        FreePower freePower = new FreePower(id++, "Free Power");
        basePassives.put(freePower.getId(), freePower);

        ManaJump manaJump = new ManaJump(id++, "Mana Jump");
        basePassives.put(manaJump.getId(), manaJump);

        Nurse nurse = new Nurse(id++, "Nurse");
        basePassives.put(nurse.getId(), nurse);*/
        basePassives = getBasePassives();
    }

    private static Map<String, AccountCredential> getCredentials() throws Exception {
        Map<String, AccountCredential> ans = new HashMap<>();
        List<AccountCredential> accountCredentials = getAll(AccountCredential.class);
        for (AccountCredential accountCredential : accountCredentials) {
            ans.put(accountCredential.getUsername(), accountCredential);
        }
        return ans;
    }

    public static Account getAccount(String username) throws Exception {
        return fetch(Account.class, username);
    }

    private static Map<String, Integer> getGameConfigs() throws Exception {
        File file = new File(dataPath + "/game_configs.json");
        return Data.getDataMapper().readValue(file, new TypeReference<HashMap<String, Integer>>() {
        });
    }

    public static Map<String, ArrayList<String>> getDecks() throws Exception {
        File file = new File(dataPath + "/decks.json");
        return Data.getDataMapper().readValue(file, new TypeReference<HashMap<String, ArrayList<String>>>() {
        });
    }

    private static Map<String, Integer> getSizeConfigs() throws Exception {
        File file = new File(dataPath + "/size_configs.json");
        return Data.getDataMapper().readValue(file, new TypeReference<HashMap<String, Integer>>() {
        });
    }

    private static Market getMarket() throws Exception { // ????????????????????
        return getAll(Market.class).get(0);
    }

    private static Map<Integer, Card> getBaseCards() throws Exception {
        Map<Integer, Card> map = new HashMap<>();
        for (int i = 1; i <= 46; i++) {
            map.put(i - 1, fetch(Card.class, i));
        }
        return map;
    }

    private static Map<Integer, Hero> getBaseHeroes() throws Exception {
        Map<Integer, Hero> map = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            map.put(i - 1, fetch(Hero.class, i));
        }
        return map;
    }

    private static Map<Integer, Passive> getBasePassives() throws Exception {
        List<Passive> passives = getAll(Passive.class);
        Map<Integer, Passive> map = new HashMap<>();
        for (Passive passive : passives) {
            map.put(passive.getId(), passive);
        }
        return map;
    }

    private static void saveCurrentAccount() throws Exception {
        saveOrUpdate(currentAccount);
    }

    private static void saveCredentials() throws Exception {
        for (AccountCredential accountCredential : Data.getAccounts().values()) {
            saveOrUpdate(accountCredential);
        }
    }

    private static void saveMarket() throws Exception {
        saveOrUpdate(market);
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
        GUIConfigs.setConfigs(sizeConfigs);
    }

    private static void loadMarket() throws Exception {
        market = getMarket();
    }

    private static void loadAccounts() throws Exception {
        Data.setAccounts(getCredentials());
    }

    private static void saveCards() throws Exception {
        int id = 0;
        // Mage
        Polymorph polymorph = new Polymorph(id++, "Polymorph", "Transform a minion into a 1/1 sheep.", 4, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(polymorph);

        Fireblast fireblast = new Fireblast(id++, "Fireblast", "Deal 1 damage.", 2, HeroType.MAGE, CardType.HERO_POWER);
        saveOrUpdate(fireblast);

        FreezingPotion freezingPotion = new FreezingPotion(id++, "Freezing Potion", "Freeze an enemy.", 0, HeroType.MAGE, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(freezingPotion);

        // Warlock
        Dreadscale dreadscale = new Dreadscale(id++, "Dreadscale", "At the end of your turn, deal 1 damage to all other minions.", 3, HeroType.WARLOCK, Rarity.LEGENDARY, CardType.MINION_CARD, 2, 4,
                false, false, false, false, false, false, false, false, MinionType.BEAST);
        saveOrUpdate(dreadscale);

        Sacrificer sacrificer = new Sacrificer(id++, "Sacrificer", "Sacrifice.", 2, HeroType.WARLOCK, CardType.HERO_POWER);
        saveOrUpdate(sacrificer);

        Soulfire soulfire = new Soulfire(id++, "Soulfire", "Deal 4 damage. Discard a random card.", 1, HeroType.WARLOCK, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(soulfire);

        // Rogue
        FriendlySmith friendlySmith = new FriendlySmith(id++, "Friendly Smith", "Discover a weapon\n" + "from any class. Add it\n" + "to your Adventure Deck\n" + "with +2/+2.", 1, HeroType.ROGUE, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(friendlySmith);

        AncientBlades ancientBlades = new AncientBlades(id++, "Ancient Blades", "Steal cards.", 3, HeroType.ROGUE, CardType.HERO_POWER);
        saveOrUpdate(ancientBlades);

        LabRecruiter labRecruiter = new LabRecruiter(id++, "Lab Recruiter", "Battlecry: Shuffle 3 copies of a friendly minion into your deck.", 2, HeroType.ROGUE, Rarity.COMMON, CardType.MINION_CARD, 2, 3,
                false, false, false, false, false, false, false, false, MinionType.NORMAL);
        saveOrUpdate(labRecruiter);

        // Paladin
        TheSilverHand theSilverHand = new TheSilverHand(id++, "The Silver Hand", "Hero Power\n" +
                "Summon two 1/1 Recruits.", 2, HeroType.PALADIN, CardType.HERO_POWER);
        saveOrUpdate(theSilverHand);

        GnomishArmyKnife gnomishArmyKnife = new GnomishArmyKnife(id++, "Gnomish Army Knife", "Give a minion Charge,\n" +
                "Windfury, Divine Shield,\n" +
                "Lifesteal, Poisonous,\n" +
                "Taunt, and Stealth.", 5, HeroType.PALADIN, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(gnomishArmyKnife);

        // Priest
        Heal heal = new Heal(id++, "Heal", "Hero Power\n" +
                "Restore 4 Health.", 2, HeroType.PRIEST, CardType.HERO_POWER);
        saveOrUpdate(heal);

        HighPriestAmet highPriestAmet = new HighPriestAmet(id++, "High Priest Amet", "Whenever you summon a\n" +
                "minion, set its Health equal\n" +
                "to this minion's.", 4, HeroType.PRIEST, Rarity.LEGENDARY, CardType.MINION_CARD, 7, 2,
                false, false, false, false, false, false, false, false, MinionType.NORMAL);
        saveOrUpdate(highPriestAmet);

        // All

        // Spell
        Blur blur = new Blur(id++, "Blur", "Your hero can't take damage this turn.", 0, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(blur);

        Tracking tracking = new Tracking(id++, "Tracking", "Look at the top 3 cards of your deck. Draw one and discard the others.", 1, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(tracking);

        Sprint sprint = new Sprint(id++, "Sprint", "Draw 4 cards.", 7, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(sprint);

        SwarmOfLocusts swarmOfLocusts = new SwarmOfLocusts(id++, "Swarm of Locusts", "Summon seven 1/1 \n Locusts with Rush.", 6, HeroType.ALL, Rarity.RARE, CardType.SPELL);
        saveOrUpdate(swarmOfLocusts);

        PharaohsBlessing pharaohsBlessing = new PharaohsBlessing(id++, "Pharaoh's Blessing", "Give a minion +4/+4,\n Divine Shield, and Taunt.", 6, HeroType.ALL, Rarity.RARE, CardType.SPELL);
        saveOrUpdate(pharaohsBlessing);

        BookOfSpecters bookOfSpecters = new BookOfSpecters(id++, "Book of Specters", "Draw 3 cards. Discard \n any spells drawn.", 2, HeroType.ALL, Rarity.EPIC, CardType.SPELL);
        saveOrUpdate(bookOfSpecters);

        WeaponSteal weaponSteal = new WeaponSteal(id++, "Weapon Steal", "Choose your enemy weapon and add it to your deck", 2, HeroType.ALL, Rarity.COMMON, CardType.SPELL);
        saveOrUpdate(weaponSteal);

        // Minion
        HulkingOverfiend hulkingOverfiend = new HulkingOverfiend(id++, "Hulking Overfiend", "Rush. After this attacks and kills a minion, it may attack again.", 8, HeroType.ALL, Rarity.RARE, CardType.MINION_CARD, 5, 10,
                false, false, false, false, false, false, false, true, MinionType.DEMON);
        saveOrUpdate(hulkingOverfiend);

        WrathscaleNaga wrathscaleNaga = new WrathscaleNaga(id++, "Wrathscale Naga", "After a friendly minion dies, deal 3 damage to a random enemy.", 3, HeroType.ALL, Rarity.EPIC, CardType.MINION_CARD, 1, 3,
                false, false, false, false, false, false, false, false, MinionType.NORMAL);
        saveOrUpdate(wrathscaleNaga);

        WrathspikeBrute wrathspikeBrute = new WrathspikeBrute(id++, "Wrathspike Brute", "Taunt After this is attacked, deal 1 damage to all enemies.", 5, HeroType.ALL, Rarity.EPIC, CardType.MINION_CARD, 6, 2,
                false, false, false, false, false, true, false, false, MinionType.DEMON);
        saveOrUpdate(wrathspikeBrute);

        PitCommander pitCommander = new PitCommander(id++, "Pit Commander", "Taunt At the end of your turn, summon a Demon from your deck.", 9, HeroType.ALL, Rarity.EPIC, CardType.MINION_CARD, 9, 7,
                false, false, false, false, false, true, false, false, MinionType.DEMON);
        saveOrUpdate(pitCommander);

        GoldshireFootman goldshireFootman = new GoldshireFootman(id++, "Goldshire Footman", "Taunt", 1, HeroType.ALL, Rarity.COMMON, CardType.MINION_CARD, 2, 1,
                false, false, false, false, false,
                true, false, false, MinionType.NORMAL);
        saveOrUpdate(goldshireFootman);

        Abomination abomination = new Abomination(id++, "Abomination", "Taunt. Deathrattle: Deal 2\ndamage to ALL characters.", 5, HeroType.ALL, Rarity.RARE, CardType.MINION_CARD, 4, 4,
                true, false, false, false, false, true, false, false, MinionType.NORMAL);
        saveOrUpdate(abomination);

        Sathrovarr sathrovarr = new Sathrovarr(id++, "Sathrovarr", "Battlecry: Choose a friendly\n minion. Add a copy of it to\n your hand, deck, and\n battlefield.", 9, HeroType.ALL, Rarity.LEGENDARY, CardType.MINION_CARD, 5, 5,
                false, false, false, false, false, false, false, false, MinionType.DEMON);
        saveOrUpdate(sathrovarr);

        TombWarden tombWarden = new TombWarden(id++, "Tomb Warden", "Taunt \n Battlecry: Summon a copy\n of this minion.", 8, HeroType.ALL, Rarity.RARE, CardType.MINION_CARD, 6, 3,
                false, false, false, false, false, true, false, false, MinionType.MECH);
        saveOrUpdate(tombWarden);

        SecurityRover securityRover = new SecurityRover(id++, "Security Rover", "Whenever this minion\n" +
                "takes damage, summon a\n" +
                "2/3 Mech with Taunt.", 6, HeroType.ALL, Rarity.RARE, CardType.MINION_CARD, 6, 2,
                false, false, false, false, false, false, false, false, MinionType.MECH);
        saveOrUpdate(securityRover);

        CurioCollector curioCollector = new CurioCollector(id++, "Curio Collector", "Whenever you draw a card, gain +1/+1.", 5, HeroType.ALL, Rarity.RARE, CardType.MINION_CARD, 4, 4,
                false, false, false, false, false, false, false, false, MinionType.NORMAL);
        saveOrUpdate(curioCollector);

        FaerieDragon faerieDragon = new FaerieDragon(id++, "Faerie Dragon", "Nothing", 5, HeroType.ALL, Rarity.COMMON, CardType.MINION_CARD, 6, 6,
                false, false, true, true, false, false, false, false, MinionType.DRAGON);
        saveOrUpdate(faerieDragon);

        Locust locust = new Locust(id++, "Locust", "Rush", 1, HeroType.ALL, Rarity.COMMON, CardType.MINION_CARD, 1, 1,
                false, false, false, false, false, false, false, true, MinionType.BEAST);
        saveOrUpdate(locust);

        Sheep sheep = new Sheep(id++, "Sheep", "Just Sheep!", 1, HeroType.ALL, Rarity.COMMON, CardType.MINION_CARD, 1, 1,
                false, false, false, false, false,
                false, false, false, MinionType.BEAST);
        saveOrUpdate(sheep);

        TheHulk theHulk = new TheHulk(id++, "The Hulk", "Just Hulk!", 3, HeroType.ALL, Rarity.COMMON, CardType.MINION_CARD, 3, 2,
                false, false, false, false, false,
                true, false, false, MinionType.BEAST);
        saveOrUpdate(theHulk);

        // Weapon
        WarglaivesOfAzzinoth warglaivesOfAzzinoth = new WarglaivesOfAzzinoth(id++, "Warglaives of Azzinoth", "After attacking a minion, your hero may attack again.", 5, HeroType.ALL, Rarity.EPIC, CardType.WEAPON_CARD, 4, 3);
        saveOrUpdate(warglaivesOfAzzinoth);

        Flamereaper flamereaper = new Flamereaper(id++, "Flamereaper", "Also damages the minions next to whomever your hero attacks.", 7, HeroType.ALL, Rarity.EPIC, CardType.WEAPON_CARD, 3, 4);
        saveOrUpdate(flamereaper);

        Candleshot candleshot = new Candleshot(id++, "Candleshot", "Your hero is Immune while attacking.", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPON_CARD, 3, 1);
        saveOrUpdate(candleshot);

        Glaivezooka glaivezooka = new Glaivezooka(id++, "Glaivezooka", "Battlecry: Give a random friendly minion +1 Attack.", 2, HeroType.ALL, Rarity.COMMON, CardType.WEAPON_CARD, 2, 2);
        saveOrUpdate(glaivezooka);

        EaglehornBow eaglehornBow = new EaglehornBow(id++, "Eaglehorn Bow", "", 3, HeroType.ALL, Rarity.RARE, CardType.WEAPON_CARD, 3, 3);
        saveOrUpdate(eaglehornBow);

        DesertSpear desertSpear = new DesertSpear(id++, "Desert Spear", "After your hero attacks, summon a 1/1 Locust with Rush.", 3, HeroType.ALL, Rarity.COMMON, CardType.WEAPON_CARD, 3, 1);
        saveOrUpdate(desertSpear);

        BattleAxe battleAxe = new BattleAxe(id++, "Battle Axe", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPON_CARD, 2, 2);
        saveOrUpdate(battleAxe);

        HeavyAxe heavyAxe = new HeavyAxe(id++, "Heavy Axe", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPON_CARD, 3, 1);
        saveOrUpdate(heavyAxe);

        WickedKnife wickedKnife = new WickedKnife(id++, "Wicked Knife", "", 1, HeroType.ALL, Rarity.COMMON, CardType.WEAPON_CARD, 2, 1);
        saveOrUpdate(wickedKnife);

        // Reward
        LearnDraconic learnDraconic = new LearnDraconic(id++, "Learn Draconic", "Sidequest: Spend\n" +
                "8 Mana on spells.\n" +
                "Reward: Summon a\n" +
                "6/6 Dragon.", 1, HeroType.ALL, Rarity.COMMON, CardType.REWARD_CARD);
        saveOrUpdate(learnDraconic);

        StrengthInNumbers strengthInNumbers = new StrengthInNumbers(id++, "Strength in Numbers", "Sidequest: Spend 10 Mana\n +" +
                " on minions.\n" +
                "Reward: Summon a\n" +
                " minion from your deck.", 1, HeroType.ALL, Rarity.COMMON, CardType.REWARD_CARD);
        saveOrUpdate(strengthInNumbers);

        SecurityReward securityReward = new SecurityReward(id++, "Security Reward", "Sidequest: Spend 10 Mana\n +" +
                " on minions.\n" +
                "Reward: Summon a\n" +
                " security rover from your deck.", 1, HeroType.ALL, Rarity.COMMON, CardType.REWARD_CARD);
        saveOrUpdate(securityReward);
    }

    private static void saveHeroes() throws Exception {
        int id = 0;
        Mage mage = new Mage(id++, "Mage", HeroType.MAGE,
                "witchers are good with spells!\nshe pays 2 mana less for spells!", "Fireblast",
                30);
        saveOrUpdate(mage);

        Warlock warlock = new Warlock(id++, "Warlock", HeroType.WARLOCK,
                "healthier than other!\nreduce 2 health and do somethings !\n", "Sacrificer",
                35);
        saveOrUpdate(warlock);

        Rogue rogue = new Rogue(id++, "Rogue", HeroType.ROGUE,
                "the thief!\nwith 3 mana, she can steal one opponent card!", "Ancient Blades",
                30);
        saveOrUpdate(rogue);

        Paladin paladin = new Paladin(id++, "Paladin", HeroType.PALADIN, "Minions",
                "The Silver Hand",
                30);
        saveOrUpdate(paladin);

        Priest priest = new Priest(id++, "Priest", HeroType.PRIEST, "", "Heal",
                30);
        saveOrUpdate(priest);
    }

    private static void savePassives() throws Exception {
        int id = 0;
        TwiceDraw twiceDraw = new TwiceDraw(id++, "Twice Draw");
        saveOrUpdate(twiceDraw);

        OffCards offCards = new OffCards(id++, "Off Cards");
        saveOrUpdate(offCards);

        FreePower freePower = new FreePower(id++, "Free Power");
        saveOrUpdate(freePower);

        ManaJump manaJump = new ManaJump(id++, "Mana Jump");
        saveOrUpdate(manaJump);

        Nurse nurse = new Nurse(id++, "Nurse");
        saveOrUpdate(nurse);
    }

    public static void load() throws Exception {
        /*saveHeroes();
        saveCards();
        savePassives();
        saveMarket();
        if (sessionFactory != null)
            return;*/

        loadConfigs();

        loadCards();
        loadHeroes();
        loadPassives();

        loadAccounts();
        loadMarket();
    }

    private static void saveOrUpdate(Object object) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.saveOrUpdate(object);

        session.getTransaction().commit();

        session.close();
    }

    private static <T> T fetch(Class<T> tClass, Object id) {
        Session session = sessionFactory.openSession();

        T t = session.get(tClass, (Serializable) id);

        session.close();

        return t;
    }

    private static <T> List<T> getAll(Class<T> tClass) {
        Session session = sessionFactory.openSession();
        return session.createQuery("from " + tClass.getName(), tClass).getResultList();
    }
}