package hearth.stone;

import hearth.stone.game.cards.CardType;
import hearth.stone.game.cards.MinionCard;
import hearth.stone.game.cards.Rarity;
import hearth.stone.game.cards.SpellCard;
import hearth.stone.game.cards.*;
import hearth.stone.game.heroes.HeroType;
import hearth.stone.game.heroes.Rogue;

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