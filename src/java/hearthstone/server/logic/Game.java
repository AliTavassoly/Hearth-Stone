package hearthstone.server.logic;

import hearthstone.models.WatcherInfo;
import hearthstone.models.behaviours.ChooseCardAbility;
import hearthstone.models.card.Card;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.player.Player;
import hearthstone.models.player.PlayerModel;
import hearthstone.server.model.GameType;
import hearthstone.server.network.HSServer;
import hearthstone.server.network.ServerMapper;
import hearthstone.shared.GameConfigs;
import hearthstone.util.HearthStoneException;
import hearthstone.util.timer.HSBigTask;
import hearthstone.util.timer.HSTimerTask;

import java.util.*;

public class Game extends Thread {
    protected Player player0, player1;
    protected int whoseTurn;

    protected ArrayList<Card> cards;
    protected final Object cardsLock = new Object();

    protected ArrayList<Hero> heroes;
    protected final Object heroesLock = new Object();

    protected ArrayList<WatcherInfo> watchers;
    protected final Object watchersLock = new Object();

    public ArrayList<WatcherInfo> getWatchers() {
        return watchers;
    }
    public void setWatchers(ArrayList<WatcherInfo> watchers) {
        this.watchers = watchers;
    }

    protected GameType gameType;

    public GameType getGameType(){
        return gameType;
    }

    public Game(){}

    public Game(Player player0, Player player1, int id0, int id1, GameType gameType) {
        this.player0 = player0;
        this.player1 = player1;

        this.player0.setPlayerId(id0);
        this.player1.setPlayerId(id1);

        this.gameType = gameType;

        this.player0.setEnemyPlayerId(id1);
        this.player1.setEnemyPlayerId(id0);

        cards = new ArrayList<>();
        heroes = new ArrayList<>();
        watchers = new ArrayList<>();

        new HSTimerTask(500, new HSBigTask() {
            @Override
            public void startFunction() {
            }

            @Override
            public void periodFunction() {
            }

            @Override
            public void warningFunction() {
            }

            @Override
            public void finishedFunction() {

            }

            @Override
            public void closeFunction() {
                HSServer.getInstance().gameEnded(player0, player1);
            }

            @Override
            public boolean finishCondition() {
                return gameEnded();
            }
        }).start();
    }

    protected void configGame() {
        whoseTurn = player0.getPlayerId();

        player0.setGame(this);
        player1.setGame(this);

        player0.configPlayer();
        player1.configPlayer();
    }

    @Override
    public void run() {
        try {
            selectPassive();

            discardInitialCards();

            configGame();

            player0.setMyTurn(true);

            HSServer.getInstance().startGameOnGui(player0.getPlayerId());
            HSServer.getInstance().startGameOnGui(player1.getPlayerId());

            player0.startGame();

            player1.startGame();

            player0.startTurn();
        } catch (HearthStoneException e) {
            e.printStackTrace();
        }
    }

    protected void selectPassive() throws HearthStoneException {
        ServerMapper.selectPassiveRequest(player0.getPlayerId(), HSServer.getInstance().getClientHandlerByPlayer(player0));
        ServerMapper.selectPassiveRequest(player1.getPlayerId(), HSServer.getInstance().getClientHandlerByPlayer(player1));

        long startTime = System.currentTimeMillis();

        while (player0.getPassive() == null || player1.getPassive() == null) {
            if ((System.currentTimeMillis() - startTime) / 1000 > 20) {
                throw new HearthStoneException("Player didn't choose passive!");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void discardInitialCards() throws HearthStoneException {
        ServerMapper.selectNotWantedCardsRequest(player0.getPlayerId(), player0.getTopCards(GameConfigs.initialDiscardCards), HSServer.getInstance().getClientHandlerByPlayer(player0));
        ServerMapper.selectNotWantedCardsRequest(player1.getPlayerId(), player1.getTopCards(GameConfigs.initialDiscardCards), HSServer.getInstance().getClientHandlerByPlayer(player1));

        long startTime = System.currentTimeMillis();

        while (!player0.isDiscardedCards() || !player1.isDiscardedCards()) {
            if ((System.currentTimeMillis() - startTime) / 1000 > 20) {
                throw new HearthStoneException("Player didn't discard initial cards!");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void endTurn(int playerId) throws HearthStoneException{
        if(playerId != whoseTurn)
            throw new HearthStoneException("Not Your Turn");

        if (whoseTurn == player0.getPlayerId()) {
            player0.endTurn();

            whoseTurn = player1.getPlayerId();

            player0.setMyTurn(false);
            player1.setMyTurn(true);
            player1.startTurn();
        } else {
            player1.endTurn();

            whoseTurn = player0.getPlayerId();

            player0.setMyTurn(true);
            player1.setMyTurn(false);
            player0.startTurn();
        }
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public Player getFirstPlayer() {
        return player0;
    }

    public Player getSecondPlayer() {
        return player1;
    }

    public Player getPlayerById(int id) {
        if (player0.getPlayerId() == id) return player0;
        else return player1;
    }

    public boolean gameEnded() {
        return player0.getHero().getHealth() <= 0 || player1.getHero().getHealth() <= 0;
    }

    public void addCard(Card card) {
        synchronized (cardsLock) {
            cards.add(card);
        }
    }

    public void addHero(Hero hero) {
        synchronized (heroesLock) {
            heroes.add(hero);
        }
    }

    public void addWatcher(String username){
        synchronized (watchersLock) {
            watchers.add(new WatcherInfo(username));
        }
    }

    public void removeWatcher(String username){
        synchronized (watchersLock) {
            for (WatcherInfo info : watchers) {
                if (info.getUsername().equals(username)) {
                    watchers.remove(info);
                    return;
                }
            }
        }
    }

    public int getNewCardId() {
        synchronized (cardsLock) {
            return cards.size();
        }
    }

    public int getNewHeroId() {
        synchronized (heroesLock) {
            return heroes.size();
        }
    }

    public Card getCardById(int cardId) {
        synchronized (cardsLock) {
            for (Card card : cards) {
                if (card.getCardGameId() == cardId) {
                    return card;
                }
            }
            return null;
        }
    }

    public Hero getHeroById(int heroId) {
        synchronized (heroesLock) {
            for (Hero hero : heroes) {
                if (hero.getHeroGameId() == heroId)
                    return hero;
            }
            return null;
        }
    }

    public synchronized void foundObject(Object waitedCardId, Object founded) throws HearthStoneException {
        if (!(waitedCardId instanceof Card)) {
            throw new HearthStoneException("Strange Object founded!");
        }

        Card waitObject = getCardById(((Card) waitedCardId).getCardGameId());
        Object foundedObject;

        if (founded instanceof Hero) {
            foundedObject = getHeroById(((Hero) founded).getHeroGameId());
        } else if (founded instanceof Card) {
            foundedObject = getCardById(((Card) founded).getCardGameId());
        } else {
            throw new HearthStoneException("Strange Object founded!");
        }

        System.out.println(waitObject.getName() + " " + foundedObject + " " + waitObject);
        if(waitObject instanceof MinionCard){
            System.out.print(waitObject.getName() + " ");
            System.out.println(((MinionCard) waitObject).canAttack() + " " + ((MinionCard) waitObject).isCanAttack());
        }

        waitObject.found(foundedObject);

        HSServer.getInstance().updateGame(player0.getPlayerId());
    }

    public synchronized void chooseCardAbility(int cardGameId, Card card) {
        for (Card card1 : cards) {
            if (card1.getCardGameId() == cardGameId) {
                ((ChooseCardAbility) card1).doAfterChoosingCard(card);
                break;
            }
        }
    }

    public Player getPlayerByUsername(String username) {
        if(player0.getUsername().equals(username))
            return player0;
        else if(player1.getUsername().equals(username))
            return player1;
        return null;
    }
}
