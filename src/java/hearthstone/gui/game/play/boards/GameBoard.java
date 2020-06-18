package hearthstone.gui.game.play.boards;

import hearthstone.GuiLogicMapper;
import hearthstone.GuiPlayerMapper;
import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.controls.PassiveButton;
import hearthstone.gui.controls.card.CardButton;
import hearthstone.gui.controls.dialogs.PassiveDialog;
import hearthstone.gui.controls.dialogs.SureDialog;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.game.play.controls.*;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.gamestuff.Game;
import hearthstone.models.card.Card;
import hearthstone.models.card.minions.GoldshireFootman;
import hearthstone.models.hero.Hero;
import hearthstone.models.player.Player;
import hearthstone.util.*;

import javax.imageio.ImageIO;
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
    protected final Player myPlayer, enemyPlayer;
    private final SoundPlayer soundPlayer;
    private final Game game;
    private PassiveButton myPassive;
    private MyTimerTask endTurnLineTimerTask;
    private SparkImage sparkImage;
    protected ArrayList<Card> animatedCardsInMyHand, animatedCardsInEnemyHand;
    protected ArrayList<Card> animatedCardsInMyLand, animatedCardsInEnemyLand;

    protected ArrayList<CardImage> drawCards;

    private boolean isLookingFor;
    private Object waitingObject;

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
    private final int myHeroPowerY = SizeConfigs.gameFrameHeight - 190;

    private final int enemyHeroPowerX = midX + 52;
    private final int enemyHeroPowerY = 60;

    private final int myWeaponX = midX - 165;
    private final int myWeaponY = SizeConfigs.gameFrameHeight - 190;

    private final int enemyWeaponX = midX - 165;
    private final int enemyWeaponY = 60;

    private final int heroWidth = SizeConfigs.medHeroWidth;
    private final int heroHeight = SizeConfigs.medHeroHeight;

    private final int enemyHeroX = midX - 60;
    private final int enemyHeroY = 60;

    private final int myHandX = SizeConfigs.gameFrameWidth / 2 - 40;
    private final int myHandY = SizeConfigs.gameFrameHeight - 80;
    private final int myHandDisCard = 220;

    protected final int enemyHandX = SizeConfigs.gameFrameWidth / 2 - 40;
    protected final int enemyHandY = -65;
    protected final int enemyHandDisCard = 220;

    private final int myLandStartY = midY + 10;
    private final int myLandEndY = midY + 180;
    private final int myLandX = midX;
    private final int myLandY = myLandStartY;
    private final int myLandDisCard = 115;

    private final int enemyLandStartY = midY - 120;
    private final int enemyLandEndY = midY;
    private final int enemyLandX = midX;
    private final int enemyLandY = enemyLandStartY;
    private final int enemyLandDisCard = 115;

    private final int myDeckCardsNumberX = midX + 450;
    private final int myDeckCardsNumberY = midY + 105;

    private final int enemyDeckCardsNumberX = midX + 450;
    private final int enemyDeckCardsNumberY = midY - 95;

    private int myPickedCardX = myDeckCardsNumberX;
    private int myPickedCardY = myDeckCardsNumberY;

    protected int enemyPickedCardX = enemyDeckCardsNumberX;
    protected int enemyPickedCardY = enemyDeckCardsNumberY - SizeConfigs.smallCardHeight - 27;

    protected final int extraPassiveX = 60;
    protected final int extraPassiveY = 50;

    protected final int endTurnTimeLineStartX = boardStartX + 10;
    protected final int endTurnTimeLineEndX = endTurnButtonX - 15;
    protected final int endTurnTimeLineY = midY;

    // Finals END

    public GameBoard(Player myPlayer, Player enemyPlayer, Game game) {
        this.myPlayer = myPlayer;
        this.enemyPlayer = enemyPlayer;
        this.game = game;

        animatedCardsInMyHand = new ArrayList<>();
        animatedCardsInEnemyHand = new ArrayList<>();

        animatedCardsInMyLand = new ArrayList<>();
        animatedCardsInEnemyLand = new ArrayList<>();

        drawCards = new ArrayList<>();

        soundPlayer = new SoundPlayer("/sounds/play_background.wav");
        CredentialsFrame.getSoundPlayer().stop();
        soundPlayer.loopPlay();

        configPanel();

        makeIcons();

        showPassiveDialogs();

        game.startGame();

        iconLayout();

        makeGameStuff();

        gameStuffLayout();

        drawEndTurnTimeLine();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/game_board_background.png"));
        } catch (Exception ignore) {
        }
        g.drawImage(image, 0, 0, null);

        drawMyMana(g2, GuiPlayerMapper.getInstance().getMana(myPlayer),
                GuiPlayerMapper.getInstance().getTurnNumber(myPlayer));

        drawEnemyMana(g2, GuiPlayerMapper.getInstance().getMana(enemyPlayer),
                GuiPlayerMapper.getInstance().getTurnNumber(enemyPlayer));

        drawMyDeckNumberOfCards(g2, GuiPlayerMapper.getInstance().getDeck(myPlayer).getCards().size());
        drawEnemyDeckNumberOfCards(g2, GuiPlayerMapper.getInstance().getDeck(enemyPlayer).getCards().size());

        synchronized (drawCards) {
            for (CardImage cardImage : drawCards) {
                g2.drawImage(cardImage.getImage().getScaledInstance(
                        cardImage.getWidth(), cardImage.getHeight(),
                        Image.SCALE_SMOOTH),
                        cardImage.getX(), cardImage.getY(),
                        cardImage.getWidth(), cardImage.getHeight(),
                        null);
            }
        }

        synchronized (sparkImage) {
            g2.drawImage(sparkImage.getImage().getScaledInstance(
                    sparkImage.getWidth(), sparkImage.getHeight(),
                    Image.SCALE_SMOOTH),
                    sparkImage.getX(), sparkImage.getY(),
                    sparkImage.getWidth(), sparkImage.getHeight(),
                    null);
        }
    }

    private void configPanel() {
        setLayout(null);
        setDoubleBuffered(true);
        setVisible(true);
    }

    protected void showPassiveDialogs() {
        PassiveDialog passiveDialog0 = new PassiveDialog(
                GameFrame.getInstance(),
                GameConfigs.initialPassives * SizeConfigs.medCardWidth + extraPassiveX,
                SizeConfigs.medCardHeight + extraPassiveY,
                Rand.getInstance().getRandomArray(
                        GameConfigs.initialPassives,
                        HearthStone.basePassives.size())
        );
        GuiPlayerMapper.getInstance().setPassive(myPlayer, passiveDialog0.getPassive());
        GuiPlayerMapper.getInstance().doPassive(myPlayer);


        PassiveDialog passiveDialog1 = new PassiveDialog(
                GameFrame.getInstance(),
                GameConfigs.initialPassives * SizeConfigs.medCardWidth + extraPassiveX,
                SizeConfigs.medCardHeight + extraPassiveY,
                Rand.getInstance().getRandomArray(
                        GameConfigs.initialPassives,
                        HearthStone.basePassives.size())
        );
        GuiPlayerMapper.getInstance().setPassive(enemyPlayer, passiveDialog1.getPassive());
        GuiPlayerMapper.getInstance().doPassive(enemyPlayer);

    }

    private void drawMyMana(Graphics2D g, int number, int maxNumber) {
        int fontSize = 25;
        maxNumber = Math.min(maxNumber, GameConfigs.maxManaInGame);

        for (int i = 0; i < number; i++) {
            try {
                BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(
                        "/images/mana.png"));
                g.drawImage(image.getScaledInstance(SizeConfigs.manaWidth, SizeConfigs.manaHeight,
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

        g.setFont(GameFrame.getInstance().getCustomFont(0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(0, fontSize));
        g.drawString(text,
                myManaStringX - fontMetrics.stringWidth(text) / 2, myManaStringY);
    }

    private void drawEnemyMana(Graphics2D g, int number, int maxNumber) {
        int fontSize = 25;
        maxNumber = Math.min(maxNumber, GameConfigs.maxManaInGame);

        String text = number + "/" + maxNumber;

        if (maxNumber == 10)
            fontSize = 19;

        g.setFont(GameFrame.getInstance().getCustomFont(0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(0, fontSize));
        g.drawString(text,
                enemyManaStringX - fontMetrics.stringWidth(text) / 2, enemyManaStringY);
    }

    private void drawMyDeckNumberOfCards(Graphics2D g, int number) {
        int fontSize = 50;

        g.setFont(GameFrame.getInstance().getCustomFont(0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(0, fontSize));

        g.drawString(String.valueOf(number),
                myDeckCardsNumberX - fontMetrics.stringWidth(String.valueOf(number)) / 2,
                myDeckCardsNumberY);
    }

    private void drawEnemyDeckNumberOfCards(Graphics2D g, int number) {
        int fontSize = 50;

        g.setFont(GameFrame.getInstance().getCustomFont(0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(0, fontSize));

        g.drawString(String.valueOf(number),
                enemyDeckCardsNumberX - fontMetrics.stringWidth(String.valueOf(number)) / 2,
                enemyDeckCardsNumberY);
    }

    private void drawMyHeroPower() {
        if (GuiPlayerMapper.getInstance().getHeroPower(myPlayer) == null)
            return;
        HeroPowerButton heroPowerButton = new HeroPowerButton(GuiPlayerMapper.getInstance().getHeroPower(myPlayer),
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight, false);
        heroPowerButton.setBounds(myHeroPowerX, myHeroPowerY,
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight);
        add(heroPowerButton);
    }

    private void drawEnemyHeroPower() {
        if (GuiPlayerMapper.getInstance().getHeroPower(enemyPlayer) == null)
            return;
        HeroPowerButton heroPowerButton = new HeroPowerButton(GuiPlayerMapper.getInstance().getHeroPower(enemyPlayer),
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight, true);
        heroPowerButton.setBounds(enemyHeroPowerX, enemyHeroPowerY,
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight);
        add(heroPowerButton);
    }

    private void drawMyWeapon() {
        if (GuiPlayerMapper.getInstance().getWeapon(myPlayer) == null)
            return;
        WeaponButton weaponButton = new WeaponButton(GuiPlayerMapper.getInstance().getWeapon(myPlayer),
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight, false);
        weaponButton.setBounds(myWeaponX, myWeaponY,
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight);
        add(weaponButton);
    }

    private void drawEnemyWeapon() {
        if (GuiPlayerMapper.getInstance().getWeapon(enemyPlayer) == null)
            return;
        WeaponButton weaponButton = new WeaponButton(GuiPlayerMapper.getInstance().getWeapon(enemyPlayer),
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight, true);
        weaponButton.setBounds(enemyWeaponX, enemyWeaponY,
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight);
        add(weaponButton);
    }

    protected void animateCard(int startX, int startY,
                               int destinationX, int destinationY, BoardCardButton cardButton) {
        long period = 75;

        cardButton.setBounds(startX, startY, cardButton.getWidth(), cardButton.getHeight());

        final int[] x = {cardButton.getX()};
        final int[] y = {cardButton.getY()};

        x[0] = startX;
        y[0] = startY;

        MyTimerTask task = new MyTimerTask(period, new MyTask() {
            @Override
            public void startFunction() {
                CardImage cardImage = new CardImage(x[0], y[0], cardButton.getWidth(), cardButton.getHeight(), cardButton);
                synchronized (drawCards) {
                    drawCards.add(cardImage);
                }
            }

            @Override
            public void periodFunction() {
                int plusX = 5;
                int plusY = 5;

                if (Math.abs(destinationX - x[0]) < 5)
                    plusX = 1;
                if (Math.abs(destinationY - y[0]) < 5)
                    plusY = 1;

                x[0] += (destinationX - x[0] != 0 ? plusX * (destinationX - x[0]) / Math.abs(destinationX - x[0]) : 0);
                y[0] += (destinationY - y[0] != 0 ? plusY * (destinationY - y[0]) / Math.abs(destinationY - y[0]) : 0);

                synchronized (drawCards) {
                    try {
                        getCardImageFromDrawCards(cardButton.getCard()).setX(x[0]);
                        getCardImageFromDrawCards(cardButton.getCard()).setY(y[0]);
                    } catch (NullPointerException ignore) {
                    }
                }
                repaint();
                revalidate();
            }

            @Override
            public void warningFunction() {
            }

            @Override
            public void finishedFunction() {
            }

            @Override
            public void closeFunction() {
                cardButton.setBounds(
                        destinationX, destinationY,
                        cardButton.getWidth(), cardButton.getHeight());
                synchronized (drawCards) {
                    removeCardImageFromDrawCards(cardButton.getCard());
                }
                repaint();
                revalidate();
            }

            @Override
            public boolean finishCondition() {
                if ((x[0] == destinationX && y[0] == destinationY) || (cardButton.getX() == destinationX && cardButton.getY() == destinationY))
                    return true;
                return false;
            }
        });
    }

    private CardImage getCardImageFromDrawCards(Card card) {
        for (CardImage cardImage : drawCards) {
            if (cardImage.getCardButton().getCard() == card) {
                return cardImage;
            }
        }
        return null;
    }

    private void removeCardImageFromDrawCards(Card card) {
        drawCards.removeIf(cardImage -> cardImage.getCardButton().getCard() == card);
    }

    private void drawMyCardsOnHand() {
        ArrayList<Card> cards = GuiPlayerMapper.getInstance().getHand(myPlayer);
        if (cards.size() == 0)
            return;
        int dis = myHandDisCard / cards.size();
        int startX = myHandX;
        int startY = myHandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, true, 0);

            makeCardMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth,
                    SizeConfigs.smallCardHeight);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);

            if (!animatedCardsInMyHand.contains(card)) {
                animateCard(myPickedCardX, myPickedCardY,
                        startX + dis * (i - cards.size() / 2), startY,
                        cardButton);
                animatedCardsInMyHand.add(card);
            }
            add(cardButton);
        }
    }

    protected void drawEnemyCardsOnHand() {
        ArrayList<Card> cards = GuiPlayerMapper.getInstance().getHand(enemyPlayer);
        if (cards.size() == 0)
            return;
        int dis = enemyHandDisCard / cards.size();
        int startX = enemyHandX;
        int startY = enemyHandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, 180, true, 1);

            makeCardMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth,
                    SizeConfigs.smallCardHeight);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);

            if (!animatedCardsInEnemyHand.contains(card)) {
                animateCard(enemyPickedCardX, enemyPickedCardY,
                        startX + dis * (i - cards.size() / 2), startY,
                        cardButton);
                animatedCardsInEnemyHand.add(card);
            }
            add(cardButton);
        }
    }

    private void drawMyCardsOnLand() {
        ArrayList<Card> cards = GuiPlayerMapper.getInstance().getLand(myPlayer);
        if (cards.size() == 0)
            return;

        int dis = myLandDisCard;
        int startX = myLandX;
        int startY = myLandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidthOnLand, SizeConfigs.smallCardHeightOnLand, true, 0, true);

            makeCardMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidthOnLand,
                    SizeConfigs.smallCardHeightOnLand);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidthOnLand, SizeConfigs.smallCardHeightOnLand);
            if (!animatedCardsInMyLand.contains(card)) {
                animateCard(myHandX, myHandY,
                        startX + dis * (i - cards.size() / 2)
                                - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                        startY,
                        cardButton);
                animatedCardsInMyLand.add(card);
            }
            add(cardButton);
        }
    }

    private void drawEnemyCardsOnLand() {
        ArrayList<Card> cards = GuiPlayerMapper.getInstance().getLand(enemyPlayer);
        if (cards.size() == 0)
            return;

        int dis = enemyLandDisCard;
        int startX = enemyLandX;
        int startY = enemyLandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidthOnLand, SizeConfigs.smallCardHeightOnLand, true, 1, true);

            makeCardMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidthOnLand,
                    SizeConfigs.smallCardHeightOnLand);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidthOnLand, SizeConfigs.smallCardHeightOnLand);
            if (!animatedCardsInEnemyLand.contains(card)) {
                animateCard(enemyHandX, enemyHandY,
                        startX + dis * (i - cards.size() / 2)
                                - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidthOnLand / 2 : 0),
                        startY,
                        cardButton);
                animatedCardsInEnemyLand.add(card);
            }
            add(cardButton);
        }
    }

    private void drawEndTurnTimeLine() {
        int totalX = endTurnTimeLineEndX - endTurnTimeLineStartX - sparkImage.getWidth();
        long length = 90000;
        long period = length / totalX * 4;
        long warningTime = 9000;

        final int[] xSpark = {endTurnTimeLineStartX};
        final int[] ySpark = {endTurnTimeLineY - SizeConfigs.endTurnFireHeight / 2};

        SoundPlayer countdown = new SoundPlayer("/sounds/countdown.wav");

        endTurnLineTimerTask = new MyTimerTask(period, length, warningTime, new MyTask() {
            @Override
            public void startFunction() {
            }

            @Override
            public void periodFunction() {
                sparkImage.setImagePath("/images/spark_" + (Rand.getInstance().getRandomNumber(10) % 10) + ".png");

                xSpark[0] += 4;
                sparkImage.setX(xSpark[0]);
                sparkImage.setY(ySpark[0]);

                repaint();
                revalidate();
            }

            @Override
            public void warningFunction() {
                countdown.playOnce();
            }

            @Override
            public void finishedFunction() {
                endTurnButton.doClick();
            }

            @Override
            public void closeFunction() {
                countdown.stop();
            }

            @Override
            public boolean finishCondition() {
                return false;
            }
        });
    }

    private void playCard(BoardCardButton button, Card card, BoardCardButton cardButton,
                          int startX, int startY, int width, int height) {
        try {
            if (button.getId() == 0) {
                GuiPlayerMapper.getInstance().playCard(myPlayer, card);
            } else {
                GuiPlayerMapper.getInstance().playCard(enemyPlayer, card);
            }
            cardButton.playSound();
            hearthstone.util.Logger.saveLog("Play card",
                    card.getName() + " played");
            restart();
        } catch (HearthStoneException e) {
            try {
                hearthstone.util.Logger.saveLog("ERROR",
                        e.getClass().getName() + ": " + e.getMessage()
                                + "\nStack Trace: " + e.getStackTrace());
            } catch (Exception f) {
                e.getStackTrace();
            }

            button.setBounds(startX, startY, width, height);
            button.setRotate(button.getInitialRotate());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void makeCardMouseListener(BoardCardButton button, Card card,
                                       BoardCardButton cardButton,
                                       int startX, int startY, int width, int height) {
        CardButton bigCardButton = new CardButton(
                card,
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight,
                -1);

        if (button.getId() == 0) {
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
                if (isInMyLand(startX, startY) || isInEnemyLand(startX, startY)) {
                    if (isLookingFor) {
                        if (((GoldshireFootman) waitingObject).found(card)) {
                            isLookingFor = false;
                            waitingObject = null;
                            updatePlayers();
                        }
                    } else {
                        if (button.getId() == game.getWhoseTurn() && ((GoldshireFootman) card).pressed()) {
                            isLookingFor = true;
                            waitingObject = card;
                        }
                    }
                }

                CardImage cardImage = new CardImage(startX, startY, width, height, button);
                synchronized (drawCards) {
                    drawCards.add(cardImage);
                }
            }

            public void mouseEntered(MouseEvent e) {
                if (cardButton.isShowBig()) {
                    add(bigCardButton);
                    updateUI();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (cardButton.isShowBig()) {
                    remove(bigCardButton);
                    updateUI();
                }
            }

            public void mouseReleased(MouseEvent E) {
                synchronized (drawCards) {
                    removeCardImageFromDrawCards(card);
                }
                repaint();
                revalidate();

                if (!isInMyLand(startX, startY) &&
                        isInMyLand(E.getX() + startX, E.getY() + startY) &&
                        game.getWhoseTurn() == 0) {
                    playCard(button, card, cardButton,
                            startX, startY, width, height);
                } else if (!isInEnemyLand(startX, startY) &&
                        isInEnemyLand(E.getX() + startX, E.getY() + startY) &&
                        game.getWhoseTurn() == 1) {
                    playCard(button, card, cardButton,
                            startX, startY, width, height);
                } else {
                    button.setBounds(startX, startY, width, height);
                }
            }
        });

        button.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isInMyLand(startX, startY) || isInEnemyLand(startX, startY))
                    return;
                if ((game.getWhoseTurn() != button.getId()))
                    return;

                synchronized (drawCards) {
                    int newX = e.getX() + startX;
                    int newY = e.getY() + startY;
                    // card.setBounds(newX, newY)
                    getCardImageFromDrawCards(card).setX(newX);
                    getCardImageFromDrawCards(card).setY(newY);
                }
                repaint();
                revalidate();
            }
        });
    }

    private void makeHeroMouseListener(BoardHeroButton button, Hero hero) {
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(isLookingFor){
                    if(((GoldshireFootman) waitingObject).found(hero)){
                        isLookingFor = false;
                        waitingObject = null;
                    }
                }
            }
        });
    }


    private void makeIcons() {
        backButton = new ImageButton("icons/back.png",
                "icons/back_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png",
                "icons/minimize_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png",
                "icons/close_active.png",
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
                CredentialsFrame.getSoundPlayer().loopPlay();
                closeBoard();
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
            }
        });
    }

    private void makeGameStuff() {
        endTurnButton = new ImageButton("End Turn", "end_turn.png",
                "end_turn_active.png", 15, 1,
                SizeConfigs.endTurnButtonWidth, SizeConfigs.endTurnButtonHeight);
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SoundPlayer soundPlayer = new SoundPlayer("/sounds/ding.wav");
                soundPlayer.playOnce();

                GuiLogicMapper.getInstance().endTurn(game);
                endTurnLineTimerTask.myStop();
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

        myHero = new BoardHeroButton(myPlayer.getHero(), heroWidth, heroHeight, 0); // player hero
        makeHeroMouseListener(myHero, myHero.getHero());

        enemyHero = new BoardHeroButton(enemyPlayer.getHero(), heroWidth, heroHeight, 1); // enemy hero
        makeHeroMouseListener(enemyHero, enemyHero.getHero());

        myPassive = new PassiveButton(GuiPlayerMapper.getInstance().getPassive(myPlayer),
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight);
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

    private void gameStuffLayout() {
        drawMyCardsOnHand();
        drawEnemyCardsOnHand();

        drawMyCardsOnLand();
        drawEnemyCardsOnLand();

        drawMyHeroPower();
        drawEnemyHeroPower();

        drawMyWeapon();
        drawEnemyWeapon();

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

    private void closeBoard() {
        soundPlayer.stop();
        endTurnLineTimerTask.myStop();
    }

    private void removeComponents() {
        for (Component component : this.getComponents()) {
            if (component instanceof ImagePanel &&
                    ((ImagePanel) component).getImagePath().contains("spark"))
                continue;
            if (component instanceof ImagePanel && ((ImagePanel) component).getImagePath().contains("rope.png"))
                continue;
            this.remove(component);
        }
    }

    private void updatePlayers() {
        GuiPlayerMapper.getInstance().updatePlayer(myPlayer);
        GuiPlayerMapper.getInstance().updatePlayer(enemyPlayer);
        restart();
    }

    private void restart() {
        removeComponents();
        iconLayout();
        gameStuffLayout();
        revalidate();
        repaint();
    }
}