package hearthstone.gui.game.play.boards;

import hearthstone.DataTransform;
import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.dialogs.PassiveDialog;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.play.Animation;
import hearthstone.gui.game.play.controls.BoardCardButton;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.card.Card;
import hearthstone.util.Rand;

import java.util.ArrayList;

public class SoloGameBoard extends GameBoard {
    public SoloGameBoard(int myPlayerId, int enemyPlayerId) {
        super(myPlayerId, enemyPlayerId);
    }

    @Override
    protected void drawEnemyCardsOnHand() {
        ArrayList<Card> cards = DataTransform.getInstance().getHand(enemyPlayerId);
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
                animatedCardsInEnemyHand.add(card);

                Animation destination = new Animation(enemyHandX + dis * (i - cards.size() / 2),
                        enemyHandY, cardButton);

                animateCard(enemyPickedCardX, enemyPickedCardY, destination);
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
        DataTransform.getInstance().setPassive(myPlayerId, passiveDialog0.getPassive());
        Mapper.getInstance().doPassive(myPlayerId);
    }
}