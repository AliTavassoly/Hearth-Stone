package hearthstone;

import hearthstone.data.Data;
import hearthstone.data.bean.Account;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.util.Crypt;
import hearthstone.util.HearthStoneException;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HearthStone {
    public static final int maxCollectionSize = 50;
    public static final int initialCoins = 50;
    public static final int maxDeckSize = 30;
    public static final int maxNumberOfCard = 2;
    public static Map<Integer, Card> baseCards = new HashMap<>();
    public static Map<Integer, Hero> baseHeroes = new HashMap<>();
    public static Account currentAccount;

    public static void login(String username, String password) throws Exception{
        Data.checkAccountCredentials(username, password);
    }

    public static void register(String username, String password, String repeat) throws Exception{
        if(!password.equals(repeat)){
            throw new HearthStoneException("Passwords does not match !");
        }
        Data.addAccountCredentials(username, password);
    }

    public static void logout(Account account) throws Exception{
        FileWriter writer = new FileWriter("./users" + account.getId() + ".json");
        gson.toJson(account, "./test.json");
    }

    public static void main(String[] args) {
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
