package hearthstone;

import hearthstone.data.Data;
import hearthstone.data.DataBase;
import hearthstone.data.bean.Account;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.gamestuff.Store;
import hearthstone.util.HearthStoneException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HearthStone {
    public static int maxCollectionSize = 50;
    public static int initialCoins = 50;
    public static int maxDeckSize = 30;
    public static int maxNumberOfCard = 2;
    public static Map<Integer, Card> baseCards = new HashMap<>();
    public static Map<Integer, Hero> baseHeroes = new HashMap<>();
    public static Account currentAccount;
    public static String dataPath;
    public static Store store = new Store();

    public static void login(String username, String password) throws Exception {
        Data.checkAccountCredentials(username, password);
        currentAccount = DataBase.getAccount(Data.getAccountId(username));
    }

    public static void register(String name, String username, String password, String repeat) throws Exception{
        if(!password.equals(repeat)){
            throw new HearthStoneException("Passwords does not match !");
        }
        Data.addAccountCredentials(username, password);
        currentAccount = new Account(Data.getAccountId(username), name, username);
    }

    public static void logout(Account account) throws Exception{
        DataBase.save();
        currentAccount = null;
    }

    public static void main(String[] args) {
        try{
            dataPath = new File(HearthStone.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            dataPath += "/data";
        } catch (Exception e){
            dataPath = "./data";
        }

        try {
            DataBase.load();
        } catch (Exception e){
            System.out.println("Failed to load DataBase !");
        }

        while (true){
            try {

            } catch (HearthStoneException e){
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println("An error occurred !");
            }
        }
    }
}
