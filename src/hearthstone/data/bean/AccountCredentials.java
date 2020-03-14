package hearthstone.data.bean;

public class AccountCredentials {
    private String username;
    private int password;
    private int id;

    public void setPassword(int password){
        this.password = password;
    }
    public int getPassword(){
        return password;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
