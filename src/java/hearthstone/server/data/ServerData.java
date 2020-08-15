package hearthstone.server.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import hearthstone.models.Account;
import hearthstone.models.AccountCredential;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.models.passive.Passive;
import hearthstone.server.model.ClientDetails;
import hearthstone.util.Crypt;
import hearthstone.util.HearthStoneException;
import org.hibernate.collection.internal.PersistentBag;

import java.util.HashMap;
import java.util.Map;

public class ServerData {
    private static Map<String, AccountCredential> accounts = new HashMap<>();
    private static Map<String, ClientDetails> clientsDetails = new HashMap<>();

    public static Map<Integer, Card> baseCards = new HashMap<>();
    public static Map<Integer, Hero> baseHeroes = new HashMap<>();
    public static Map<Integer, Passive> basePassives = new HashMap<>();

    public static void setAccounts(Map<String, AccountCredential> accounts) {
        ServerData.accounts = accounts;
    }

    public static void setClientsDetails(){
        for(String username: accounts.keySet()){
            try {
                clientsDetails.put(username, new ClientDetails(DataBase.getAccount(username)));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Map<String, AccountCredential> getAccounts() {
        return accounts;
    }

    public static Map<String, ClientDetails> getClientsDetails() {
        return clientsDetails;
    }

    public static ClientDetails getClientDetails(String username){
        return clientsDetails.get(username);
    }

    public static void checkAccountCredentials(String username, String password) throws HearthStoneException {
        if (!accounts.containsKey(username)) {
            throw new HearthStoneException("This username does not exists!");
        }
        if (accounts.get(username).getPasswordHash() != Crypt.hash(password)) {
            throw new HearthStoneException("Password is not correct!");
        }
        if (accounts.get(username).isDeleted()) {
            throw new HearthStoneException("This username has been deleted!");
        }
    }

    public static void addAccountCredentials(String username, String password) throws HearthStoneException {
        if (accounts.containsKey(username)) {
            throw new HearthStoneException("This username is already exists!");
        }
        accounts.put(username, new AccountCredential(accounts.size(), username, Crypt.hash(password)));
    }

    public static void addNewClientDetails(String username, Account account) {
        clientsDetails.put(username, new ClientDetails(account));
    }

    public static void deleteAccount(String username) {
        accounts.get(username).setDeleted(true);
        try {
            DataBase.save();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getAccountId(String username) {
        return accounts.get(username).getId();
    }

    public static void changePassword(String username, String password){
        accounts.get(username).setPasswordHash(Crypt.hash(password));
    }

    public static ObjectMapper getDataMapper(){
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    public static ObjectMapper getObjectCloneMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.EVERYTHING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new Hibernate5Module());
        mapper.registerSubtypes(PersistentBag.class);
        return mapper;
    }

    public static ObjectMapper getNetworkMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.EVERYTHING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new Hibernate5Module());
        return mapper;
    }

    public static Card getCardByName(String name) {
        for (Card card : baseCards.values()) {
            if (card.getName().equals(name)) {
                return card.copy();
            }
        }
        return null;
    }

    public static Card getCardById(int cardId) {
        for (Card card : baseCards.values()) {
            if (card.getId() == cardId) {
                return card.copy();
            }
        }
        return null;
    }

    public static Hero getHeroByName(String name) {
        for (Hero hero : baseHeroes.values()) {
            if (hero.getName().equals(name)) {
                return hero.copy();
            }
        }
        return null;
    }

    public static Hero getHeroByType(HeroType heroType) {
        for (Hero hero : baseHeroes.values()) {
            if (hero.getType() == heroType) {
                return hero.copy();
            }
        }
        return null;
    }
}