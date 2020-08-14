package hearthstone.models;

public class WatcherInfo {
    private String username;

    public void setUsername(String username){
        this.username =  username;
    }

    public String getUsername(){
        return username;
    }

    public WatcherInfo(){}

    public WatcherInfo(String username){
        this.username = username;
    }
}
