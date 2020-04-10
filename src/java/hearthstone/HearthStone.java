package hearthstone;

import hearthstone.data.Data;
import hearthstone.data.DataBase;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.models.Account;
import hearthstone.models.cards.Card;
import hearthstone.models.heroes.Hero;
import hearthstone.gamestuff.CollectionManager;
import hearthstone.gamestuff.Market;
import hearthstone.util.GetByName;
import hearthstone.util.HearthStoneException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class  HearthStone {
    public static int maxCollectionSize;
    public static int initialCoins;
    public static int maxDeckSize;
    public static int maxNumberOfCard;

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
        if (!userNameIsValid(username)) {
            throw new HearthStoneException("Username is invalid(at least 4 character, only contains 1-9, '-', '_' and letters!)");
        }
        if (!passwordIsValid(password)) {
            throw new HearthStoneException("Password is invalid(at least 4 character and contains at least a capital letter!)");
        }
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

    /*public static void loginCli() {
        Scanner scanner = new Scanner(System.in);
        String name, username, password, repeat, sure, heroName;

        while (true) {
            try {
                if (currentAccount != null)
                    cli();

                System.out.print(ANSI_YELLOW + "@HearthStone : " + ANSI_RESET);
                String command = scanner.nextLine().trim();

                switch (command) {
                    case "help":
                        System.out.println("login : if you already have an account!");
                        System.out.println("register : if you want to create new account!");
                        System.out.println("EXIT : if you want to exit from Hearth Stone!");
                        break;
                    case "login":
                        System.out.print("username : ");
                        username = scanner.nextLine().trim();
                        System.out.print("password : ");
                        password = scanner.nextLine().trim();
                        login(username, password);
                        break;
                    case "register":
                        System.out.print("enter your name : ");
                        name = scanner.nextLine().trim();

                        System.out.print("select your hero (");
                        for (int i = 0; i < baseHeroes.size(); i++) {
                            Hero baseHero = baseHeroes.get(i);
                            if (i == 0)
                                System.out.print(baseHero.getName());
                            else
                                System.out.print(", " + baseHero.getName());
                        }
                        System.out.print(") : ");
                        System.out.flush();
                        heroName = scanner.nextLine().trim();

                        System.out.print("enter your username (at least 4 character, only contains 1-9, '.', '_' and letters) : ");
                        username = scanner.nextLine().trim();
                        System.out.print("enter password (at least 4 character and contains at least a capital letter !): ");
                        password = scanner.nextLine().trim();
                        System.out.print("repeat your password : ");
                        repeat = scanner.nextLine().trim();
                        register(name, username, password, repeat, GetByName.getHeroByName(heroName).getName());
                        break;
                    case "EXIT":
                        System.out.print(ANSI_RED + "are you sure you want to EXIT?! (y/n) " + ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if (sure.equals("y")) {
                            logout();
                            System.exit(0);
                        }
                        break;
                    default:
                        System.out.println("please enter correct command! (enter help for more info)");
                }
                DataBase.save();
            } catch (HearthStoneException e) {
                try {
                    hearthstone.util.Logger.saveLog("ERROR", e.getClass().getName() + ": " + e.getMessage() + "\nStack Trace: " + e.getStackTrace());
                } catch (Exception f) {
                }
                System.out.println(e.getMessage());
            } catch (Exception e) {
                try {
                    hearthstone.util.Logger.saveLog("ERROR", e.getClass().getName() + ": " + e.getMessage() + "\nStack Trace: " + e.getStackTrace());
                } catch (Exception f) {
                }
                System.out.println("An error occurred!");
            }
        }
    }*/

    /*public static void cli() {
        String sure, password;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                if (currentAccount == null)
                    loginCli();

                System.out.print(ANSI_YELLOW + currentAccount.getUsername() + "@" + "HearthStone : " + ANSI_RESET);
                String command = scanner.nextLine().trim();

                switch (command) {
                    case "help":
                        System.out.println("market : go and shopping!");
                        System.out.println("collection : rearrange your deck and fight!");
                        System.out.println("delete : delete your account!");
                        System.out.println("exit : to logout!");
                        System.out.println("EXIT : exit from Hearth Stone!");
                        break;
                    case "market":
                        Market.cli();
                        break;
                    case "collection":
                        CollectionManager.cli();
                        break;
                    case "delete":
                        System.out.print(ANSI_RED + "are you sure you want to delete your account?! (y/n) " + ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if (sure.equals("y")) {
                            System.out.print("enter password : ");
                            password = scanner.nextLine().trim();
                            deleteAccount(currentAccount.getUsername(), password);
                        }
                        break;
                    case "exit":
                        System.out.print(ANSI_RED + "are you sure you want to logout?! (y/n) " + ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if (sure.equals("y")) {
                            logout();
                        }
                        break;
                    case "EXIT":
                        System.out.print(ANSI_RED + "are you sure you want to EXIT?! (y/n) " + ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if (sure.equals("y")) {
                            logout();
                            System.exit(0);
                        }
                        break;
                    default:
                        System.out.println("please enter correct command! (enter help for more info)");
                }
                DataBase.save();
            } catch (HearthStoneException e) {
                try {
                    hearthstone.util.Logger.saveLog("ERROR", hearthstone.util.Logger.exceptionToLog(e));
                } catch (Exception f) {
                }
                System.out.println(e.getMessage());
            } catch (Exception e) {
                try {
                    hearthstone.util.Logger.saveLog("ERROR", hearthstone.util.Logger.exceptionToLog(e));
                } catch (Exception f) {
                }
                System.out.println("An error occurred!");
            }
        }
    }*/

    public static void main(String[] args) {
        dataPath = "./data";
        try {
            DataBase.load();
            CredentialsFrame.getInstance();
            //cli();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to load DataBase!");
        }
    }
}