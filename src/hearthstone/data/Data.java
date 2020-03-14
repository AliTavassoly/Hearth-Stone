package hearthstone.data;

import hearthstone.data.bean.AccountCredentials;
import hearthstone.util.Crypt;
import hearthstone.util.HearthStoneException;

import javax.management.openmbean.CompositeData;
import javax.naming.ldap.ExtendedRequest;
import java.util.HashMap;
import java.util.Map;

public class Data {
    private static Map<String, AccountCredentials> accounts = new HashMap<>();

    public static void checkAccountCredentials(String username, String  password) throws Exception{
        if(!accounts.containsKey(username)){
            throw new HearthStoneException("This username does not exists !");
        }
        if(accounts.get(username).getPassword() != Crypt.hash(password)){
            throw new HearthStoneException("Password is not correct !");
        }
    }

    public static void addAccountCredentials(String username, String password, String repeat) throws Exception {
        if(accounts.containsKey(username)){
            throw new HearthStoneException("This username is already exists !");
        }
    }

}
