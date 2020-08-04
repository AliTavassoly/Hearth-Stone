package hearthstone.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import hearthstone.models.AccountCredential;
import hearthstone.util.Crypt;
import hearthstone.util.HearthStoneException;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private static Map<String, AccountCredential> accounts = new HashMap<>();

    public static void setAccounts(Map<String, AccountCredential> accounts) {
        Data.accounts = accounts;
    }

    public static Map<String, AccountCredential> getAccounts() {
        return accounts;
    }

    public static void checkAccountCredentials(String username, String password) throws Exception {
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

    public static void addAccountCredentials(String username, String password) throws Exception {
        if (accounts.containsKey(username)) {
            throw new HearthStoneException("This username is already exists!");
        }
        accounts.put(username, new AccountCredential(accounts.size(), username, Crypt.hash(password)));
    }

    public static void deleteAccount(String username, String password) throws Exception {
        if (accounts.get(username).getPasswordHash() != Crypt.hash(password)) {
            throw new HearthStoneException("Password is not correct!");
        }
        accounts.get(username).setDeleted(true);
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
        return mapper;
    }

    public synchronized static ObjectMapper getNetworkMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.EVERYTHING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new Hibernate5Module());
        return mapper;
    }
}