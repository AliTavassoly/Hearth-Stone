package hearthstone.data.bean.heroes;

public abstract class Hero{
    private int id;
    private String name;
    private String description;
    private int health;
    private HeroType type;

    Hero(){ }

    Hero(String name, String description, int health, HeroType type){
        this.name = name;
        this.description = description;
        this.health = health;
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public HeroType getType() {
        return type;
    }

    public void setType(HeroType type) {
        this.type = type;
    }
}