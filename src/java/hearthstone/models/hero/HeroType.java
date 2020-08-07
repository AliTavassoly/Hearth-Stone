package hearthstone.models.hero;

public enum HeroType {
    ALL ("All"),
    WARLOCK ("Warlock"),
    MAGE ("Mage"),
    ROGUE ("Rogue"),
    PALADIN ("Paladin"),
    PRIEST ("Priest");

    private final String heroName;

    HeroType(String heroName){
        this.heroName = heroName;
    }

    public String getHeroName(){
        return heroName;
    }
}
