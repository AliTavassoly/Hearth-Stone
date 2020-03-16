package hearthstone;

import hearthstone.data.Data;
import hearthstone.data.DataBase;
import hearthstone.data.bean.Account;
import hearthstone.data.bean.cards.Card;
import hearthstone.data.bean.heroes.Hero;
import hearthstone.gamestuff.CollectionManager;
import hearthstone.gamestuff.Market;
import hearthstone.util.HearthStoneException;

import java.io.File;
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

    public static void logout() throws Exception{
        DataBase.save();
        currentAccount = null;
    }

    public static void deleteAccount(String username, String password) throws Exception{
        Data.deleteAccount(username, password);
    }

    public static void loginCli(){
        Scanner scanner = new Scanner(System.in);
        String name, username, password, repeat, sure;

        while (true){
            try {
                if(currentAccount != null)
                    cli();

                System.out.print("@login/register page, please enter your command : ");
                String command = scanner.nextLine();

                switch (command) {
                    case "help" :
                        System.out.println("login : if you already have an account !");
                        System.out.println("register : if you want to create new account !");
                        System.out.println("EXIT : if you want to exit from Hearth Stone !");
                        break;
                    case "login" :
                        System.out.print("username : ");
                        username = scanner.next();
                        System.out.print("password : ");
                        password = scanner.next();
                        login(username, password);
                        //LOG : login
                        break;
                    case "register" :
                        System.out.print("enter your name : ");
                        name = scanner.next();
                        System.out.print("enter your username : ");
                        username = scanner.next();
                        System.out.print("enter password : ");
                        password = scanner.next();
                        System.out.print("repeat your password : ");
                        repeat = scanner.next();
                        register(name, username, password, repeat);
                        //LOG : registration
                        break;
                    case "EXIT" :
                        System.out.print("are you sure you want to EXIT ?!(y/n) ");
                        sure = scanner.next();
                        if(sure.equals("y")) {
                            //LOG : registration
                            logout();
                            System.exit(0);
                        }
                        break;
                    default :
                        System.out.println("please enter correct command !");
                }
            } catch (HearthStoneException e){
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println("An error occurred !");
            }
        }
    }

    public static void cli(){
        String sure, password;
        Scanner scanner = new Scanner(System.in);
        while (true){
            try {
                if (currentAccount == null)
                    loginCli();

                System.out.print("@" + currentAccount.getUsername() + " "+ " account, please enter your command : ");
                String command = scanner.nextLine();

                switch (command){
                    case "help" :
                        System.out.println("store : go and shopping !");
                        System.out.println("collection : rearrange your deck and fight !");
                        System.out.println("delete : delete your account !");
                        System.out.println("exit : to logout !");
                        System.out.println("EXIT : exit from Hearth Stone !");
                        break;
                    case "store" :
                        Market.cli();
                        break;
                    case "collection" :
                        CollectionManager.cli();
                        break;
                    case "delete" :
                        System.out.print("are you sure you want to logout ?!(y/n) ");
                        sure = scanner.next();
                        if(sure.equals("y")) {
                            System.out.print("enter password : ");
                            password = scanner.next();
                            deleteAccount(currentAccount.getUsername(), password);
                        }
                        break;
                    case "exit" :
                        System.out.print("are you sure you want to logout ?!(y/n) ");
                        sure = scanner.next();
                        if(sure.equals("y")){
                            logout();
                        }
                        break;
                    case "EXIT" :
                        System.out.print("are you sure you want to EXIT ?!(y/n) ");
                        sure = scanner.next();
                        if(sure.equals("y")) {
                            logout();
                            System.exit(0);
                        }
                        break;
                    default :
                        System.out.println("please enter correct command !");
                }
            } catch (HearthStoneException e){
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println("An error occurred !");
            }
        }
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
            cli();
        } catch (Exception e){
            System.out.println(e.getMessage());
            //System.out.println("Failed to load DataBase !");
        }
    }
}
