package hearthstone.client.gui.game.play.boards;

import hearthstone.client.data.ClientData;
import hearthstone.client.gui.controls.dialogs.CardDialog;
import hearthstone.client.gui.controls.dialogs.ErrorDialog;
import hearthstone.client.gui.controls.dialogs.PassiveDialog;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.network.ClientMapper;
import hearthstone.models.card.Card;
import hearthstone.models.player.Player;
import hearthstone.models.player.PlayerModel;
import hearthstone.shared.GUIConfigs;
import hearthstone.shared.GameConfigs;
import hearthstone.util.Rand;
import hearthstone.util.SoundPlayer;

import java.util.ArrayList;

public class PracticeGameBoard extends GameBoard {
    private static PracticeGameBoard instance;

    private PracticeGameBoard(PlayerModel myPlayer, PlayerModel enemyPlayer) {
        super(myPlayer, enemyPlayer);
    }

    public static PracticeGameBoard makeInstance(PlayerModel myPlayer, PlayerModel enemyPlayer){
        return instance = new PracticeGameBoard(myPlayer, enemyPlayer);
    }

    public static PracticeGameBoard getInstance(){
        return instance;
    }

    @Override
    public void showPassiveDialogs(int playerId) {
        PassiveDialog passiveDialog = new PassiveDialog(
                GameFrame.getInstance(),
                Rand.getInstance().getRandomArray(
                        GameConfigs.initialPassives,
                        ClientData.basePassives.size())
        );
        ClientMapper.selectPassiveResponse(playerId, passiveDialog.getPassive());
    }

    @Override
    public void showCardDialog(int playerId, ArrayList<Card> cards) {
        CardDialog cardDialog0 = new CardDialog(
                GameFrame.getInstance(),
                cards);
        ArrayList<Card> selectedCards = cardDialog0.getCards();
        ArrayList<Integer> selectedId = new ArrayList<>();

        System.out.println("Salam1");

        for (Card card : selectedCards) {
            System.out.println(card.getName() + " " + card.getCardGameId());
            selectedId.add(card.getCardGameId());
        }

        System.out.println("Salam2 " + selectedId.size() + " " + playerId);

        ClientMapper.selectNotWantedCardsResponse(playerId, selectedId);
    }

    @Override
    public void gameEnded() {
        try {
            ErrorDialog errorDialog = new ErrorDialog(GameFrame.getInstance(), "Game ended!",
                    GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }

        beforeCloseBoard();
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
    }
}