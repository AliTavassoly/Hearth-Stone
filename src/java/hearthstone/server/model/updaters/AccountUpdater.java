package hearthstone.server.model.updaters;

import hearthstone.server.model.UpdateWaiter;
import hearthstone.server.network.ClientHandler;
import hearthstone.server.network.HSServer;

public class AccountUpdater implements UpdateWaiter {
    private ClientHandler clientHandler;
    private UpdateWaiter.UpdaterType updaterType;
    private String username;

    public AccountUpdater(String username, UpdateWaiter.UpdaterType updaterType, ClientHandler clientHandler) {
        this.username = username;
        this.updaterType = updaterType;
        this.clientHandler = clientHandler;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public UpdateWaiter.UpdaterType updaterType() {
        return updaterType;
    }

    @Override
    public void update() {
        HSServer.getInstance().updateAccount(clientHandler);
    }
}
