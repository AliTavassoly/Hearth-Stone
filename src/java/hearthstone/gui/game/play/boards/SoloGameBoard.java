package hearthstone.gui.game.play.boards;

import hearthstone.HearthStone;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.dialogs.PassiveDialog;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.play.controls.BoardCardButton;
import hearthstone.logic.GameConfigs;
import hearthstone.models.card.Card;
import hearthstone.models.player.Player;
import hearthstone.util.Rand;

import java.util.ArrayList;

public class SoloGameBoard extends GameBoard {
    public SoloGameBoard(Player myPlayer, Player enemyPlayer) {
        super(myPlayer, enemyPlayer);
    }

    @Override
    protected void drawEnemyCardsOnHand() {
        ArrayList<Card> cards = enemyPlayer.getHand();
        if (cards.size() == 0)
            return;
        int dis = enemyHandDisCard / cards.size();

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton = new BoardCardButton(card, SizeConfigs.smallCardWidth,
                    SizeConfigs.smallCardHeight);

            cardButton.setBounds(enemyHandX + dis * (i - cards.size() / 2),
                    enemyHandY,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);

            if (!animatedCardsInEnemyHand.contains(card)) {
                animateCard(enemyPickedCardX, enemyPickedCardY,
                        enemyHandX + dis * (i - cards.size() / 2), enemyHandY,
                        cardButton);
                animatedCardsInEnemyHand.add(card);
            }
            add(cardButton);
        }
    }

    @Override
    protected void showPassiveDialogs() {
        PassiveDialog passiveDialog0 = new PassiveDialog(
                GameFrame.getInstance(),
                GameConfigs.initialPassives * SizeConfigs.medCardWidth + extraPassiveX,
                SizeConfigs.medCardHeight + extraPassiveY,
                Rand.getInstance().getRandomArray(
                        GameConfigs.initialPassives,
                        HearthStone.basePassives.size())
        );
        myPlayer.setPassive(passiveDialog0.getPassive());
        myPlayer.doPassives();
    }
}