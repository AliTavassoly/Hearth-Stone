package hearthstone.client.gui.game.play.boards;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.client.data.GUIConfigs;
import hearthstone.server.data.GameConfigs;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.buttons.PassiveButton;
import hearthstone.client.gui.controls.dialogs.CardDialog;
import hearthstone.client.gui.controls.dialogs.MessageDialog;
import hearthstone.client.gui.controls.dialogs.PassiveDialog;
import hearthstone.client.gui.controls.interfaces.ShouldHovered;
import hearthstone.client.gui.controls.panels.ImagePanel;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.play.controls.*;
import hearthstone.client.gui.util.Animation;
import hearthstone.models.card.Card;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;
import hearthstone.util.SoundPlayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SoloGameBoard extends GameBoard {
    public SoloGameBoard(int myPlayerId, int enemyPlayerId) {
        super(myPlayerId, enemyPlayerId);
    }

    protected void drawCardsOnHand(int playerId, int handX, int handY) {
        ArrayList<Card> cards = Mapper.getHand(playerId);
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
                        GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, true, 0);

                makeCardOnHandMouseListener(cardButton,
                        startX + dis * (i - cards.size() / 2),
                        startY,
                        GUIConfigs.smallCardWidth,
                        GUIConfigs.smallCardHeight);
            } else {
                cardButton = new BoardCardButton(card,
                        GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, 1, true);
            }

            synchronized (animationLock) {
                if (animationsCard.contains(card.getCardGameId())) {
                    int ind = animationsCard.indexOf(card.getCardGameId());
                    Animation destination = animations.get(ind);
                    destination.setDestinationX(startX + dis * (i - cards.size() / 2));
                    destination.setDestinationY(startY);
                    continue;
                }
            }

            cardButton.setBounds(startX + dis * (i - cards.size() / 2),
                    startY,
                    GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight);

            add(cardButton);

            if (!animatedCardsInHand.contains(card.getCardGameId())) {
                animatedCardsInHand.add(card.getCardGameId());

                Animation destination = new Animation(startX + dis * (i - cards.size() / 2),
                        startY, cardButton);

                if (playerId == myPlayerId)
                    animateCard(myPickedCardX, myPickedCardY, GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, destination);
                else
                    animateCard(enemyPickedCardX, enemyPickedCardY, GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, destination);
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
        Mapper.setPassive(myPlayerId, passiveDialog0.getPassive());

        Mapper.passPassivesToAI(enemyPlayerId, Rand.getInstance().getRandomArray(
                GameConfigs.initialPassives,
                HearthStone.basePassives.size()));
    }

    @Override
    protected void showCardDialog() {
        CardDialog cardDialog0 = new CardDialog(
                GameFrame.getInstance(),
                Mapper.getTopCards(0, GameConfigs.initialDiscardCards));

        Mapper.removeInitialCards(0, cardDialog0.getCards(), GameConfigs.initialDiscardCards);
    }

    @Override
    protected void makeCardOnHandMouseListener(BoardCardButton button, int startX, int startY, int width, int height) {
        if(button.getCard().getPlayerId() == 0){
            super.makeCardOnHandMouseListener(button, startX, startY, width, height);
            return;
        }
    }

    @Override
    protected void makeCardOnLandMouseListener(BoardCardButton button, int startX, int startY) {
        if(button.getCard().getPlayerId() == 0){
            super.makeCardOnLandMouseListener(button, startX, startY);
            return;
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    try {
                        Mapper.foundObjectForObject(waitingObject, button.getCard());
                        deleteCurrentMouseWaiting();
                        Mapper.updateBoard();
                    } catch (HearthStoneException hse) {
                        showError(hse.getMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void makeHeroPowerMouseListener(HeroPowerButton button) {
        if(button.getCard().getPlayerId() == 0){
            super.makeHeroPowerMouseListener(button);
            return;
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    try {
                        Mapper.foundObjectForObject(waitingObject, button.getCard());
                        deleteCurrentMouseWaiting();
                        Mapper.updateBoard();
                    } catch (HearthStoneException hse) {
                        showError(hse.getMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void makeWeaponMouseListener(WeaponButton button) {
        if(button.getCard().getPlayerId() == 0){
            super.makeWeaponMouseListener(button);
            return;
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    try {
                        Mapper.foundObjectForObject(waitingObject, button.getCard());
                        deleteCurrentMouseWaiting();
                        Mapper.updateBoard();
                    } catch (HearthStoneException hse) {
                        showError(hse.getMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void makeGameStuff() {
        endTurnButton = new ImageButton("End Turn", "end_turn.png",
                "end_turn_hovered.png", 15, 1,
                GUIConfigs.endTurnButtonWidth, GUIConfigs.endTurnButtonHeight, new ShouldHovered() {
            @Override
            public boolean shouldHovered() {
                return Mapper.getWhoseTurn() == 0;
            }
        });

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Mapper.getWhoseTurn() != myPlayerId)
                    return;

                try {
                    hearthstone.util.Logger.saveLog("End turn", "Player " +
                            Mapper.getPlayerName(Mapper.getWhoseTurn()) +
                            " ended turn!");
                } catch (Exception e){
                    e.printStackTrace();
                }

                SoundPlayer soundPlayer = new SoundPlayer("/sounds/ding.wav");
                soundPlayer.playOnce();

                Mapper.endTurn();
                endTurnLineTimerTask.myStop();

                deleteCurrentMouseWaiting();

                drawEndTurnTimeLine();
                restart();
            }
        });

        sparkImage = new SparkImage(
                endTurnTimeLineStartX,
                endTurnTimeLineY - GUIConfigs.endTurnFireHeight / 2,
                GUIConfigs.endTurnFireWidth,
                GUIConfigs.endTurnFireHeight,
                "/images/spark_0.png");

        ropeImage = new ImagePanel("rope.png", GUIConfigs.endTurnRopeWidth + 7,
                GUIConfigs.endTurnRopeHeight, sparkImage.getX() + sparkImage.getWidth(),
                sparkImage.getY() + sparkImage.getHeight() / 2, true);
        add(ropeImage);

        myHero = new BoardHeroButton(Mapper.getHero(myPlayerId),
                heroWidth, heroHeight, 0);
        makeHeroMouseListener(myHero);

        enemyHero = new BoardHeroButton(Mapper.getHero(enemyPlayerId), heroWidth, heroHeight, 1); // enemy hero
        makeHeroMouseListener(enemyHero);

        myPassive = new PassiveButton(Mapper.getPassive(myPlayerId),
                GUIConfigs.medCardWidth,
                GUIConfigs.medCardHeight);

        myMessageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
        add(myMessageDialog);

        enemyMessageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
        add(enemyMessageDialog);
    }
}