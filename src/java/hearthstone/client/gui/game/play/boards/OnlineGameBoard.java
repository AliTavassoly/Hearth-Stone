package hearthstone.client.gui.game.play.boards;

import hearthstone.client.data.ClientData;
import hearthstone.client.gui.controls.buttons.CardButton;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.buttons.PassiveButton;
import hearthstone.client.gui.controls.dialogs.CardDialog;
import hearthstone.client.gui.controls.dialogs.MessageDialog;
import hearthstone.client.gui.controls.dialogs.PassiveDialog;
import hearthstone.client.gui.controls.dialogs.SureDialog;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.icons.WatchersIcon;
import hearthstone.client.gui.controls.interfaces.ShouldHovered;
import hearthstone.client.gui.controls.panels.ImagePanel;
import hearthstone.client.gui.controls.panels.WatchersPanel;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.play.controls.*;
import hearthstone.client.gui.util.Animation;
import hearthstone.client.gui.util.CustomScrollBarUI;
import hearthstone.client.network.ClientMapper;
import hearthstone.client.network.HSClient;
import hearthstone.models.WatcherInfo;
import hearthstone.models.card.Card;
import hearthstone.models.card.heropower.HeroPowerBehaviour;
import hearthstone.models.card.minion.MinionBehaviour;
import hearthstone.models.player.Player;
import hearthstone.models.player.PlayerModel;
import hearthstone.shared.GUIConfigs;
import hearthstone.shared.GameConfigs;
import hearthstone.util.CursorType;
import hearthstone.util.Rand;
import hearthstone.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class OnlineGameBoard extends GameBoard {
    private static OnlineGameBoard instance;

    private ImageButton watchersButton;

    protected WatchersPanel watchersPanel;
    protected JScrollPane watcherScroll;

    private OnlineGameBoard(PlayerModel myPlayer, PlayerModel enemyPlayer) {
        super(myPlayer, enemyPlayer);
    }

    public static OnlineGameBoard makeInstance(PlayerModel myPlayer, PlayerModel enemyPlayer) {
        return instance = new OnlineGameBoard(myPlayer, enemyPlayer);
    }

    public static OnlineGameBoard getInstance() {
        return instance;
    }

    protected void drawCardsOnHand(PlayerModel player, int handX, int handY) {
        ArrayList<Card> cards = player.getHand();
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

            if (player.getPlayerId() == myPlayer.getPlayerId()) {
                cardButton = new BoardCardButton(card,
                        GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, true, myPlayer.getPlayerId());

                makeCardOnHandMouseListener(cardButton,
                        startX + dis * (i - cards.size() / 2),
                        startY,
                        GUIConfigs.smallCardWidth,
                        GUIConfigs.smallCardHeight);
            } else {
                cardButton = new BoardCardButton(card,
                        GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, enemyPlayer.getPlayerId(), true);
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

                if (player.getPlayerId() == myPlayer.getPlayerId())
                    animateCard(myPickedCardX, myPickedCardY, GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, destination);
                else
                    animateCard(enemyPickedCardX, enemyPickedCardY, GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, destination);
            }
        }
    }

    @Override
    protected void makeIcons() {
        backButton = new ImageButton("icons/back.png",
                "icons/back_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png",
                "icons/minimize_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png",
                "icons/close_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        watchersButton = new WatchersIcon("icons/watchers.png",
                "icons/watchers_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        backButton.addActionListener(actionEvent -> {
            SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                    "Are you sure you want to exit game?! (you will lose current game)",
                    GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);
            boolean sure = sureDialog.getValue();
            if (sure) {
                ClientMapper.exitGameRequest(myPlayer.getPlayerId());
            }
        });

        watchersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!watcherScroll.isVisible()) {
                    ClientMapper.watchersRequest(HSClient.currentAccount.getUsername());
                }
                watcherScroll.setVisible(!watcherScroll.isVisible());
            }
        });
    }

    @Override
    protected void iconLayout() {
        // ICONS
        backButton.setBounds(leftIconX, startIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(backButton);

        minimizeButton.setBounds(leftIconX, endIconY - iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(leftIconX, endIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(closeButton);

        watchersButton.setBounds(rightIconX, startIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(watchersButton);

        makeWatchersPanel();
    }

    @Override
    public void showPassiveDialogs(int playerId) {
        PassiveDialog passiveDialog = new PassiveDialog(
                GameFrame.getInstance(),
                Rand.getInstance().getRandomArray(
                        GameConfigs.initialPassives,
                        ClientData.basePassives.size())
        );
        ClientMapper.selectPassiveResponse(myPlayer.getPlayerId(), passiveDialog.getPassive());
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
    protected void playCard(BoardCardButton button, Card card) {
        if (button.getPlayerId() == myPlayer.getPlayerId()) {
            ClientMapper.playCardRequest(myPlayer.getPlayerId(), card);
            restart();
        }
    }

    @Override
    protected void makeCardOnHandMouseListener(BoardCardButton button, int startX, int startY, int width, int height) {
        if (button.getCard().getPlayerId() == myPlayer.getPlayerId()) {
            super.makeCardOnHandMouseListener(button, startX, startY, width, height);
        }
    }

    @Override
    protected void makeCardOnLandMouseListener(BoardCardButton button, int startX, int startY) {
        if (button.getCard().getPlayerId() == myPlayer.getPlayerId()) {
            super.makeCardOnLandMouseListener(button, startX, startY);
            return;
        }

        CardButton bigCardButton = new CardButton(
                button.getCard(),
                GUIConfigs.medCardWidth,
                GUIConfigs.medCardHeight,
                -1);

        if (button.getPlayerId() == myPlayer.getPlayerId()) {
            bigCardButton.setBounds(
                    GUIConfigs.gameFrameWidth - GUIConfigs.medCardWidth,
                    GUIConfigs.gameFrameHeight - GUIConfigs.medCardHeight,
                    GUIConfigs.medCardWidth,
                    GUIConfigs.medCardHeight);
        } else {
            bigCardButton.setBounds(
                    GUIConfigs.gameFrameWidth - GUIConfigs.medCardWidth,
                    0,
                    GUIConfigs.medCardWidth,
                    GUIConfigs.medCardHeight);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    ClientMapper.foundObjectRequest(((Card) waitingObject).getPlayerId(), waitingObject, button.getCard());
                }
            }

            public void mouseEntered(MouseEvent e) {
                if (button.isShowBig()) {
                    add(bigCardButton);
                    updateUI();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (button.isShowBig()) {
                    remove(bigCardButton);
                    updateUI();
                }
            }
        });
    }

    @Override
    protected void makeHeroPowerMouseListener(HeroPowerButton button) {
        if (button.getCard().getPlayerId() == myPlayer.getPlayerId()) {
            super.makeHeroPowerMouseListener(button);
            return;
        }

        CardButton bigCardButton = new CardButton(
                button.getCard(),
                GUIConfigs.medCardWidth,
                GUIConfigs.medCardHeight,
                -1);

        if (button.getPlayerId() == myPlayer.getPlayerId()) {
            bigCardButton.setBounds(
                    GUIConfigs.gameFrameWidth - GUIConfigs.medCardWidth,
                    GUIConfigs.gameFrameHeight - GUIConfigs.medCardHeight,
                    GUIConfigs.medCardWidth,
                    GUIConfigs.medCardHeight);
        } else {
            bigCardButton.setBounds(
                    GUIConfigs.gameFrameWidth - GUIConfigs.medCardWidth,
                    0,
                    GUIConfigs.medCardWidth,
                    GUIConfigs.medCardHeight);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    ClientMapper.foundObjectRequest(((Card) waitingObject).getPlayerId(), waitingObject, button.getCard());
                }
            }

            public void mouseEntered(MouseEvent e) {
                if (button.isShowBig()) {
                    add(bigCardButton);
                    updateUI();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (button.isShowBig()) {
                    remove(bigCardButton);
                    updateUI();
                }
            }
        });
    }

    @Override
    protected void makeWeaponMouseListener(WeaponButton button) {
        if (button.getCard().getPlayerId() == myPlayer.getPlayerId()) {
            super.makeWeaponMouseListener(button);
            return;
        }

        CardButton bigCardButton = new CardButton(
                button.getCard(),
                GUIConfigs.medCardWidth,
                GUIConfigs.medCardHeight,
                -1);

        if (button.getPlayerId() == myPlayer.getPlayerId()) {
            bigCardButton.setBounds(
                    GUIConfigs.gameFrameWidth - GUIConfigs.medCardWidth,
                    GUIConfigs.gameFrameHeight - GUIConfigs.medCardHeight,
                    GUIConfigs.medCardWidth,
                    GUIConfigs.medCardHeight);
        } else {
            bigCardButton.setBounds(
                    GUIConfigs.gameFrameWidth - GUIConfigs.medCardWidth,
                    0,
                    GUIConfigs.medCardWidth,
                    GUIConfigs.medCardHeight);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    ClientMapper.foundObjectRequest(((Card) waitingObject).getPlayerId(), waitingObject, button.getCard());
                }
            }

            public void mouseEntered(MouseEvent e) {
                if (button.isShowBig()) {
                    add(bigCardButton);
                    updateUI();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (button.isShowBig()) {
                    remove(bigCardButton);
                    updateUI();
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
                return myPlayer.isMyTurn();
            }
        });

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (getWhoseTurn() != myPlayer.getPlayerId())
                    return;

                SoundPlayer soundPlayer = new SoundPlayer("/sounds/ding.wav");
                soundPlayer.playOnce();

                ClientMapper.endTurnRequest(myPlayer.getPlayerId());

                deleteCurrentMouseWaiting();

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

        myHero = new BoardHeroButton(myPlayer.getHero(),
                heroWidth, heroHeight, myPlayer.getPlayerId());
        makeHeroMouseListener(myHero);

        enemyHero = new BoardHeroButton(enemyPlayer.getHero(),
                heroWidth, heroHeight, enemyPlayer.getPlayerId());
        makeHeroMouseListener(enemyHero);

        myMessageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
        add(myMessageDialog);

        enemyMessageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
        add(enemyMessageDialog);
    }

    private void makeWatchersPanel() {
        ArrayList<WatcherInfo> watcherInfos = new ArrayList<>();

        watchersPanel = new WatchersPanel(watcherInfos, GUIConfigs.watcherInfoWidth,
                GUIConfigs.watcherInfoHeight);

        watcherScroll = new JScrollPane(watchersPanel);
        watcherScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        watcherScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        watcherScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        watcherScroll.setOpaque(false);
        watcherScroll.getViewport().setOpaque(true);
        watcherScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        watcherScroll.setBorder(null);

        watcherScroll.setBounds(rightIconX + GUIConfigs.iconWidth - GUIConfigs.watchersInfoListWidth,
                startIconY + GUIConfigs.iconHeight,
                GUIConfigs.watchersInfoListWidth,
                GUIConfigs.watchersInfoListHeight);
        add(watcherScroll);

        watcherScroll.setVisible(false);
    }

    public void updateWatchers(ArrayList<WatcherInfo> watchers) {
        watchersPanel.updateWatchers(watchers);
    }
}
