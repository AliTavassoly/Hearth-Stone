package hearthstone.data.bean;

public class AccountCredential {
    private int id;
    private int passwordHash;
    private boolean deleted;

    public AccountCredential(){ }
    public AccountCredential(int id, int passwordHash) {
        this.id = id;
        this.passwordHash = passwordHash;
        deleted = false;
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


    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
