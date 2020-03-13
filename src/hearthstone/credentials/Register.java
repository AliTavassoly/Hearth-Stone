package hearthstone.credentials;

public class Register {
    void register(String username, String password1, String password2){
        if(Login.users.containsKey(username)){
            System.err.println("Username already exist !");
            return;
        }
        if(password1 != password2){
            System.out.println("Passwords does not match !");
            return;
        }
        // create an account in jason
    }
}
