package hearthstone.data;

import hearthstone.data.bean.AccountCredential;
import hearthstone.util.Crypt;
import hearthstone.util.HearthStoneException;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private static Map<String, AccountCredential> accounts = new HashMap<>();

    public static void setAccounts(Map<String, AccountCredential> accounts) {
            Data.accounts = accounts;
    }

    public static Map<String, AccountCredential> getAccounts(){
        return accounts;
    }

    public static void checkAccountCredentials(String username, String  password) throws Exception{
        if(!accounts.containsKey(username)){
            throw new HearthStoneException("This username does not exists !");
        }
        if(accounts.get(username).getPasswordHash() != Crypt.hash(password)){
            throw new HearthStoneException("Password is not correct !");
        }
        if(accounts.get(username).isDeleted()){
            throw new HearthStoneException("This username has been deleted !");
        }
    }

    public static void addAccountCredentials(String username, String password) throws Exception {
        if(accounts.containsKey(username)){
            throw new HearthStoneException("This username is already exists !");
        }
        accounts.put(username, new AccountCredential(accounts.size(), Crypt.hash(password)));
    }

    public static void deleteAccount(String username, String password) throws Exception{
        if(accounts.get(username).getPasswordHash() != Crypt.hash(password)){
            throw new HearthStoneException("Password is not correct !");
        }
        accounts.get(username).setDeleted(true);
    }

    public static int getAccountId(String username){
        return accounts.get(username).getId();
    }

}
