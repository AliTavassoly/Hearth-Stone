package hearth.stone.game.heroes;

public class Hero{
    private static int lastId;
    private int id;
    private String name;
    private String description;
    private int health;
    private HeroType type;

    Hero(String name, String description, int health, HeroType type){
        id = lastId;

        this.name = name;
        this.description = description;
        this.health = health;
        this.type = type;

        lastId++;
    }

    public void setType(HeroType heroType){
        this.type = heroType;
    }
    public HeroType getType(){
        return type;
    }

    public int getLastId(){
        return lastId;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setHealth(int health){
        this.health = health;
    }
    public int getHealth(){
        return health;
    }
}