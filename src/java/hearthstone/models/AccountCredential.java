package hearthstone.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccountCredential {
    @Id
    private int id;
    @Column
    private int passwordHash;
    @Column
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
