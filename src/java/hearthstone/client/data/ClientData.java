package hearthstone.client.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.passive.Passive;
import hearthstone.server.data.ServerData;
import hearthstone.shared.GUIConfigs;
import hearthstone.shared.GameConfigs;
import org.hibernate.collection.internal.PersistentBag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientData {
    public static String dataPath = "data";

    public static Map<Integer, Card> baseCards = new HashMap<>();
    public static Map<Integer, Hero> baseHeroes = new HashMap<>();
    public static Map<Integer, Passive> basePassives = new HashMap<>();

    public static Card getCardByName(String name){
        for(Card card: baseCards.values()){
            if(card.getName().equals(name)){
                return card.copy();
            }
        }
        return null;
    }

    public static Card getCardById(int cardId){
        for(Card card: baseCards.values()){
            if(card.getId() == cardId){
                return card.copy();
            }
        }
        return null;
    }

    public static Hero getHeroByName(String name){
        for(Hero hero: baseHeroes.values()){
            if(hero.getName().equals(name)){
                return hero.copy();
            }
        }
        return null;
    }

    public static Hero getHeroByType(HeroType heroType){
        for(Hero hero: baseHeroes.values()){
            if(hero.getType() == heroType){
                return hero.copy();
            }
        }
        return null;
    }

    public static ArrayList<Card> getCardsArrayFromName(ArrayList<String> cardsName){
        ArrayList<Card> ans = new ArrayList<>();

        for(int i = 0; i < cardsName.size(); i++){
            ans.add(ServerData.getCardByName(cardsName.get(i)));
        }

        return ans;
    }

    public synchronized static ObjectMapper getDataMapper(){
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    public synchronized static ObjectMapper getObjectCloneMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.EVERYTHING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new Hibernate5Module());
        mapper.registerSubtypes(PersistentBag.class);
        return mapper;
    }

    public synchronized static ObjectMapper getNetworkMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.EVERYTHING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new Hibernate5Module());
        return mapper;
    }

    public static void load() throws Exception{
        GUIConfigs.loadConfigs();

        GameConfigs.loadConfigs();
    }
}
