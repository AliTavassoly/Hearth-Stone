package hearthstone.logic.models.passive;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hearthstone.DataTransform;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.hero.IHero;
import hearthstone.logic.models.specialpower.SpecialHeroPower;
import hearthstone.util.AbstractAdapter;

public abstract class Passive {
    private int id;
    private String name;
    private int playerId;

    public Passive() {
    }

    public Passive(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;

    }

    public void log(){
        try {
            hearthstone.util.Logger.saveLog("Passive",
                    DataTransform.getInstance().getPlayerName(getPlayerId()) +
                    " player, chose " + this.getName() + " passive!");
        } catch (Exception e){
            e.printStackTrace();
        }
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

    public Passive copy() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Card.class, new AbstractAdapter<Card>());
        gsonBuilder.registerTypeAdapter(IHero.class, new AbstractAdapter<IHero>());
        gsonBuilder.registerTypeAdapter(Passive.class, new AbstractAdapter<Passive>());
        gsonBuilder.registerTypeAdapter(SpecialHeroPower.class, new AbstractAdapter<SpecialHeroPower>());

        Gson gson = gsonBuilder.create();
        return gson.fromJson(gson.toJson(this, Passive.class), Passive.class);
    }
}
