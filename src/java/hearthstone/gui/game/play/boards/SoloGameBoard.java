package hearthstone.gui.game.play.boards;

import hearthstone.DataTransform;
import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.dialogs.CardDialog;
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


    protected void drawCardsOnHand(int playerId, int handX, int handY) {
        ArrayList<Card> cards = DataTransform.getInstance().getHand(playerId);
        if (cards.size() == 0)
            return;

        int dis = handDisCard / cards.size();
        int startX = handX;
        int startY = handY;

        if (cards.size() % 2 == 0) {
            startX += 25;
        }

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton;

            if (playerId == myPlayerId) {
                cardButton = new BoardCardButton(card,
                        SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, true, 0);

                makeCardOnHandMouseListener(cardButton,
                        startX + dis * (i - cards.size() / 2),
                        startY,
                        SizeConfigs.smallCardWidth,
                        SizeConfigs.smallCardHeight);
            } else {
                cardButton = new BoardCardButton(card,
                        SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, 1, true);
            }

            synchronized (animationsCard) {
                if (animationsCard.contains(card)) {
                    int ind = animationsCard.indexOf(card);
                    Animation destination = animations.get(ind);
                    destination.setDestinationX(startX + dis * (i - cards.size() / 2));
                    destination.setDestinationY(startY);
                    continue;
                }
            }

            cardButton.setBounds(startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);

            add(cardButton);

            if (!animatedCardsInHand.contains(card)) {
                animatedCardsInHand.add(card);

                Animation destination = new Animation(startX + dis * (i - cards.size() / 2),
                        startY, cardButton);

                if (playerId == myPlayerId)
                    animateCard(myPickedCardX, myPickedCardY, SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, destination);
                else
                    animateCard(enemyPickedCardX, enemyPickedCardY, SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, destination);
            }
        }
    }

    @Override
    protected void showPassiveDialogs() {
        PassiveDialog passiveDialog0 = new PassiveDialog(
                GameFrame.getInstance(),
                Rand.getInstance().getRandomArray(
                        GameConfigs.initialPassives,
                        HearthStone.basePassives.size())
        );
        Mapper.getInstance().setPassive(myPlayerId, passiveDialog0.getPassive());
        Mapper.getInstance().doPassive(myPlayerId);

        Mapper.getInstance().passPassivesToAI(enemyPlayerId, Rand.getInstance().getRandomArray(
                GameConfigs.initialPassives,
                HearthStone.basePassives.size()));
    }

    @Override
    protected void showCardDialog() {
        CardDialog cardDialog0 = new CardDialog(
                GameFrame.getInstance(),
                DataTransform.getInstance().getTopCards(0, GameConfigs.initialDiscardCards));

        Mapper.getInstance().removeInitialCards(0, cardDialog0.getCards(), GameConfigs.initialDiscardCards);
    }
}