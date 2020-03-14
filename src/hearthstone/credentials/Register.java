package hearthstone.credentials;

public class Register {
    void register(String username, String password1, String repeat){
        if(Login.users.containsKey(username)){
            System.err.println("Username already exist !");
            return;
        }
        if(!password1.equals(repeat)){
            System.out.println("Passwords does not match !");
            return;
        }
        // create an account in jason
    }
}
