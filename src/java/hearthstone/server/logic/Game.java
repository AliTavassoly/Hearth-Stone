package hearthstone.server.logic;

import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.player.Player;
import hearthstone.server.network.HSServer;
import hearthstone.server.network.ServerMapper;
import hearthstone.shared.GameConfigs;
import hearthstone.util.HearthStoneException;
import hearthstone.util.timer.HSBigTask;
import hearthstone.util.timer.HSTimerTask;

import java.util.*;

public class Game extends Thread{
    private Player player0, player1;
    private int whoseTurn;

    private ArrayList<Card> cards;
    private ArrayList<Hero> heroes;

    public Game(Player player0, Player player1, int id0, int id1) {
        this.player0 = player0;
        this.player1 = player1;

        this.player0.setPlayerId(id0);
        this.player1.setPlayerId(id1);

        this.player0.setEnemyPlayerId(id1);
        this.player1.setEnemyPlayerId(id0);

        cards = new ArrayList<>();
        heroes = new ArrayList<>();

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
                //Mapper.gameEnded(); ???????????????
            }

            @Override
            public boolean finishCondition() {
                return gameEnded();
            }
        }).start();
    }

    private void configGame() {
        whoseTurn = 0;

        player0.setGame(this);
        player1.setGame(this);

        player0.configPlayer();
        player1.configPlayer();
    }

    @Override
    public void run() {
        try {
            configGame();

            selectPassive();

            discardInitialCards();

            player0.setMyTurn(true);

            HSServer.getInstance().startGameOnGui(player0.getPlayerId());
            HSServer.getInstance().startGameOnGui(player1.getPlayerId());

            player0.startGame();

            player1.startGame();

            player0.startTurn();
        } catch (HearthStoneException e){
            e.printStackTrace();
        }
    }

    private void selectPassive() throws HearthStoneException {
        ServerMapper.selectPassiveRequest(HSServer.getInstance().getClientHandlerByPlayer(player0));
        ServerMapper.selectPassiveRequest(HSServer.getInstance().getClientHandlerByPlayer(player1));

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

    private void discardInitialCards() throws HearthStoneException{
        ServerMapper.selectNotWantedCardsRequest(player0.getTopCards(GameConfigs.initialDiscardCards), HSServer.getInstance().getClientHandlerByPlayer(player0));
        ServerMapper.selectNotWantedCardsRequest(player1.getTopCards(GameConfigs.initialDiscardCards), HSServer.getInstance().getClientHandlerByPlayer(player1));

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

    public void endTurn() {
        if (whoseTurn == 0) {
            try {
                player0.endTurn();
                player0.setMyTurn(false);

                whoseTurn = 1;

                player1.startTurn();
                player1.setMyTurn(true);
            } catch (HearthStoneException e) {
                e.printStackTrace();
            }
        } else {
            try {
                player1.endTurn();
                player1.setMyTurn(false);

                whoseTurn = 0;

                player0.startTurn();
                player0.setMyTurn(true);
            } catch (HearthStoneException e) {
                e.printStackTrace();
            }
        }
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public Player getPlayerById(int id) {
        if (id == 0) return player0;
        else return player1;
    }

    public boolean gameEnded() {
        return player0.getHero().getHealth() <= 0 || player1.getHero().getHealth() <= 0;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addHero(Hero hero) {
        heroes.add(hero);
    }

    public int getNewCardId() {
        return cards.size();
    }

    public int getNewHeroId() {
        return heroes.size();
    }

    public Card getCardById(int cardId) {
        for (Card card : cards) {
            if (card.getCardGameId() == cardId) {
                return card;
            }
        }
        return null;
    }

    public Hero getHeroById(int heroId){
        for(Hero hero: heroes){
            if(hero.getHeroGameId() == heroId)
                return hero;
        }
        return null;
    }

    public void foundObject(Object waitedCardId, Object founded) throws HearthStoneException{
        if(!(waitedCardId instanceof Card)){
            throw new HearthStoneException("Strange Object founded!");
        }

        Card waitObject = getCardById(((Card)waitedCardId).getCardGameId());
        Object foundedObject = null;

        if(founded instanceof Hero){
            foundedObject = getHeroById(((Hero)founded).getHeroGameId());
        }  else if (founded instanceof Card){
            foundedObject = getCardById(((Card)founded).getCardGameId());
        } else {
            throw new HearthStoneException("Strange Object founded!");
        }

        waitObject.found(foundedObject);
    }
}
