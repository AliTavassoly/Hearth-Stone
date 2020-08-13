package hearthstone.server.model;

public class GameRequest {
    private long requestTime;
    private String username;

    public long getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public GameRequest(){}

    public GameRequest(String username, long requestTime) {
        this.requestTime = requestTime;
        this.username = username;
    }
}
