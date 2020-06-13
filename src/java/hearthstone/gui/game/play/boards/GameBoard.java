package hearthstone.gui.game.play.boards;

import hearthstone.GuiLogicMapper;
import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
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
import hearthstone.models.player.Player;
import hearthstone.models.card.Card;
import hearthstone.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoard extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton;
    private ImageButton endTurnButton;
    private BoardHeroButton myHero, enemyHero;
    protected final Player myPlayer, enemyPlayer;
    private final SoundPlayer soundPlayer;
    private final Game game;
    private PassiveButton myPassive;
    private EndTurnTimeLine endTurnTimeLine;
    private MyTimerTask endTurnLineTimerTask;

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

    private final int myLandStartY = midY;
    private final int myLandEndY = midY + 180;
    private final int myLandX = midX;
    private final int myLandY = myLandStartY;
    private final int myLandDisCard = 85;


    private final int enemyLandStartY = midY - 130;
    private final int enemyLandEndY = midY;
    private final int enemyLandX = midX;
    private final int enemyLandY = enemyLandStartY;
    private final int enemyLandDisCard = 85;

    private final int myDeckCardsNumberX = midX + 450;
    private final int myDeckCardsNumberY = midY + 105;

    private final int enemyDeckCardsNumberX = midX + 450;
    private final int enemyDeckCardsNumberY = midY - 95;

    protected final int extraPassiveX = 60;
    protected final int extraPassiveY = 50;

    protected final int endTurnTimeLineX = boardStartX;
    protected final int endTurnTimeLineY = midY;

    // Finals END

    public GameBoard(Player myPlayer, Player enemyPlayer, Game game) {
        this.myPlayer = myPlayer;
        this.enemyPlayer = enemyPlayer;
        this.game = game;

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

        justOnceLayout();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/game_board_background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(image, 0, 0, null);

        drawMyMana(g2, myPlayer.getMana(), myPlayer.getTurnNumber());

        drawEnemyMana(g2, enemyPlayer.getMana(), enemyPlayer.getTurnNumber());

        drawMyDeckNumberOfCards(g2, myPlayer.getDeck().getCards().size());
        drawEnemyDeckNumberOfCards(g2, enemyPlayer.getDeck().getCards().size());
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
        myPlayer.setPassive(passiveDialog0.getPassive());
        myPlayer.doPassives();

        PassiveDialog passiveDialog1 = new PassiveDialog(
                GameFrame.getInstance(),
                GameConfigs.initialPassives * SizeConfigs.medCardWidth + extraPassiveX,
                SizeConfigs.medCardHeight + extraPassiveY,
                Rand.getInstance().getRandomArray(
                        GameConfigs.initialPassives,
                        HearthStone.basePassives.size())
        );
        enemyPlayer.setPassive(passiveDialog1.getPassive());
        enemyPlayer.doPassives();
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
        if (myPlayer.getHeroPower() == null)
            return;
        HeroPowerButton heroPowerButton = new HeroPowerButton(myPlayer.getHeroPower(),
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight, false);
        heroPowerButton.setBounds(myHeroPowerX, myHeroPowerY,
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight);
        add(heroPowerButton);
    }

    private void drawEnemyHeroPower() {
        if (enemyPlayer.getHeroPower() == null)
            return;
        HeroPowerButton heroPowerButton = new HeroPowerButton(enemyPlayer.getHeroPower(),
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight, true);
        heroPowerButton.setBounds(enemyHeroPowerX, enemyHeroPowerY,
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight);
        add(heroPowerButton);
    }

    private void drawMyWeapon() {
        if (myPlayer.getWeapon() == null)
            return;
        WeaponButton weaponButton = new WeaponButton(myPlayer.getWeapon(),
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight, false);
        weaponButton.setBounds(myWeaponX, myWeaponY,
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight);
        add(weaponButton);
    }

    private void drawEnemyWeapon() {
        if (enemyPlayer.getWeapon() == null)
            return;
        WeaponButton weaponButton = new WeaponButton(enemyPlayer.getWeapon(),
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight, true);
        weaponButton.setBounds(enemyWeaponX, enemyWeaponY,
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight);
        add(weaponButton);
    }

    private void drawMyCardsOnHand() {
        ArrayList<Card> cards = myPlayer.getHand();
        if (cards.size() == 0)
            return;
        int dis = myHandDisCard / cards.size();
        int startX = myHandX;
        int startY = myHandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, true, false);

            makeMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth,
                    SizeConfigs.smallCardHeight);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);
            add(cardButton);
        }
    }

    protected void drawEnemyCardsOnHand() {
        ArrayList<Card> cards = enemyPlayer.getHand();
        if (cards.size() == 0)
            return;
        int dis = enemyHandDisCard / cards.size();
        int startX = enemyHandX;
        int startY = enemyHandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, 180, true, true);

            makeMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth,
                    SizeConfigs.smallCardHeight);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2),
                    startY,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);
            add(cardButton);
        }
    }

    private void drawMyCardsOnLand() {
        ArrayList<Card> cards = myPlayer.getLand();
        if (cards.size() == 0)
            return;

        int dis = myLandDisCard;
        int startX = myLandX;
        int startY = myLandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, false);

            makeMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidth / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidth,
                    SizeConfigs.smallCardHeight);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidth / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);
            add(cardButton);
        }
    }

    private void drawEnemyCardsOnLand() {
        ArrayList<Card> cards = enemyPlayer.getLand();
        if (cards.size() == 0)
            return;

        int dis = enemyLandDisCard;
        int startX = enemyLandX;
        int startY = enemyLandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight, true);

            makeMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidth / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidth,
                    SizeConfigs.smallCardHeight);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? SizeConfigs.smallCardWidth / 2 : 0),
                    startY,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);
            add(cardButton);
        }
    }

    private void drawEndTurnTimeLine() {
        int totalX = endTurnButtonX - endTurnTimeLine.getWidth() - endTurnTimeLineX;
        long length = 60000;
        long period = length / totalX;
        long warningTime = 9000;

        endTurnTimeLine.setBounds(endTurnTimeLineX, endTurnTimeLineY - SizeConfigs.endTurnTimeLineHeight / 2,
                SizeConfigs.endTurnTimeLineWidth, SizeConfigs.endTurnTimeLineHeight);

        SoundPlayer countdown = new SoundPlayer("/sounds/countdown.wav");

        endTurnLineTimerTask = new MyTimerTask(period, length, warningTime, new MyTask() {
            @Override
            public void startFunction() { }

            @Override
            public void periodFunction() {
                endTurnTimeLine.setBounds(
                        endTurnTimeLine.getX() + 1,
                        endTurnTimeLine.getY(),
                        SizeConfigs.endTurnTimeLineWidth,
                        SizeConfigs.endTurnTimeLineHeight);
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
        });
    }

    private void playCard(BoardCardButton button, Card card, BoardCardButton cardButton,
                          int startX, int startY, int width, int height) {
        try {
            if (!button.isEnemy()) {
                GuiLogicMapper.getInstance().playCard(myPlayer, card);
            } else {
                GuiLogicMapper.getInstance().playCard(enemyPlayer, card);
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

    private void makeMouseListener(BoardCardButton button, Card card,
                                   BoardCardButton cardButton,
                                   int startX, int startY, int width, int height) {
        CardButton bigCardButton = new CardButton(
                card,
                SizeConfigs.medCardWidth,
                SizeConfigs.medCardHeight,
                -1);

        if (!button.isEnemy()) {
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

        button.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (cardButton.isShowBig()) {
                    add(bigCardButton);
                    updateUI();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (cardButton.isShowBig()) {
                    remove(bigCardButton);
                    updateUI();
                }
            }

            @Override
            public void mouseReleased(MouseEvent E) {
                if (!isInMyLand(startX, startY) &&
                        isInMyLand(E.getX() + button.getX(), E.getY() + button.getY()) &&
                        game.getWhoseTurn() == 0) {
                    playCard(button, card, cardButton,
                            startX, startY, width, height);
                } else if (!isInEnemyLand(startX, startY) &&
                        isInEnemyLand(E.getX() + button.getX(), E.getY() + button.getY()) &&
                        game.getWhoseTurn() == 1) {
                    playCard(button, card, cardButton,
                            startX, startY, width, height);
                } else {
                    if (button.isShouldRotate()) {
                        button.setRotate(button.getInitialRotate());
                    }
                    button.setBounds(startX, startY, width, height);
                }
            }
        });

        button.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isInMyLand(startX, startY) || isInEnemyLand(startX, startY))
                    return;
                if ((game.getWhoseTurn() == 0 && button.isEnemy()))
                    return;
                if ((game.getWhoseTurn() == 1 && !button.isEnemy()))
                    return;

                int newX = e.getX() + button.getX();
                int newY = e.getY() + button.getY();

                button.setRotate(0);
                button.setBounds(newX, newY, width, height);
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

        endTurnTimeLine = new EndTurnTimeLine(SizeConfigs.endTurnTimeLineWidth,
                SizeConfigs.endTurnTimeLineHeight);

        myHero = new BoardHeroButton(HearthStone.currentAccount.getSelectedHero(), heroWidth, heroHeight, false); // player hero

        enemyHero = new BoardHeroButton(HearthStone.currentAccount.getSelectedHero(), heroWidth, heroHeight, true); // enemy hero

        myPassive = new PassiveButton(myPlayer.getPassive(),
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

    private void justOnceLayout(){
        endTurnTimeLine.setBounds(endTurnTimeLineX, endTurnTimeLineY - SizeConfigs.endTurnTimeLineHeight / 2,
                SizeConfigs.endTurnTimeLineWidth, SizeConfigs.endTurnTimeLineHeight);
        add(endTurnTimeLine);
        drawEndTurnTimeLine();
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

    private void closeBoard(){
        soundPlayer.stop();
        endTurnLineTimerTask.myStop();
    }

    private void removeComponents(){
        for(Component component : this.getComponents()){
            if(component instanceof EndTurnTimeLine)
                continue;
            this.remove(component);
        }
    }

    private void restart() {
        removeComponents();
        iconLayout();
        gameStuffLayout();
        revalidate();
        repaint();
    }
}