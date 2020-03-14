package hearthstone;

import hearthstone.game.cards.CardType;
import hearthstone.game.cards.MinionCard;
import hearthstone.game.cards.Rarity;
import hearthstone.game.cards.SpellCard;
import hearthstone.game.heroes.HeroType;
import hearthstone.game.heroes.Rogue;

public class Load {
    public static void loadMinionCards() {
        MinionCard ali = new MinionCard("ali", "Kill all !", 10, HeroType.ALL, Rarity.COMMON, CardType.MINIONCARD, 3, 5);
        Game.baseCards.put(ali.getId(), ali);
    }

    public static void loadWeaponCards() { }

    public static void loadSpellCards() {
        SpellCard rage = new SpellCard("rage", "speed", 2, HeroType.MAGE, Rarity.EPIC, CardType.SPELL);
        Game.baseCards.put(rage.getId(), rage);
    }

    public static void loadHeroCards() { }

    public static void loadHeroes() {
        Rogue rogue = new Rogue("Warlock", "more health", 35, HeroType.WARLOCK);
        Game.baseHeroes.put(rogue.getId(), rogue);
    }

    public static void loadStore(){}
}