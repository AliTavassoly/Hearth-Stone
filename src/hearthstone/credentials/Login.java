package hearthstone.credentials;

import java.util.HashMap;
import java.util.Map;

public class Login {
    public static Map<String, UserLoginDetail> users = new HashMap<>();

    public static void login(String username, String password){
        if(!users.containsKey(username)){
            System.err.println("Username does not exist !");
            return;
        }
        if(users.get(username).getPassword() != Crypt.hash(password)){
            System.err.println("Password is not correct !");
            return;
        }
        //login sho
    }
}
