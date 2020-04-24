package hearthstone;

import hearthstone.data.Data;
import hearthstone.data.DataBase;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.Account;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.gamestuff.Market;
import hearthstone.util.HearthStoneException;

import java.util.HashMap;
import java.util.Map;

public class  HearthStone {
    public static int maxCardInCollection;
    public static int initialCoins;
    public static int maxCardInDeck;
    public static int maxManaInGame;
    public static int maxCardInHand;
    public static int maxCardInLand;
    public static int maxCardOfOneType;


    public static Map<Integer, Card> baseCards = new HashMap<>();
    public static Map<Integer, Hero> baseHeroes = new HashMap<>();
    public static Account currentAccount;
    public static String dataPath;
    public static Market market = new Market();

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static boolean userNameIsValid(String username) {
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (c >= '0' && c <= '9')
                continue;
            if (c >= 'a' && c <= 'z')
                continue;
            if (c >= 'A' && c <= 'Z')
                continue;
            if (c == '_' || c == '.')
                continue;
            return false;
        }
        return username.length() >= 4;
    }

    public static boolean passwordIsValid(String username) {
        if (username.length() < 4)
            return false;
        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) >= 'A' && username.charAt(i) <= 'Z') {
                return true;
            }
        }
        return false;
    }

    public static void login(String username, String password) throws Exception {
        Data.checkAccountCredentials(username, password);
        currentAccount = DataBase.getAccount(Data.getAccountId(username));
        hearthstone.util.Logger.saveLog("login", "signed in successfully!");
    }

    public static void register(String name, String username, String password, String repeat) throws Exception {
        if(name.length() == 0 || username.length() == 0 || password.length() == 0 || repeat.length() == 0){
            throw new HearthStoneException("please fill all the fields!");
        }
        if (!password.equals(repeat)) {
            throw new HearthStoneException("Passwords does not match!");
        }
        /*if (!userNameIsValid(username)) {
            throw new HearthStoneException("Username is invalid(at least 4 character, only contains 1-9, '-', '_' and letters!)");
        }
        if (!passwordIsValid(password)) {
            throw new HearthStoneException("Password is invalid(at least 4 character and contains at least a capital letter!)");
        }*/
        Data.addAccountCredentials(username, password);
        currentAccount = new Account(Data.getAccountId(username), name, username);
        hearthstone.util.Logger.createAccountLog(username);
    }

    public static void logout() throws Exception {
        DataBase.save();
        if (currentAccount != null)
            hearthstone.util.Logger.saveLog("logout", "signed out in successfully!");
        currentAccount = null;
    }

    public static void deleteAccount(String username, String password) throws Exception {
        Data.deleteAccount(username, password);
        hearthstone.util.Logger.saveLog("Delete Account", "account deleted!");
        logout();
    }

    public static void main(String[] args) {
        dataPath = "./data";
        try {
            DataBase.load();
            //cli();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to load DataBase!");
        }
        CredentialsFrame.getInstance();
    }
}