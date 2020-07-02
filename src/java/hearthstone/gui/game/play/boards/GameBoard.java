package hearthstone.gui.game.play.boards;

import hearthstone.DataTransform;
import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.data.DataBase;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.controls.PassiveButton;
import hearthstone.gui.controls.card.CardButton;
import hearthstone.gui.controls.dialogs.*;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.game.play.Animation;
import hearthstone.gui.game.play.controls.*;
import hearthstone.gui.interfaces.HaveCard;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerBehaviour;
import hearthstone.logic.models.card.minion.MinionBehaviour;
import hearthstone.logic.models.card.reward.RewardCard;
import hearthstone.logic.models.card.weapon.WeaponBehaviour;
import hearthstone.util.*;
import hearthstone.util.getresource.ImageResource;
import hearthstone.util.timer.MyBigTask;
import hearthstone.util.timer.MyDelayTask;
import hearthstone.util.timer.MyDelayTimerTask;
import hearthstone.util.timer.MyTimerTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameBoard extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton;
    private ImageButton endTurnButton;
    private BoardHeroButton myHero, enemyHero;
    private final SoundPlayer soundPlayer;

    protected final int myPlayerId, enemyPlayerId;

    private PassiveButton myPassive;

    private MyTimerTask endTurnLineTimerTask;
    private SparkImage sparkImage;
    private ImagePanel ropeImage;

    protected ArrayList<Card> animatedCardsInHand;
    protected ArrayList<Card> animatedCardsInLand;

    protected ArrayList<Card> animationsCard;
    protected ArrayList<Animation> animations;
    private final Object animationLock = new Object();

    private boolean isLookingFor;
    private Object waitingObject;

    private static BufferedImage backgroundImage;
    private static BufferedImage manaImage;

    private MessageDialog messageDialog;

    // Finals START
    private final int boardStartX = SizeConfigs.gameFrameWidth / 2 - 360;
    private final int boardEndX = SizeConfigs.gameFrameWidth / 2 + 360;

    private final int midX = SizeConfigs.gameFrameWidth / 2;
    private final int midY = SizeConfigs.gameFrameHeight / 2 - 23;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;

    private final int endTurnButtonX = 882;
    private final int endTurnButtonY = 300;

    private final int manaX = 770;
    private final int manaY = 638;
    private final int manaDis = 0;
    private final int myManaStringX = 742;
    private final int myManaStringY = 658;

    private final int enemyManaStringX = 713;
    private final int enemyManaStringY = 53;

    private final int myHeroX = midX - 60;
    private final int myHeroY = SizeConfigs.gameFrameHeight - 236;

    private final int myHeroPowerX = midX + 52;
    private final int myHeroPowerY = SizeConfigs.gameFrameHeight - 220;

    private final int enemyHeroPowerX = midX + 52;
    private final int enemyHeroPowerY = 90;

    private final int myWeaponX = midX - 165;
    private final int myWeaponY = SizeConfigs.gameFrameHeight - 220;

    private final int enemyWeaponX = midX - 165;
    private final int enemyWeaponY = 90;

    private final int heroWidth = SizeConfigs.medHeroWidth;
    private final int heroHeight = SizeConfigs.medHeroHeight;

    private final int enemyHeroX = midX - 60;
    private final int enemyHeroY = 60;

    private final int myHandX = SizeConfigs.gameFrameWidth / 2 - 40;
    private final int myHandY = SizeConfigs.gameFrameHeight - 80;
    protected final int handDisCard = 220;

    protected final int enemyHandX = SizeConfigs.gameFrameWidth / 2 - 40;
    protected final int enemyHandY = -65;

    private final int myLandStartY = midY + 10;
    private final int myLandEndY = midY + 180;
    private final int myLandX = midX;
    private final int myLandY = myLandStartY;
    private final int landDisCard = 115;

    private final int enemyLandStartY = midY - 120;
    private final int enemyLandEndY = midY;
    private final int enemyLandX = midX;
    private final int enemyLandY = enemyLandStartY;

    private final int myDeckCardsNumberX = midX + 450;
    private final int myDeckCardsNumberY = midY + 105;

    private final int enemyDeckCardsNumberX = midX + 450;
    private final int enemyDeckCardsNumberY = midY - 95;

    protected int myPickedCardX = myDeckCardsNumberX;
    protected int myPickedCardY = myDeckCardsNumberY;

    protected int enemyPickedCardX = enemyDeckCardsNumberX;
    protected int enemyPickedCardY = enemyDeckCardsNumberY - SizeConfigs.smallCardHeight - 27;

    protected final int endTurnTimeLineStartX = boardStartX + 10;
    protected final int endTurnTimeLineEndX = endTurnButtonX - 15;
    protected final int endTurnTimeLineY = midY;

    protected final int myErrorX = 615;
    protected final int myErrorY = 530;

    protected final int enemyErrorX = 200;
    protected final int enemyErrorY = 70;

    private final int myRewardX = 0;
    private final int myRewardY = 315;

    private final int enemySpellDestinationX = 180;
    private final int enemySpellDestinationY = 0;

    private final int mySpellDestinationX = 180;
    private final int mySpellDestinationY = 450;

    private final int enemyRewardX = 0;
    private final int enemyRewardY = 130;

    // Finals END

    public GameBoard(int myPlayerId, int enemyPlayerId) {
        this.myPlayerId = myPlayerId;
        this.enemyPlayerId = enemyPlayerId;

        animatedCardsInHand = new ArrayList<>();
        animatedCardsInLand = new ArrayList<>();

        animationsCard = new ArrayList<>();
        animations = new ArrayList<>();

        soundPlayer = new SoundPlayer("/sounds/play_background.wav");
        CredentialsFrame.getSoundPlayer().stop();
        soundPlayer.loopPlay();

        configPanel();

        makeIcons();

        iconLayout();

        makeGameStuff();

        gameStuffLayoutBeforeStartGame();

        MyDelayTimerTask initialGameAsking = new MyDelayTimerTask(100, new MyDelayTask() {
            @Override
            public void delayAction() {
                showPassiveDialogs();

                showCardDialog();

                gameStuffLayout();

                drawEndTurnTimeLine();

                Mapper.getInstance().startGame();
            }
        });
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

        drawMyMana(g2, DataTransform.getInstance().getMana(myPlayerId),
                DataTransform.getInstance().getTurnNumber(myPlayerId));

        drawEnemyMana(g2, DataTransform.getInstance().getMana(enemyPlayerId),
                DataTransform.getInstance().getTurnNumber(enemyPlayerId));

        drawDeckNumberOfCards(g2, myDeckCardsNumberX, myDeckCardsNumberY,
                DataTransform.getInstance().getDeck(myPlayerId).getCards().size());
        drawDeckNumberOfCards(g2, enemyDeckCardsNumberX, enemyDeckCardsNumberY,
                DataTransform.getInstance().getDeck(enemyPlayerId).getCards().size());

        g2.drawImage(sparkImage.getImage().getScaledInstance(
                sparkImage.getWidth(), sparkImage.getHeight(),
                Image.SCALE_SMOOTH),
                sparkImage.getX(), sparkImage.getY(),
                sparkImage.getWidth(), sparkImage.getHeight(),
                null);
    }

    private void configPanel() {
        setSize(new Dimension(SizeConfigs.gameFrameWidth, SizeConfigs.gameFrameHeight));
        setLayout(null);
        setDoubleBuffered(true);
        setVisible(true);
    }

    protected void showPassiveDialogs() {
        PassiveDialog passiveDialog0 = new PassiveDialog(
                GameFrame.getInstance(),
                Rand.getInstance().getRandomArray(
                        DataTransform.getInstance().getNumberOfPassive(),
                        HearthStone.basePassives.size())
        );
        Mapper.getInstance().setPassive(myPlayerId, passiveDialog0.getPassive());
        Mapper.getInstance().doPassive(myPlayerId);


        PassiveDialog passiveDialog1 = new PassiveDialog(
                GameFrame.getInstance(),
                Rand.getInstance().getRandomArray(
                        DataTransform.getInstance().getNumberOfPassive(),
                        HearthStone.basePassives.size())
        );
        Mapper.getInstance().setPassive(enemyPlayerId, passiveDialog1.getPassive());
        Mapper.getInstance().doPassive(enemyPlayerId);
    }

    protected void showCardDialog() {
        CardDialog cardDialog0 = new CardDialog(
                GameFrame.getInstance(),
                DataTransform.getInstance().getTopCards(0, GameConfigs.initialDiscardCards));

        Mapper.getInstance().removeInitialCards(0, cardDialog0.getCards(), GameConfigs.initialDiscardCards);

        CardDialog cardDialog1 = new CardDialog(
                GameFrame.getInstance(),
                DataTransform.getInstance().getTopCards(1, GameConfigs.initialDiscardCards));

        Mapper.getInstance().removeInitialCards(1, cardDialog1.getCards(), GameConfigs.initialDiscardCards);
    }

    // DRAW MANA
    private void drawMyMana(Graphics2D g, int number, int maxNumber) {
        int fontSize = 25;
        maxNumber = Math.min(maxNumber, DataTransform.getInstance().getMaxManaInGame());

        for (int i = 0; i < number; i++) {
            try {
                if (manaImage == null)
                    manaImage = ImageResource.getInstance().getImage(
                            "/images/mana.png");
                g.drawImage(manaImage.getScaledInstance(SizeConfigs.manaWidth, SizeConfigs.manaHeight,
                        Image.SCALE_SMOOTH),
                        manaX + (SizeConfigs.manaWidth + manaDis) * i, manaY,
                        SizeConfigs.manaWidth, SizeConfigs.manaHeight, null);
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

    private void drawEnemyMana(Graphics2D g, int number, int maxNumber) {
        int fontSize = 25;
        maxNumber = Math.min(maxNumber, DataTransform.getInstance().getMaxManaInGame());

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
    private void drawDeckNumberOfCards(Graphics2D g, int X, int Y, int number) {
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
    private void drawReward(int playerId, int X, int Y) {
        RewardCard rewardCard = DataTransform.getInstance().getReward(playerId);
        if (rewardCard == null)
            return;

        BoardRewardButton rewardButton = new BoardRewardButton(
                rewardCard,
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight);

        rewardButton.setBounds(X, Y,
                SizeConfigs.medCardWidth, SizeConfigs.medCardHeight);

        add(rewardButton);
    }
    // DRAW REWARD

    // DRAW HEROPOWER
    private void drawHeroPower(int playerId, int X, int Y) {
        if (DataTransform.getInstance().getHeroPower(playerId) == null)
            return;
        HeroPowerButton heroPowerButton = new HeroPowerButton(DataTransform.getInstance().getHeroPower(playerId),
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight, true, playerId);

        makeHeroPowerMouseListener(heroPowerButton);

        heroPowerButton.setBounds(X, Y,
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight);

        add(heroPowerButton);
    }
    // DRAW HEROPOWER

    // DRAW WEAPON
    private void drawWeapon(int playerId, int X, int Y) {
        if (DataTransform.getInstance().getWeapon(playerId) == null)
            return;

        WeaponButton weaponButton = new WeaponButton(DataTransform.getInstance().getWeapon(playerId),
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight, true, playerId);
        weaponButton.setBounds(X, Y,
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight);

        makeWeaponMouseListener(weaponButton);

        weaponButton.setBounds(X, Y,
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight);

        add(weaponButton);
    }
    // DRAW WEAPON

    // DRAW CARDS
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
                        SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, true, playerId);
            } else {
                cardButton = new BoardCardButton(card,
                        SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, 180, true, playerId);
            }

            makeCardOnHandMouseListener(cardButton,
                    startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth,
                    SizeConfigs.smallCardHeight);

            synchronized (animationLock) {
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

    private void drawCardsOnLand(int playerId, int landX, int landY) {
        ArrayList<Card> cards = DataTransform.getInstance().getLand(playerId);
        if (cards.size() == 0)
            return;

        int dis = landDisCard;
        int startX = landX;
        int startY = landY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton;

            if (myPlayerId == playerId) {
                cardButton = new BoardCardButton(card,
                        SizeConfigs.smallCardWidthOnLand, SizeConfigs.smallCardHeightOnLand, true, 0, true);
            } else {
                cardButton = new BoardCardButton(card,
                        SizeConfigs.smallCardWidthOnLand, SizeConfigs.smallCardHeightOnLand, true, 1, true);
            }

            makeCardOnLandMouseListener(cardButton,
                    startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                    startY);

            synchronized (animationLock) {
                if (animationsCard.contains(card)) {
                    int ind = animationsCard.indexOf(card);
                    Animation animation = animations.get(ind);

                    remove(animation.getComponent());
                    add(cardButton);

                    animation.setDestinationX(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0));
                    animation.setDestinationY(startY);
                    animation.setComponent(cardButton);

                    animatedCardsInLand.add(card);
                    continue;
                }
            }

            cardButton.setBounds(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidthOnLand, SizeConfigs.smallCardHeightOnLand);

            if (!animatedCardsInLand.contains(card)) {

                animatedCardsInLand.add(card);

                Animation destination = new Animation(startX + dis * (i - cards.size() / 2)
                        - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                        startY, cardButton);

                if (playerId == myPlayerId)
                    animateCard(myHandX, myHandY, SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, destination);
                else
                    animateCard(enemyHandX, enemyHandY, SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, destination);
            }
            add(cardButton);
        }
    }
    // DRAW CARDS

    // END TURN LINE
    private void drawEndTurnTimeLine() {
        int totalX = endTurnTimeLineEndX - endTurnTimeLineStartX - sparkImage.getWidth();
        long length = 90000;
        long period = length / totalX * 2;
        long warningTime = 9000;

        final int[] xSpark = {endTurnTimeLineStartX};
        final int[] ySpark = {endTurnTimeLineY - SizeConfigs.endTurnFireHeight / 2};

        final int sparkWidth = sparkImage.getWidth();
        final int sparkHeight = sparkImage.getHeight();

        SoundPlayer warningPlayer = new SoundPlayer("/sounds/countdown.wav");

        endTurnLineTimerTask = new MyTimerTask(period, length, warningTime, new MyBigTask() {
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
                        SizeConfigs.endTurnRopeHeight);

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
            }

            @Override
            public boolean finishCondition() {
                return false;
            }
        });
    }
    // END TURN LINE

    // ANIMATIONS
    protected void animateCard(int startX, int startY, int width, int height,
                               Animation animation) {
        long period = 25;

        final int[] x = {startX};
        final int[] y = {startY};

        MyTimerTask task = new MyTimerTask(period, new MyBigTask() {
            private void addComponentIfNot(Component component) {
                for(Component component1: GameBoard.this.getComponents()){
                    if(component1 == component)
                        return;
                }
                GameBoard.this.add(component);
            }

            @Override
            public void startFunction() {
                synchronized (animationLock) {
                    animationsCard.add(((HaveCard) animation.getComponent()).getCard());
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
                MyDelayTimerTask removeDelay =  new MyDelayTimerTask(animation.getRemoveDelayAfterArrived(), new MyDelayTask() {
                    @Override
                    public void delayAction() {
                        synchronized (animationLock) {
                            int ind = animationsCard.indexOf(((HaveCard) animation.getComponent()).getCard());
                            animationsCard.remove(ind);
                            animations.remove(ind);

                            if (animationsCard.size() == 0) {
                                Mapper.getInstance().updateBoard();
                            }
                        }
                    }
                });
            }

            @Override
            public boolean finishCondition() {
                return x[0] == animation.getDestinationX() && y[0] == animation.getDestinationY();
            }
        });
    }

    protected void animateCardWithResize(Card card, int startX, int startY,
                                         int startWidth, int startHeight,
                                         Animation animation) {
        long period = 20;

        final double[] x = {animation.getComponent().getX()};
        final double[] y = {animation.getComponent().getY()};

        final double[] width = {animation.getComponent().getWidth()};
        final double[] height = {animation.getComponent().getHeight()};

        x[0] = startX;
        y[0] = startY;

        width[0] = startWidth;
        height[0] = startHeight;

        MyTimerTask task = new MyTimerTask(period, new MyBigTask() {
            @Override
            public void startFunction() {
                synchronized (animationLock) {
                    animationsCard.add(((CardButton) animation.getComponent()).getCard());
                    animations.add(animation);

                    animation.getComponent().setBounds(startX, startY, startWidth, startHeight);
                    add(animation.getComponent());
                }
            }

            @Override
            public void periodFunction() {
                double jumpLoc = 1;
                double plusX = jumpLoc;
                double plusY = jumpLoc;

                double jumpSize = 0.5;
                double plusWidth = jumpSize;
                double plusHeight = jumpSize;

                if (Math.abs(animation.getDestinationX() - x[0]) < jumpLoc)
                    plusX = 1;
                if (Math.abs(animation.getDestinationY() - y[0]) < jumpLoc)
                    plusY = 1;

                if (Math.abs(animation.getDestinationWidth() - width[0]) < jumpSize)
                    plusWidth = 0.5;
                if (Math.abs(animation.getDestinationHeight() - height[0]) < jumpSize)
                    plusHeight = 0.5;

                x[0] += (animation.getDestinationX() - x[0] != 0 ? plusX * (animation.getDestinationX() - x[0]) / Math.abs(animation.getDestinationX() - x[0]) : 0);
                y[0] += (animation.getDestinationY() - y[0] != 0 ? plusY * (animation.getDestinationY() - y[0]) / Math.abs(animation.getDestinationY() - y[0]) : 0);


                width[0] += (animation.getDestinationWidth() - width[0] != 0 ? plusWidth * (animation.getDestinationWidth() - width[0]) / Math.abs(animation.getDestinationWidth() - width[0]) : 0);
                height[0] += (animation.getDestinationHeight() - height[0] != 0 ? plusHeight * (animation.getDestinationHeight() - height[0]) / Math.abs(animation.getDestinationHeight() - height[0]) : 0);

                GameBoard.this.remove(animation.getComponent());
                animation.setComponent(new CardButton(card, false, (int) width[0], (int) height[0], -1));
                animation.getComponent().setBounds((int) x[0], (int) y[0], (int) width[0], (int) height[0]);
                GameBoard.this.add(animation.getComponent());
            }

            @Override
            public void warningFunction() {
            }

            @Override
            public void finishedFunction() {
            }

            @Override
            public void closeFunction() {
                MyDelayTimerTask delayTimerTask = new MyDelayTimerTask(3000, new MyDelayTask() {
                    @Override
                    public void delayAction() {

                        synchronized (animationLock) {
                            int ind = animationsCard.indexOf(((CardButton) animation.getComponent()).getCard());
                            animationsCard.remove(ind);
                            animations.remove(ind);

                            if (animationsCard.size() == 0) {
                                Mapper.getInstance().updateBoard();
                            }
                        }
                    }
                });
            }

            @Override
            public boolean finishCondition() {
                return (int) x[0] == animation.getDestinationX() &&
                        (int) y[0] == animation.getDestinationY() &&
                        (int) width[0] == animation.getDestinationWidth() &&
                        (int) height[0] == animation.getDestinationHeight();
            }
        });
    }
    // ANIMATIONS

    // MOUSE LISTENERS
    protected void makeCardOnHandMouseListener(BoardCardButton button,
                                               int startX, int startY, int width, int height) {
        CardButton bigCardButton = new CardButton(
                button.getCard(),
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight,
                -1);

        if (button.getPlayerId() == 0) {
            bigCardButton.setBounds(
                    SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                    SizeConfigs.gameFrameHeight - SizeConfigs.medCardHeight,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
        } else {
            bigCardButton.setBounds(
                    SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                    0,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button.isShowBig()) {
                    add(bigCardButton);
                    if (button.getPlayerId() == 0)
                        button.setBounds(button.getX(), button.getY() - 47, button.getWidth(), button.getHeight());
                    else
                        button.setBounds(button.getX(), button.getY() + 47, button.getWidth(), button.getHeight());
                    updateUI();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (button.isShowBig()) {
                    remove(bigCardButton);
                    if (button.getPlayerId() == 0 && button.getY() != startY)
                        button.setBounds(button.getX(), button.getY() + 47, button.getWidth(), button.getHeight());
                    else if (button.getPlayerId() == 1 && button.getY() != startY)
                        button.setBounds(button.getX(), button.getY() - 47, button.getWidth(), button.getHeight());
                    updateUI();
                }
            }

            public void mouseReleased(MouseEvent E) {
                if (!isInMyLand(startX, startY) &&
                        isInMyLand(E.getX() + button.getX(), E.getY() + button.getY()) &&
                        DataTransform.getInstance().getWhoseTurn() == 0) {
                    playCard(button, button.getCard(),
                            startX, startY, width, height);
                } else if (!isInEnemyLand(startX, startY) &&
                        isInEnemyLand(E.getX() + button.getX(), E.getY() + button.getY()) &&
                        DataTransform.getInstance().getWhoseTurn() == 1) {
                    playCard(button, button.getCard(),
                            startX, startY, width, height);
                } else {
                    button.setBounds(startX, startY, width, height);
                }
            }
        });

        button.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if ((DataTransform.getInstance().getWhoseTurn() != button.getPlayerId()))
                    return;

                int newX = e.getX() + button.getX();
                int newY = e.getY() + button.getY();
                button.setBounds(newX - width / 2, newY - height / 2, width, height);
            }
        });
    }

    private void makeCardOnLandMouseListener(BoardCardButton button,
                                             int startX, int startY) {
        CardButton bigCardButton = new CardButton(
                button.getCard(),
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight,
                -1);

        if (button.getPlayerId() == 0) {
            bigCardButton.setBounds(
                    SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                    SizeConfigs.gameFrameHeight - SizeConfigs.medCardHeight,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
        } else {
            bigCardButton.setBounds(
                    SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                    0,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    try {
                        Mapper.getInstance().foundObjectForObject(waitingObject, button.getCard());
                        deleteCurrentMouseWaiting();
                        Mapper.getInstance().updateBoard();
                    } catch (HearthStoneException hse) {
                        showError(hse.getMessage());
                    }
                } else {
                    if (button.getPlayerId() == DataTransform.getInstance().getWhoseTurn()
                            && ((MinionBehaviour) button.getCard()).canAttack()) {
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

    private void makeWeaponMouseListener(WeaponButton button) {
        CardButton bigCardButton = new CardButton(
                button.getCard(),
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight,
                -1);

        if (button.getPlayerId() == 0) {
            bigCardButton.setBounds(
                    SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                    SizeConfigs.gameFrameHeight - SizeConfigs.medCardHeight,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
        } else {
            bigCardButton.setBounds(
                    SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                    0,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    try {
                        Mapper.getInstance().foundObjectForObject(waitingObject, button.getCard());
                        deleteCurrentMouseWaiting();
                        Mapper.getInstance().updateBoard();
                    } catch (HearthStoneException hse) {
                        showError(hse.getMessage());
                    }
                } else {
                    if (button.getPlayerId() == DataTransform.getInstance().getWhoseTurn()
                            && ((WeaponBehaviour) button.getCard()).pressed()) {
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

    private void makeHeroPowerMouseListener(HeroPowerButton button) {
        CardButton bigCardButton = new CardButton(
                button.getCard(),
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight,
                -1);

        if (button.getPlayerId() == 0) {
            bigCardButton.setBounds(
                    SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                    SizeConfigs.gameFrameHeight - SizeConfigs.medCardHeight,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
        } else {
            bigCardButton.setBounds(
                    SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                    0,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    try {
                        Mapper.getInstance().foundObjectForObject(waitingObject, button.getCard());
                        deleteCurrentMouseWaiting();
                        Mapper.getInstance().updateBoard();
                    } catch (HearthStoneException hse) {
                        showError(hse.getMessage());
                    }
                } else {
                    if (button.getPlayerId() == DataTransform.getInstance().getWhoseTurn()
                            && ((HeroPowerBehaviour) button.getCard()).canAttack()) {
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

    private void makeHeroMouseListener(BoardHeroButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isLookingFor) {
                    try {
                        Mapper.getInstance().foundObjectForObject(waitingObject, button.getHero());
                        deleteCurrentMouseWaiting();
                    } catch (HearthStoneException hse) {
                        showError(hse.getMessage());
                    }
                }
            }
        });
    }
    // MOUSE LISTENERS

    private void playCard(BoardCardButton button, Card card,
                          int startX, int startY, int width, int height) {
        try {
            if (button.getPlayerId() == 0) {
                Mapper.getInstance().playCard(myPlayerId, card);
            } else {
                Mapper.getInstance().playCard(enemyPlayerId, card);
            }

            if (card.getCardType() == CardType.SPELL || card.getCardType() == CardType.WEAPONCARD || card.getCardType() == CardType.HEROPOWER)
                removeCardAnimation(card);

            button.playSound();

            hearthstone.util.Logger.saveLog("Play card",
                    card.getName() + " played");
        } catch (HearthStoneException e) {
            try {
                hearthstone.util.Logger.saveLog("ERROR",
                        e.getClass().getName() + ": " + e.getMessage()
                                + "\nStack Trace: " + e.getStackTrace());
            } catch (Exception f) {
                e.getStackTrace();
            }

            button.setBounds(startX, startY, width, height);
            showError(e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void animateSpell(int playerId, Card card) {
        for (Component component : this.getComponents()) {
            if (component instanceof HaveCard && ((HaveCard) component).getCard() == card) {
                this.remove(component);
            }
        }

        if (playerId == myPlayerId) {
            animateCard(myHandX, myHandY, SizeConfigs.medCardWidth, SizeConfigs.medCardHeight,
                    new Animation(mySpellDestinationX, mySpellDestinationY,
                            3000, new CardButton(card, SizeConfigs.medCardWidth, SizeConfigs.medCardHeight, -1)));
        } else {
            animateCard(enemyHandX, enemyHandY, SizeConfigs.medCardWidth, SizeConfigs.medCardHeight,
                    new Animation(enemySpellDestinationX, enemySpellDestinationY,
                            3000, new CardButton(card, SizeConfigs.medCardWidth, SizeConfigs.medCardHeight, -1)));
        }
    }

    private void removeCardAnimation(Card card) {
        synchronized (animationLock) {
            if (animationsCard.contains(card)) {
                int ind = animationsCard.indexOf(card);
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

    private void makeIcons() {
        backButton = new ImageButton("icons/back.png",
                "icons/back_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png",
                "icons/minimize_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png",
                "icons/close_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        backButton.addActionListener(actionEvent -> {
            try {
                hearthstone.util.Logger.saveLog("Click_button",
                        "back_button");
            } catch (Exception e) {
                e.getStackTrace();
            }

            SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                    "Are you sure you want to exit game?! (you will lose current game)",
                    SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);
            boolean sure = sureDialog.getValue();
            if (sure) {
                try {
                    DataBase.save();
                    hearthstone.util.Logger.saveLog("Exit",
                            "Exited from game board");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                beforeCloseBoard();
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
            }
        });
    }

    private void makeGameStuff() {
        endTurnButton = new ImageButton("End Turn", "end_turn.png",
                "end_turn_hovered.png", 15, 1,
                SizeConfigs.endTurnButtonWidth, SizeConfigs.endTurnButtonHeight);
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SoundPlayer soundPlayer = new SoundPlayer("/sounds/ding.wav");
                soundPlayer.playOnce();

                Mapper.getInstance().endTurn();
                endTurnLineTimerTask.myStop();

                deleteCurrentMouseWaiting();

                drawEndTurnTimeLine();
                restart();
            }
        });

        sparkImage = new SparkImage(
                endTurnTimeLineStartX,
                endTurnTimeLineY - SizeConfigs.endTurnFireHeight / 2,
                SizeConfigs.endTurnFireWidth,
                SizeConfigs.endTurnFireHeight,
                "/images/spark_0.png");

        ropeImage = new ImagePanel("rope.png", SizeConfigs.endTurnRopeWidth + 7,
                SizeConfigs.endTurnRopeHeight, sparkImage.getX() + sparkImage.getWidth(),
                sparkImage.getY() + sparkImage.getHeight() / 2, true);
        add(ropeImage);

        myHero = new BoardHeroButton(DataTransform.getInstance().getHero(myPlayerId),
                heroWidth, heroHeight, 0);
        makeHeroMouseListener(myHero);

        enemyHero = new BoardHeroButton(DataTransform.getInstance().getHero(enemyPlayerId), heroWidth, heroHeight, 1); // enemy hero
        makeHeroMouseListener(enemyHero);

        myPassive = new PassiveButton(DataTransform.getInstance().getPassive(myPlayerId),
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight);

        messageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, SizeConfigs.inGameErrorWidth, SizeConfigs.inGameErrorHeight);
        add(messageDialog);
    }

    private void iconLayout() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(backButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(closeButton);
    }

    private void gameStuffLayoutBeforeStartGame() {
        endTurnButton.setBounds(endTurnButtonX, endTurnButtonY,
                SizeConfigs.endTurnButtonWidth, SizeConfigs.endTurnButtonHeight);
        add(endTurnButton);

        myHero.setBounds(myHeroX, myHeroY,
                heroWidth, heroHeight);
        add(myHero);

        enemyHero.setBounds(enemyHeroX, enemyHeroY,
                heroWidth, heroHeight);
        add(enemyHero);
    }

    private void gameStuffLayout() {
        drawCardsOnHand(0, myHandX, myHandY);
        drawCardsOnHand(1, enemyHandX, enemyHandY);

        drawCardsOnLand(0, myLandX, myLandY);
        drawCardsOnLand(1, enemyLandX, enemyLandY);

        drawHeroPower(0, myHeroPowerX, myHeroPowerY);
        drawHeroPower(1, enemyHeroPowerX, enemyHeroPowerY);

        drawWeapon(0, myWeaponX, myWeaponY);
        drawWeapon(1, enemyWeaponX, enemyWeaponY);

        drawReward(0, myRewardX, myRewardY);
        drawReward(1, enemyRewardX, enemyRewardY);

        endTurnButton.setBounds(endTurnButtonX, endTurnButtonY,
                SizeConfigs.endTurnButtonWidth, SizeConfigs.endTurnButtonHeight);
        add(endTurnButton);

        myHero.setBounds(myHeroX, myHeroY,
                heroWidth, heroHeight);
        add(myHero);

        enemyHero.setBounds(enemyHeroX, enemyHeroY,
                heroWidth, heroHeight);
        add(enemyHero);

        /*myPassive.setBounds(SizeConfigs.gameFrameWidth - SizeConfigs.medCardWidth,
                0,
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight);
        add(myPassive);*/
    }

    private boolean isInMyLand(int x, int y) {
        return x >= boardStartX && x <= boardEndX && y >= myLandStartY && y <= myLandEndY;
    }

    private boolean isInEnemyLand(int x, int y) {
        return x >= boardStartX && x <= boardEndX && y >= enemyLandStartY && y <= enemyLandEndY;
    }

    private synchronized void showError(String text) {
        messageDialog.setText(text);
        this.remove(messageDialog);

        if (DataTransform.getInstance().getWhoseTurn() == 0) {
            messageDialog.setImagePath("/images/my_think_dialog.png");
            messageDialog.setBounds(myErrorX, myErrorY,
                    SizeConfigs.inGameErrorWidth, SizeConfigs.inGameErrorHeight);
        } else {
            messageDialog.setImagePath("/images/enemy_think_dialog.png");
            messageDialog.setBounds(enemyErrorX, enemyErrorY,
                    SizeConfigs.inGameErrorWidth, SizeConfigs.inGameErrorHeight);
        }

        this.add(messageDialog);
        messageDialog.setVisibility(true);
    }

    public void gameEnded() {
        if (DataTransform.getInstance().isLost(0)) {
            try {
                DataBase.save();

                hearthstone.util.Logger.saveLog("Game ended", DataTransform.getInstance().getPlayerName(1) +
                        " won " +
                        DataTransform.getInstance().getPlayerName(0));

                Mapper.getInstance().lost(0);
                Mapper.getInstance().won(1);

                ErrorDialog errorDialog = new ErrorDialog(GameFrame.getInstance(), "Ooops! You lost the game :(",
                        SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                DataBase.save();
                hearthstone.util.Logger.saveLog("Game ended", DataTransform.getInstance().getPlayerName(0) +
                        " won " +
                        DataTransform.getInstance().getPlayerName(1));

                Mapper.getInstance().lost(1);
                Mapper.getInstance().won(0);

                ErrorDialog errorDialog = new ErrorDialog(GameFrame.getInstance(),
                        "Congratulation! You Won the game :)", new Color(38, 104, 23),
                        SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        beforeCloseBoard();
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
    }

    private void beforeCloseBoard() {
        endTurnLineTimerTask.myStop();
        CredentialsFrame.getSoundPlayer().loopPlay();
        soundPlayer.stop();
        deleteCurrentMouseWaiting();
    }

    private void removeComponents() {
        for (Component component : this.getComponents()) {
            if (component instanceof ImagePanel &&
                    ((ImagePanel) component).getImagePath().contains("spark"))
                continue;
            if (component instanceof ImagePanel && ((ImagePanel) component).isRTL())
                continue;

            if (component instanceof HaveCard) {
                HaveCard cardButton = (HaveCard) component;
                synchronized (animationLock) {
                    if (animationsCard.contains(cardButton.getCard())) {
                        continue;
                    }
                }
            }

            if (component instanceof MessageDialog)
                continue;
            this.remove(component);
        }
    }

    public void restart() {
        removeComponents();
        iconLayout();
        gameStuffLayout();
        revalidate();
        repaint();
    }
}