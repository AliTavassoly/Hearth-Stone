package hearthstone;

import hearthstone.data.Data;
import hearthstone.data.bean.Account;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.util.Crypt;
import hearthstone.util.HearthStoneException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HearthStone {
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
