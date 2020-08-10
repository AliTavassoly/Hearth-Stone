package hearthstone.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hearthstone.models.Account;
import hearthstone.server.logic.Game;
import hearthstone.server.network.ClientHandler;

@JsonIgnoreProperties(value = { "currentGame", "clientHandler"})

public class ClientDetails {
    private ClientHandler clientHandler;
    private Game currentGame;
    private Account account;

    public ClientHandler getClientHandler() {
        return clientHandler;
    }
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public Game getCurrentGame() {
        return currentGame;
    }
    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    public ClientDetails(){

    }

    public ClientDetails(ClientHandler clientHandler, Game currentGame, Account account) {
        this.clientHandler = clientHandler;
        this.currentGame = currentGame;
        this.account = account;
    }

    public ClientDetails(Account account) {
        this.account = account;
    }
}
