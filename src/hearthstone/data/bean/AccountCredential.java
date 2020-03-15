package hearthstone.data.bean;

public class AccountCredential {
    private int passwordHash;
    private int id;

    public AccountCredential(){ }
    public AccountCredential(int id, int passwordHash) {
        this.id = id;
        this.passwordHash = passwordHash;
    }

    public void setPasswordHash(int passwordHash){
        this.passwordHash = passwordHash;
    }
    public int getPasswordHash(){
        return passwordHash;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

}
