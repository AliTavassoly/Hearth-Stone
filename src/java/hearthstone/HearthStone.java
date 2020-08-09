package hearthstone;

import hearthstone.server.data.ServerData;
import hearthstone.server.data.DataBase;
import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.client.gui.game.play.boards.GameBoard;
import hearthstone.server.logic.Game;
import hearthstone.server.logic.Market;
import hearthstone.models.Account;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;
import hearthstone.util.HearthStoneException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  HearthStone{
    //public static Account currentAccount;
    //public static String dataPath;
    public static Market market = new Market();

    public static GameBoard currentGameBoard;
    public static Game currentGame;

    /*public static void login(String username, String password) throws HearthStoneException {
        ServerData.checkAccountCredentials(username, password);
        currentAccount = DataBase.getAccount(username);
        try {
            hearthstone.util.Logger.saveLog("login", "signed in successfully!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void register(String name, String username,
                                String password, String repeat) throws Exception {
        if(name.length() == 0 || username.length() == 0 || password.length() == 0 || repeat.length() == 0){
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
        }
        ServerData.addAccountCredentials(username, password);
        currentAccount = new Account(ServerData.getAccountId(username), name, username);
        hearthstone.util.Logger.createAccountLog(username);
    }

    public static void logout() throws Exception {
        Mapper.saveDataBase();
        if (currentAccount != null)
            hearthstone.util.Logger.saveLog("logout", "signed out in successfully!");
        currentAccount = null;
    }
*/
    public static void main(String[] args) {
        //dataPath = "./data";
        try {
            DataBase.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CredentialsFrame.getInstance();
    }
}