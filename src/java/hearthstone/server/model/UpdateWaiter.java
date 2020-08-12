package hearthstone.server.model;

public interface UpdateWaiter {
    String getUsername();
    UpdateWaiter.UpdaterType updaterType();
    void update();

    enum UpdaterType{
        ACCOUNT,
        MARKET_CARDS,
        RANKING
    }
}
