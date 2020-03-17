package hearthstone;

import hearthstone.data.Data;
import hearthstone.data.DataBase;
import hearthstone.data.bean.Account;
import hearthstone.modules.cards.Card;
import hearthstone.modules.heroes.Hero;
import hearthstone.gamestuff.CollectionManager;
import hearthstone.gamestuff.Market;
import hearthstone.util.HearthStoneException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HearthStone {
    public static int maxCollectionSize = 50;
    public static int initialCoins = 50;
    public static int maxDeckSize = 30;
    public static int maxNumberOfCard = 2;
    public static Map<Integer, Card> baseCards = new HashMap<>();
    public static Map<Integer, Hero> baseHeroes = new HashMap<>();
    public static Account currentAccount;
    public static String dataPath;
    public static Market market = new Market();

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void login(String username, String password) throws Exception {
        Data.checkAccountCredentials(username, password);
        currentAccount = DataBase.getAccount(Data.getAccountId(username));
    }

    public static void register(String name, String username, String password, String repeat) throws Exception {
        if (!password.equals(repeat)) {
            throw new HearthStoneException("Passwords does not match!");
        }
        Data.addAccountCredentials(username, password);
        currentAccount = new Account(Data.getAccountId(username), name, username);
    }

    public static void logout() throws Exception {
        DataBase.save();
        currentAccount = null;
    }

    public static void deleteAccount(String username, String password) throws Exception {
        Data.deleteAccount(username, password);
        logout();
    }

    public static void loginCli() {
        Scanner scanner = new Scanner(System.in);
        String name, username, password, repeat, sure;

        while (true) {
            try {
                if (currentAccount!= null)
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
                        //LOG : login
                        break;
                    case "register":
                        System.out.print("enter your name : ");
                        name = scanner.nextLine().trim();
                        System.out.print("enter your username : ");
                        username = scanner.nextLine().trim();
                        System.out.print("enter password : ");
                        password = scanner.nextLine().trim();
                        System.out.print("repeat your password : ");
                        repeat = scanner.nextLine().trim();
                        register(name, username, password, repeat);
                        //LOG : registration
                        break;
                    case "EXIT":
                        System.out.print(ANSI_RED + "are you sure you want to EXIT?! (y/n) " + ANSI_RESET);
                        sure = scanner.nextLine().trim();
                        if (sure.equals("y")) {
                            //LOG : registration
                            logout();
                            System.exit(0);
                        }
                        break;
                    default:
                        System.out.println("please enter correct command! (enter help for more info)");
                }
            } catch (HearthStoneException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred!");
            }
        }
    }

    public static void cli() {
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
                        System.out.print(ANSI_RED + "are you sure you want to logout?! (y/n) " + ANSI_RESET);
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
            } catch (HearthStoneException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred!");
            }
        }
    }

    public static void main(String[] args) {
        /*try{
            dataPath = new File(HearthStone.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            dataPath += "/data";
        } catch (Exception e){
            dataPath = "./data";
        }*/
        dataPath = "./data";
        try {
            DataBase.load();
            cli();
        } catch (Exception e) {
            System.out.println("Failed to load DataBase!");
        }
    }

}
