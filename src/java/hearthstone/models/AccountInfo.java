package hearthstone.models;

public class AccountInfo implements Comparable<AccountInfo> {
    private String username;
    private int cup;
    private int rank;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getCup() {
        return cup;
    }
    public void setCup(int cup) {
        this.cup = cup;
    }

    public int getRank(){
        return rank;
    }
    public void setRank(int rank){
        this.rank = rank;
    }

    public AccountInfo(){}

    public AccountInfo(String username, int cup) {
        this.username = username;
        this.cup = cup;
    }

    @Override
    public int compareTo(AccountInfo accountInfo) {
        if (this.getCup() != accountInfo.getCup()) {
            if (this.getCup() > accountInfo.getCup())
                return -1;
            return 1;
        }
        return username.compareTo(accountInfo.username);
    }
}
