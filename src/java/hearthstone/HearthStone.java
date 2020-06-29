package hearthstone;

import hearthstone.data.Data;
import hearthstone.data.DataBase;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.play.boards.GameBoard;
import hearthstone.logic.gamestuff.Game;
import hearthstone.logic.gamestuff.Market;
import hearthstone.logic.models.Account;
import hearthstone.logic.models.Passive;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.hero.Hero;

import java.util.HashMap;
import java.util.Map;

public class  HearthStone {
    public static Map<Integer, Card> baseCards = new HashMap<>();
    public static Map<Integer, Hero> baseHeroes = new HashMap<>();
    public static Map<Integer, Passive> basePassives = new HashMap<>();

    public static Account currentAccount;
    public static String dataPath;
    public static Market market = new Market();

    public static GameBoard currentGameBoard;
    public static Game currentGame;
    public static int cardsBackId;

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

    public static Card getCardByName(String name){
        for(Card card: baseCards.values()){
            if(card.getName().equals(name)){
                return card.copy();
            }
        }
        return null;
    }

    public static void login(String username, String password) throws Exception {
        Data.checkAccountCredentials(username, password);
        currentAccount = DataBase.getAccount(Data.getAccountId(username));
        hearthstone.util.Logger.saveLog("login", "signed in successfully!");
    }

    public static void register(String name, String username,
                                String password, String repeat) throws Exception {
        /*if(name.length() == 0 || username.length() == 0 || password.length() == 0 || repeat.length() == 0){
            throw new HearthStoneException("please fill all the fields!");
        }
        if (!password.equals(repeat)) {
            throw new HearthStoneException("Passwords does not match!");
        }
        if (!userNameIsValid(username)) {
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

    public static void main(String[] args) {
        dataPath = "./data";
        try {
            DataBase.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CredentialsFrame.getInstance();
    }
}