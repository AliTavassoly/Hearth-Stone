package hearthstone.client.gui.game.play.boards;

import hearthstone.client.gui.controls.buttons.CardButton;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.buttons.PassiveButton;
import hearthstone.client.gui.controls.dialogs.*;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.interfaces.HaveCard;
import hearthstone.client.gui.controls.panels.ImagePanel;
import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.gui.game.play.controls.*;
import hearthstone.client.gui.util.Animation;
import hearthstone.client.network.ClientMapper;
import hearthstone.models.card.Card;
import hearthstone.models.card.heropower.HeroPowerBehaviour;
import hearthstone.models.card.minion.MinionBehaviour;
import hearthstone.models.player.Player;
import hearthstone.shared.GUIConfigs;
import hearthstone.shared.GameConfigs;
import hearthstone.util.*;
import hearthstone.util.getresource.ImageResource;
import hearthstone.util.timer.HSBigTask;
import hearthstone.util.timer.HSDelayTask;
import hearthstone.util.timer.HSDelayTimerTask;
import hearthstone.util.timer.HSTimerTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameBoard extends JPanel implements MouseListener {
    protected static GameBoard instance;

    protected ImageButton backButton, minimizeButton, closeButton;
    protected ImageButton endTurnButton;
    protected BoardHeroButton myHero;
    protected BoardHeroButton enemyHero;
    protected final SoundPlayer soundPlayer;

    protected Player myPlayer, enemyPlayer;

    protected PassiveButton myPassive;

    protected HSTimerTask endTurnLineTimerTask;
    protected SparkImage sparkImage;
    protected ImagePanel ropeImage;

    protected ArrayList<Integer> animatedCardsInHand;
    protected ArrayList<Integer> animatedCardsInLand;

    protected ArrayList<Integer> animationsCard;
    protected ArrayList<Animation> animations;
    protected final Object animationLock = new Object();

    protected boolean isLookingFor;
    protected Object waitingObject;

    protected static BufferedImage backgroundImage;
    protected static BufferedImage manaImage;

    protected MessageDialog myMessageDialog;
    protected MessageDialog enemyMessageDialog;

    // Finals START
    protected final int boardStartX = GUIConfigs.gameFrameWidth / 2 - 360;
    protected final int boardEndX = GUIConfigs.gameFrameWidth / 2 + 360;

    protected final int midX = GUIConfigs.gameFrameWidth / 2;
    protected final int midY = GUIConfigs.gameFrameHeight / 2 - 23;

    protected final int leftIconX = 20;
    protected final int rightIconX = GUIConfigs.gameFrameWidth - 20 - GUIConfigs.iconWidth;
    protected final int startIconY = 20;
    protected final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    protected final int iconsDis = 70;

    protected final int endTurnButtonX = 882;
    protected final int endTurnButtonY = 300;

    protected final int manaX = 770;
    protected final int manaY = 638;
    protected final int manaDis = 0;
    protected final int myManaStringX = 742;
    protected final int myManaStringY = 658;

    protected final int enemyManaStringX = 713;
    protected final int enemyManaStringY = 53;

    protected final int myHeroX = midX - 60;
    protected final int myHeroY = GUIConfigs.gameFrameHeight - 236;

    protected final int myHeroPowerX = midX + 52;
    protected final int myHeroPowerY = GUIConfigs.gameFrameHeight - 220;

    protected final int enemyHeroPowerX = midX + 52;
    protected final int enemyHeroPowerY = 90;

    protected final int myWeaponX = midX - 165;
    protected final int myWeaponY = GUIConfigs.gameFrameHeight - 220;

    protected final int enemyWeaponX = midX - 165;
    protected final int enemyWeaponY = 90;

    protected final int heroWidth = GUIConfigs.medHeroWidth;
    protected final int heroHeight = GUIConfigs.medHeroHeight;

    protected final int enemyHeroX = midX - 60;
    protected final int enemyHeroY = 60;

    protected final int myHandX = GUIConfigs.gameFrameWidth / 2 - 40;
    protected final int myHandY = GUIConfigs.gameFrameHeight - 80;
    protected final int handDisCard = 220;

    protected final int enemyHandX = GUIConfigs.gameFrameWidth / 2 - 40;
    protected final int enemyHandY = -65;

    protected final int myLandStartY = midY + 10;
    protected final int myLandEndY = midY + 180;
    protected final int myLandX = midX;
    protected final int myLandY = myLandStartY;
    protected final int landDisCard = 115;

    protected final int enemyLandStartY = midY - 120;
    protected final int enemyLandEndY = midY;
    protected final int enemyLandX = midX;
    protected final int enemyLandY = enemyLandStartY;

    protected final int myDeckCardsNumberX = midX + 450;
    protected final int myDeckCardsNumberY = midY + 105;

    protected final int enemyDeckCardsNumberX = midX + 450;
    protected final int enemyDeckCardsNumberY = midY - 95;

    protected int myPickedCardX = myDeckCardsNumberX;
    protected int myPickedCardY = myDeckCardsNumberY;

    protected int enemyPickedCardX = enemyDeckCardsNumberX;
    protected int enemyPickedCardY = enemyDeckCardsNumberY - GUIConfigs.smallCardHeight - 27;

    protected final int endTurnTimeLineStartX = boardStartX + 10;
    protected final int endTurnTimeLineEndX = endTurnButtonX - 15;
    protected final int endTurnTimeLineY = midY;

    protected final int myErrorX = 615;
    protected final int myErrorY = 530;

    protected final int enemyErrorX = 200;
    protected final int enemyErrorY = 70;

    protected final int myRewardX = 0;
    protected final int myRewardY = 315;

    protected final int spellDestinationX = 180;
    protected final int spellDestinationY = midY - GUIConfigs.medCardHeight / 2;

    protected final int enemyRewardX = 0;
    protected final int enemyRewardY = 130;

    // Finals END

    protected GameBoard(Player myPlayer, Player enemyPlayer) {
        this.myPlayer = myPlayer;
        this.enemyPlayer = enemyPlayer;

        animatedCardsInHand = new ArrayList<>();
        animatedCardsInLand = new ArrayList<>();

        animationsCard = new ArrayList<>();
        animations = new ArrayList<>();

        soundPlayer = new SoundPlayer("/sounds/play_background.wav");
        CredentialsFrame.getInstance().stopSound();
        soundPlayer.loopPlay();

        configPanel();

        makeIcons();

        iconLayout();

        makeGameStuff();

        gameStuffLayoutBeforeStartGame();

        addMouseListener(this);
    }

    public static GameBoard makeInstance(Player myPlayer, Player enemyPlayer) {
        return instance = new GameBoard(myPlayer, enemyPlayer);
    }

    public static GameBoard getInstance() {
        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        try {
            if (backgroundImage == null) {
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/game_board_background.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.drawImage(backgroundImage, 0, 0, null);

        drawMyMana(g2, myPlayer.getMana(),
                myPlayer.getTurnNumber());

        drawEnemyMana(g2, enemyPlayer.getMana(),
                enemyPlayer.getTurnNumber());

        drawDeckNumberOfCards(g2, myDeckCardsNumberX, myDeckCardsNumberY,
                myPlayer.getDeck().getCards().size());
        drawDeckNumberOfCards(g2, enemyDeckCardsNumberX, enemyDeckCardsNumberY,
                enemyPlayer.getDeck().getCards().size());

        g2.drawImage(sparkImage.getImage().getScaledInstance(
                sparkImage.getWidth(), sparkImage.getHeight(),
                Image.SCALE_SMOOTH),
                sparkImage.getX(), sparkImage.getY(),
                sparkImage.getWidth(), sparkImage.getHeight(),
                null);
    }

    protected void configPanel() {
        setSize(new Dimension(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight));
        setLayout(null);
        setDoubleBuffered(true);
        setVisible(true);
    }

    public void startGameOnGui(Player myPlayer, Player enemyPlayer) {
        this.myPlayer = myPlayer;
        this.enemyPlayer = enemyPlayer;

        gameStuffLayout();

        drawEndTurnTimeLine();
    }

    public void showPassiveDialogs(int playerId) { }

    public void showCardDialog(int playerId, ArrayList<Card> cards) { }

    // DRAW MANA
    protected void drawMyMana(Graphics2D g, int number, int maxNumber) {
        int fontSize = 25;
        maxNumber = Math.min(maxNumber, GameConfigs.maxManaInGame);

        for (int i = 0; i < number; i++) {
            try {
                if (manaImage == null)
                    manaImage = ImageResource.getInstance().getImage(
                            "/images/mana.png");
                g.drawImage(manaImage.getScaledInstance(GUIConfigs.manaWidth, GUIConfigs.manaHeight,
                        Image.SCALE_SMOOTH),
                        manaX + (GUIConfigs.manaWidth + manaDis) * i, manaY,
                        GUIConfigs.manaWidth, GUIConfigs.manaHeight, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String text = number + "/" + maxNumber;

        if (maxNumber == 10)
            fontSize = 19;

        g.setFont(GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, fontSize));
        g.drawString(text,
                myManaStringX - fontMetrics.stringWidth(text) / 2, myManaStringY);
    }

    protected void drawEnemyMana(Graphics2D g, int number, int maxNumber) {
        int fontSize = 25;
        maxNumber = Math.min(maxNumber, GameConfigs.maxManaInGame);

        String text = number + "/" + maxNumber;

        if (maxNumber == 10)
            fontSize = 19;

        g.setFont(GameFrame.getInstance().getCustomFont(FontType.NUMBER, 0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(FontType.NUMBER, 0, fontSize));
        g.drawString(text,
                enemyManaStringX - fontMetrics.stringWidth(text) / 2, enemyManaStringY);
    }
    // DRAW MANA

    // DRAW DECK NUMBER
    protected void drawDeckNumberOfCards(Graphics2D g, int X, int Y, int number) {
        int fontSize = 50;

        g.setFont(GameFrame.getInstance().getCustomFont(FontType.NUMBER, 0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(FontType.NUMBER, 0, fontSize));

        g.drawString(String.valueOf(number),
                X - fontMetrics.stringWidth(String.valueOf(number)) / 2,
                Y);
    }
    // DRAW DECK NUMBER

    // DRAW REWARD
    protected void drawReward(Player player, int X, int Y) {
        if (player.getReward() == null)
            return;

        BoardRewardButton rewardButton = new BoardRewardButton(
                player.getReward(),
                GUIConfigs.medCardWidth,
                GUIConfigs.medCardHeight);

        rewardButton.setBounds(X, Y,
                GUIConfigs.medCardWidth, GUIConfigs.medCardHeight);

        add(rewardButton);
    }
    // DRAW REWARD

    // DRAW HEROPOWER
    protected void drawHeroPower(Player player, int X, int Y) {
        if (player.getHeroPower() == null)
            return;
        HeroPowerButton heroPowerButton = new HeroPowerButton(player.getHeroPower(),
                GUIConfigs.heroPowerWidth, GUIConfigs.heroPowerHeight, true, player.getPlayerId());

        makeHeroPowerMouseListener(heroPowerButton);

        heroPowerButton.setBounds(X, Y,
                GUIConfigs.heroPowerWidth, GUIConfigs.heroPowerHeight);

        add(heroPowerButton);
    }
    // DRAW HEROPOWER

    // DRAW WEAPON
    protected void drawWeapon(Player player, int X, int Y) {
        if (player.getWeapon() == null)
            return;

        WeaponButton weaponButton = new WeaponButton(player.getWeapon(),
                GUIConfigs.weaponWidth, GUIConfigs.weaponHeight, true, player.getPlayerId());
        weaponButton.setBounds(X, Y,
                GUIConfigs.weaponWidth, GUIConfigs.weaponHeight);

        makeWeaponMouseListener(weaponButton);

        weaponButton.setBounds(X, Y,
                GUIConfigs.weaponWidth, GUIConfigs.weaponHeight);

        add(weaponButton);
    }
    // DRAW WEAPON

    // DRAW CARDS
    protected synchronized void drawCardsOnHand(Player player, int handX, int handY) {
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
                        GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, true, player.getPlayerId());
            } else {
                cardButton = new BoardCardButton(card,
                        GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, 180, true, player.getPlayerId());
            }

            makeCardOnHandMouseListener(cardButton,
                    startX + dis * (i - cards.size() / 2),
                    startY,
                    GUIConfigs.smallCardWidth,
                    GUIConfigs.smallCardHeight);

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

    protected void drawCardsOnLand(Player player, int landX, int landY) {
        ArrayList<Card> cards = player.getLand();
        if (cards.size() == 0)
            return;

        int dis = landDisCard;
        int startX = landX;
        int startY = landY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton;

            if (myPlayer.getPlayerId() == player.getPlayerId()) {
                cardButton = new BoardCardButton(card,
                        GUIConfigs.smallCardWidthOnLand, GUIConfigs.smallCardHeightOnLand, true, myPlayer.getPlayerId(), true);
            } else {
                cardButton = new BoardCardButton(card,
                        GUIConfigs.smallCardWidthOnLand, GUIConfigs.smallCardHeightOnLand, true, enemyPlayer.getPlayerId(), true);
            }

            makeCardOnLandMouseListener(cardButton,
                    startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? GUIConfigs.smallCardWidthOnLand / 2 : 0),
                    startY);

            synchronized (animationLock) {
                if (animationsCard.contains(card.getCardGameId())) {
                    int ind = animationsCard.indexOf(card.getCardGameId());
                    Animation animation = animations.get(ind);

                    remove(animation.getComponent());
                    add(cardButton);

                    animation.setDestinationX(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? GUIConfigs.smallCardWidthOnLand / 2 : 0));
                    animation.setDestinationY(startY);
                    animation.setComponent(cardButton);

                    animatedCardsInLand.add(card.getCardGameId());
                    continue;
                }
            }

            cardButton.setBounds(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? GUIConfigs.smallCardWidthOnLand / 2 : 0),
                    startY,
                    GUIConfigs.smallCardWidthOnLand, GUIConfigs.smallCardHeightOnLand);

            if (!animatedCardsInLand.contains(card.getCardGameId())) {

                animatedCardsInLand.add(card.getCardGameId());

                Animation destination = new Animation(startX + dis * (i - cards.size() / 2)
                        - (cards.size() % 2 == 1 ? GUIConfigs.smallCardWidthOnLand / 2 : 0),
                        startY, cardButton);

                if (player.getPlayerId() == myPlayer.getPlayerId())
                    animateCard(myHandX, myHandY, GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, destination);
                else
                    animateCard(enemyHandX, enemyHandY, GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight, destination);
            }
            add(cardButton);
        }
    }
    // DRAW CARDS

    // END TURN LINE
    protected void drawEndTurnTimeLine() {
        int totalX = endTurnTimeLineEndX - endTurnTimeLineStartX - sparkImage.getWidth();
        long length = 90000;
        long period = length / totalX * 2;
        long warningTime = 9000;

        final int[] xSpark = {endTurnTimeLineStartX};
        final int[] ySpark = {endTurnTimeLineY - GUIConfigs.endTurnFireHeight / 2};

        final int sparkWidth = sparkImage.getWidth();
        final int sparkHeight = sparkImage.getHeight();

        SoundPlayer warningPlayer = new SoundPlayer("/sounds/countdown.wav");

        if (endTurnLineTimerTask != null) {
            endTurnLineTimerTask.myStop();
            endTurnLineTimerTask.interrupt();
        }

        endTurnLineTimerTask = new HSTimerTask(period, length, warningTime, new HSBigTask() {
            public void startFunction() {
            }

            @Override
            public void periodFunction() {
                sparkImage.setImagePath("/images/spark_" + (Rand.getInstance().getRandomNumber(10) % 10) + ".png");

                xSpark[0] += 2;

                sparkImage.setX(xSpark[0]);
                sparkImage.setY(ySpark[0]);

                ropeImage.setBounds(xSpark[0] + sparkWidth - 7, ySpark[0] + sparkHeight / 2 - 7,
                        endTurnTimeLineEndX - (xSpark[0] + sparkWidth) + 5,
                        GUIConfigs.endTurnRopeHeight);

                repaint();
                revalidate();
            }

            @Override
            public void warningFunction() {
                warningPlayer.playOnce();
            }

            @Override
            public void finishedFunction() {
                endTurnButton.doClick();
            }

            @Override
            public void closeFunction() {
                warningPlayer.stop();
                warningPlayer.close();
            }

            @Override
            public boolean finishCondition() {
                return false;
            }
        });
        endTurnLineTimerTask.start();
    }
    // END TURN LINE

    // ANIMATIONS
    protected void animateCard(int startX, int startY, int width, int height,
                               Animation animation) {
        long period = 25;

        final int[] x = {startX};
        final int[] y = {startY};

        new HSTimerTask(period, new HSBigTask() {
            protected void addComponentIfNot(Component component) {
                for (Component component1 : GameBoard.this.getComponents()) {
                    if (component1 == component)
                        return;
                }
                GameBoard.this.add(component);
            }

            @Override
            public void startFunction() {
                synchronized (animationLock) {
                    animationsCard.add(((HaveCard) animation.getComponent()).getCard().getCardGameId());
                    animations.add(animation);

                    animation.getComponent().setBounds(startX, startY, width, height);
                    add(animation.getComponent());
                }
            }

            @Override
            public void periodFunction() {
                int jump = 5;
                int plusX = jump;
                int plusY = jump;

                if (Math.abs(animation.getDestinationX() - x[0]) < jump)
                    plusX = 1;
                if (Math.abs(animation.getDestinationY() - y[0]) < jump)
                    plusY = 1;

                x[0] += (animation.getDestinationX() - x[0] != 0 ? plusX * (animation.getDestinationX() - x[0]) / Math.abs(animation.getDestinationX() - x[0]) : 0);
                y[0] += (animation.getDestinationY() - y[0] != 0 ? plusY * (animation.getDestinationY() - y[0]) / Math.abs(animation.getDestinationY() - y[0]) : 0);

                animation.getComponent().setBounds(x[0], y[0], width, height);
                addComponentIfNot(animation.getComponent());
            }

            @Override
            public void warningFunction() {
            }

            @Override
            public void finishedFunction() {
            }

            @Override
            public void closeFunction() {
                new HSDelayTimerTask(animation.getRemoveDelayAfterArrived(), new HSDelayTask() {
                    @Override
                    public void delayAction() {
                        synchronized (animationLock) {
                            int ind = animationsCard.indexOf(((HaveCard) animation.getComponent()).getCard().getCardGameId());
                            animationsCard.remove(ind);
                            animations.remove(ind);

                            if (animationsCard.size() == 0) {
                                //Mapper.updateBoard();
                                restart();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public boolean finishCondition() {
                return x[0] == animation.getDestinationX() && y[0] == animation.getDestinationY();
            }
        }).start();
    }

    protected void animateCardWithResize(int startX, int startY,
                                         int startWidth, int startHeight,
                                         Animation animation) {
        long period = 20;

        final int[] x = {startX};
        final int[] y = {startY};

        final int[] width = {startWidth};
        final int[] height = {startHeight};

        new HSTimerTask(period, new HSBigTask() {
            protected void addComponentIfNot(Component component) {
                for (Component component1 : GameBoard.this.getComponents()) {
                    if (component1 == component)
                        return;
                }
                GameBoard.this.add(component);
            }

            @Override
            public void startFunction() {
                synchronized (animationLock) {
                    animationsCard.add(((CardButton) animation.getComponent()).getCard().getCardGameId());
                    animations.add(animation);

                    animation.getComponent().setBounds(startX, startY, startWidth, startHeight);
                    add(animation.getComponent());
                }
            }

            @Override
            public void periodFunction() {
                int jumpLoc = 4;
                int plusX = jumpLoc;
                int plusY = jumpLoc;

                int jumpSize = 1;
                int plusWidth = jumpSize;
                int plusHeight = jumpSize;

                if (Math.abs(animation.getDestinationX() - x[0]) < jumpLoc)
                    plusX = 1;
                if (Math.abs(animation.getDestinationY() - y[0]) < jumpLoc)
                    plusY = 1;

                x[0] += (animation.getDestinationX() - x[0] != 0 ? plusX * (animation.getDestinationX() - x[0]) / Math.abs(animation.getDestinationX() - x[0]) : 0);
                y[0] += (animation.getDestinationY() - y[0] != 0 ? plusY * (animation.getDestinationY() - y[0]) / Math.abs(animation.getDestinationY() - y[0]) : 0);

                width[0] += (animation.getDestinationWidth() - width[0] != 0 ? plusWidth * (animation.getDestinationWidth() - width[0]) / Math.abs(animation.getDestinationWidth() - width[0]) : 0);
                height[0] += (animation.getDestinationHeight() - height[0] != 0 ? plusHeight * (animation.getDestinationHeight() - height[0]) / Math.abs(animation.getDestinationHeight() - height[0]) : 0);

                ((CardButton) animation.getComponent()).setMySize(width[0], height[0]);
                animation.getComponent().setBounds(x[0], y[0], width[0], height[0]);
                addComponentIfNot(animation.getComponent());
            }

            @Override
            public void warningFunction() {
            }

            @Override
            public void finishedFunction() {
            }

            @Override
            public void closeFunction() {
                new HSDelayTimerTask(3000, new HSDelayTask() {
                    @Override
                    public void delayAction() {
                        synchronized (animationLock) {
                            int ind = animationsCard.indexOf(((CardButton) animation.getComponent()).getCard().getCardGameId());
                            animationsCard.remove(ind);
                            animations.remove(ind);

                            if (animationsCard.size() == 0) {
                                restart();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public boolean finishCondition() {
                return x[0] == animation.getDestinationX() &&
                        y[0] == animation.getDestinationY() &&
                        width[0] == animation.getDestinationWidth() &&
                        height[0] == animation.getDestinationHeight();
            }
        }).start();
    }
    // ANIMATIONS

    // MOUSE LISTENERS
    protected void makeCardOnHandMouseListener(BoardCardButton button,
                                               int startX, int startY, int width, int height) {
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
            public void mouseEntered(MouseEvent e) {
                if (button.isShowBig()) {
                    add(bigCardButton);
                    if (button.getPlayerId() == myPlayer.getPlayerId())
                        button.setBounds(button.getX(), button.getY() - 47, button.getWidth(), button.getHeight());
                    else
                        button.setBounds(button.getX(), button.getY() + 47, button.getWidth(), button.getHeight());
                    updateUI();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (button.isShowBig()) {
                    remove(bigCardButton);
                    if (button.getPlayerId() == myPlayer.getPlayerId() && button.getY() != startY)
                        button.setBounds(button.getX(), button.getY() + 47, button.getWidth(), button.getHeight());
                    else if (button.getPlayerId() == enemyPlayer.getPlayerId() && button.getY() != startY)
                        button.setBounds(button.getX(), button.getY() - 47, button.getWidth(), button.getHeight());
                    updateUI();
                }
            }

            public void mouseReleased(MouseEvent E) {
                if (isInMyLand(E.getX() + button.getX(), E.getY() + button.getY()) &&
                        getWhoseTurn() == button.getCard().getPlayerId() &&
                        button.getCard().getPlayerId() == myPlayer.getPlayerId()) {
                    playCard(button, button.getCard());
                } else if (isInEnemyLand(E.getX() + button.getX(), E.getY() + button.getY()) &&
                        getWhoseTurn() == button.getCard().getPlayerId() &&
                        button.getCard().getPlayerId() == enemyPlayer.getPlayerId()) {
                    playCard(button, button.getCard());
                } else {
                    button.setBounds(startX, startY, width, height);
                }
            }
        });

        button.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if ((getWhoseTurn() != button.getPlayerId()))
                    return;

                int newX = e.getX() + button.getX();
                int newY = e.getY() + button.getY();
                button.setBounds(newX - width / 2, newY - height / 2, width, height);
            }
        });
    }

    protected void makeCardOnLandMouseListener(BoardCardButton button,
                                               int startX, int startY) {
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
                } else {
                    if (button.getPlayerId() == getWhoseTurn()
                            && ((MinionBehaviour) button.getCard()).isCanAttack()) {
                        makeNewMouseWaiting(CursorType.ATTACK, button.getCard());
                    }
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

    protected void makeWeaponMouseListener(WeaponButton button) {
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
                } else {
                    if (button.getPlayerId() == getWhoseTurn()
                            && (button.getCard()).isCanAttack()) {
                        makeNewMouseWaiting(CursorType.ATTACK, button.getCard());
                    }
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

    protected void makeHeroPowerMouseListener(HeroPowerButton button) {
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
                } else {
                    if (button.getPlayerId() == getWhoseTurn()
                            && ((HeroPowerBehaviour) button.getCard()).isCanAttack()) {
                        makeNewMouseWaiting(((HeroPowerBehaviour) button.getCard()).lookingForCursorType(),
                                button.getCard());
                    }
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

    protected void makeHeroMouseListener(BoardHeroButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    ClientMapper.foundObjectRequest(((Card) waitingObject).getPlayerId(), waitingObject, button.getHero());
                }
            }
        });
    }
    // MOUSE LISTENERS

    protected void playCard(BoardCardButton button, Card card) {
        ClientMapper.playCardRequest(card.getPlayerId(), card);
        restart();
    }

    public void animateSpell(Card card) {
        for (Component component : this.getComponents()) {
            if (component instanceof HaveCard && ((HaveCard) component).getCard().getCardGameId() == card.getCardGameId()) {
                this.remove(component);
            }
        }

        if (card.getPlayerId() == myPlayer.getPlayerId()) {
            animateCardWithResize(myHandX, myHandY, GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight,
                    new Animation(spellDestinationX, spellDestinationY,
                            GUIConfigs.medCardWidth, GUIConfigs.medCardHeight,
                            3000, new CardButton(card, false, GUIConfigs.medCardWidth, GUIConfigs.medCardHeight, -1)));
        } else {
            animateCardWithResize(enemyHandX, enemyHandY, GUIConfigs.smallCardWidth, GUIConfigs.smallCardHeight,
                    new Animation(spellDestinationX, spellDestinationY,
                            GUIConfigs.medCardWidth, GUIConfigs.medCardHeight,
                            3000, new CardButton(card, false, GUIConfigs.medCardWidth, GUIConfigs.medCardHeight, -1)));
        }
    }

    public void removeCardAnimation(Card card) {
        synchronized (animationLock) {
            if (animationsCard.contains(card.getCardGameId())) {
                int ind = animationsCard.indexOf(card.getCardGameId());
                this.remove(animations.get(ind).getComponent());
            }
        }
    }

    public void makeNewMouseWaiting(CursorType cursorType, Card card) {
        waitingObject = card;
        isLookingFor = true;

        switch (cursorType) {
            case NORMAL:
                GameFrame.getInstance().setCursor("/images/normal_cursor.png");
                break;
            case SEARCH:
                GameFrame.getInstance().setCursor("/images/search_cursor.png");
                break;
            case ATTACK:
                GameFrame.getInstance().setCursor("/images/attack_cursor.png");
                break;
            case HEAL:
                GameFrame.getInstance().setCursor("/images/heal_cursor.png");
                break;
            case FREEZE:
                GameFrame.getInstance().setCursor("/images/freeze_cursor.png");
                break;
        }
    }

    public void deleteCurrentMouseWaiting() {
        isLookingFor = false;
        waitingObject = null;

        GameFrame.getInstance().setCursor("/images/normal_cursor.png");
    }

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

        backButton.addActionListener(actionEvent -> {
            SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                    "Are you sure you want to exit game?! (you will lose current game)",
                    GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);
            boolean sure = sureDialog.getValue();
            if (sure) {
                ClientMapper.exitGameRequest(myPlayer.getPlayerId());
            }
        });
    }

    protected void makeGameStuff() {
        endTurnButton = new ImageButton("End Turn", "end_turn.png",
                "end_turn_hovered.png", 15, 1,
                GUIConfigs.endTurnButtonWidth, GUIConfigs.endTurnButtonHeight);
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SoundPlayer soundPlayer = new SoundPlayer("/sounds/ding.wav");
                soundPlayer.playOnce();

                ClientMapper.endTurnRequest(getWhoseTurn());
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

        myHero = new BoardHeroButton(myPlayer.getHero(),
                heroWidth, heroHeight, myPlayer.getPlayerId());
        makeHeroMouseListener(myHero);

        enemyHero = new BoardHeroButton(enemyPlayer.getHero(),
                heroWidth, heroHeight, enemyPlayer.getPlayerId());
        makeHeroMouseListener(enemyHero);

        myPassive = new PassiveButton(myPlayer.getPassive(),
                GUIConfigs.medCardWidth,
                GUIConfigs.medCardHeight);

        myMessageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
        add(myMessageDialog);

        enemyMessageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
        add(enemyMessageDialog);
    }

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
    }

    protected void gameStuffLayoutBeforeStartGame() {
        endTurnButton.setBounds(endTurnButtonX, endTurnButtonY,
                GUIConfigs.endTurnButtonWidth, GUIConfigs.endTurnButtonHeight);
        add(endTurnButton);

        myHero.setBounds(myHeroX, myHeroY,
                heroWidth, heroHeight);
        add(myHero);

        enemyHero.setBounds(enemyHeroX, enemyHeroY,
                heroWidth, heroHeight);
        add(enemyHero);
    }

    protected void gameStuffLayout() {
        drawCardsOnHand(myPlayer, myHandX, myHandY);
        drawCardsOnHand(enemyPlayer, enemyHandX, enemyHandY);

        drawCardsOnLand(myPlayer, myLandX, myLandY);
        drawCardsOnLand(enemyPlayer, enemyLandX, enemyLandY);

        drawHeroPower(myPlayer, myHeroPowerX, myHeroPowerY);
        drawHeroPower(enemyPlayer, enemyHeroPowerX, enemyHeroPowerY);

        drawWeapon(myPlayer, myWeaponX, myWeaponY);
        drawWeapon(enemyPlayer, enemyWeaponX, enemyWeaponY);

        drawReward(myPlayer, myRewardX, myRewardY);
        drawReward(enemyPlayer, enemyRewardX, enemyRewardY);

        endTurnButton.setBounds(endTurnButtonX, endTurnButtonY,
                GUIConfigs.endTurnButtonWidth, GUIConfigs.endTurnButtonHeight);
        add(endTurnButton);

        myHero = new BoardHeroButton(myPlayer.getHero(),
                heroWidth, heroHeight, myPlayer.getPlayerId());
        makeHeroMouseListener(myHero);

        enemyHero = new BoardHeroButton(enemyPlayer.getHero(),
                heroWidth, heroHeight, enemyPlayer.getPlayerId());
        makeHeroMouseListener(enemyHero);
        myHero.setBounds(myHeroX, myHeroY,
                heroWidth, heroHeight);
        add(myHero);

        enemyHero.setBounds(enemyHeroX, enemyHeroY,
                heroWidth, heroHeight);
        add(enemyHero);
    }

    protected boolean isInMyLand(int x, int y) {
        return x >= boardStartX && x <= boardEndX && y >= myLandStartY && y <= myLandEndY;
    }

    protected boolean isInEnemyLand(int x, int y) {
        return x >= boardStartX && x <= boardEndX && y >= enemyLandStartY && y <= enemyLandEndY;
    }

    public synchronized void showError(String text) {
        if (getWhoseTurn() == myPlayer.getPlayerId()) {
            myMessageDialog.setText(text);
            myMessageDialog.setImagePath("/images/my_think_dialog.png");
            myMessageDialog.setBounds(myErrorX, myErrorY,
                    GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
            myMessageDialog.setVisibility(true);
        } else {
            enemyMessageDialog.setText(text);
            enemyMessageDialog.setImagePath("/images/enemy_think_dialog.png");
            enemyMessageDialog.setBounds(enemyErrorX, enemyErrorY,
                    GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
            enemyMessageDialog.setVisibility(true);
        }
    }

    public void gameEnded() {
        if (myPlayer.getHero().getHealth() <= 0) {
            try {
                SoundPlayer loseSound = new SoundPlayer("/sounds/lose.wav");

                ErrorDialog errorDialog = new ErrorDialog(GameFrame.getInstance(), "Ooops! You lost game :(",
                        GUIConfigs.dialogWidth, GUIConfigs.dialogHeight, loseSound);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                SoundPlayer victorySound = new SoundPlayer("/sounds/victory.wav");

                ErrorDialog errorDialog = new ErrorDialog(GameFrame.getInstance(), "congratulations! You won game!",
                        new Color(38, 104, 23),
                        GUIConfigs.dialogWidth, GUIConfigs.dialogHeight, victorySound);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        beforeCloseBoard();
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
    }

    protected void beforeCloseBoard() {
        endTurnLineTimerTask.myStop();
        endTurnLineTimerTask.interrupt();

        CredentialsFrame.getInstance().playSound();
        soundPlayer.stop();

        deleteCurrentMouseWaiting();
    }

    protected void removeComponents() {
        for (Component component : this.getComponents()) {
            if (component instanceof ImagePanel &&
                    ((ImagePanel) component).getImagePath().contains("spark"))
                continue;
            if (component instanceof ImagePanel && ((ImagePanel) component).isRTL())
                continue;

            if (component instanceof HaveCard) {
                HaveCard cardButton = (HaveCard) component;
                synchronized (animationLock) {
                    if (animationsCard.contains(cardButton.getCard().getCardGameId())) {
                        continue;
                    }
                }
            }

            if (component instanceof MessageDialog)
                continue;
            this.remove(component);
        }
    }

    public int getWhoseTurn() {
        if (myPlayer.isMyTurn())
            return myPlayer.getPlayerId();
        return enemyPlayer.getPlayerId();
    }

    public Player getPlayerById(int playerId) {
        if (myPlayer.getPlayerId() == playerId)
            return myPlayer;
        return enemyPlayer;
    }

    public void restartTimeLine() {
        endTurnLineTimerTask.myStop();

        drawEndTurnTimeLine();

        restart();
    }

    public void restart() {
        removeComponents();
        iconLayout();
        gameStuffLayout();
        revalidate();
        repaint();
    }

    public void restart(Player myPlayer, Player enemyPlayer) {
        this.myPlayer = myPlayer;
        this.enemyPlayer = enemyPlayer;

        removeComponents();
        iconLayout();
        gameStuffLayout();
        revalidate();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            deleteCurrentMouseWaiting();
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }
}