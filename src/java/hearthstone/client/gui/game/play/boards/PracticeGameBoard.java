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
import hearthstone.shared.GUIConfigs;
import hearthstone.shared.GameConfigs;
import hearthstone.util.Rand;
import hearthstone.util.SoundPlayer;

import java.util.ArrayList;

public class PracticeGameBoard extends GameBoard {
    public PracticeGameBoard(Player myPlayer, Player enemyPlayer) {
        super(myPlayer, enemyPlayer);
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

        for (Card card : selectedCards) {
            selectedId.add(card.getCardGameId());
        }

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