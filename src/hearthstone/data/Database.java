package hearthstone.data;

import hearthstone.data.bean.cards.CardType;
import hearthstone.data.bean.cards.MinionCard;
import hearthstone.data.bean.cards.Rarity;
import hearthstone.data.bean.cards.SpellCard;
import hearthstone.data.bean.heroes.HeroType;
import hearthstone.data.bean.heroes.Rogue;
import hearthstone.HearthStone;

public class Database {
    public static void loadMinionCards() {
        MinionCard ali = new MinionCard("ali", "Kill all !", 10, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 3, 5);
        HearthStone.baseCards.put(ali.getId(), ali);
    }

    public static void loadWeaponCards() { }

    public static void loadSpellCards() {
        SpellCard rage = new SpellCard("rage", "speed", 2, HeroType.MAGE, Rarity.EPIC, CardType.SPELL);
        HearthStone.baseCards.put(rage.getId(), rage);
    }

    public static void loadHeroCards() { }

    public static void loadHeroes() {
        Rogue rogue = new Rogue("Warlock", "more health", 35, HeroType.WARLOCK);
        HearthStone.baseHeroes.put(rogue.getId(), rogue);
    }

    public static void loadStore(){}

    public static void loadUsers(){

    }
}