package hearthstone.credentials;

public class UserLoginDetail {
    private int password;
    private int id;

    void setPassword(int password){
        this.password = password;
    }
    int getPassword(){
        return password;
    }

    void setId(int id){
        this.id = id;
    }
    int getId(){
        return id;
    }
}
