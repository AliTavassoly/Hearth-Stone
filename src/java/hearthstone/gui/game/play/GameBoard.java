package hearthstone.gui.game.play;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.SureDialog;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.controls.icons.SettingIcon;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.game.play.controls.BoardCardButton;
import hearthstone.gui.game.play.controls.BoardHeroButton;
import hearthstone.gui.game.play.controls.HeroPowerButton;
import hearthstone.gui.game.play.controls.WeaponButton;
import hearthstone.logic.GameConfigs;
import hearthstone.logic.gamestuff.Game;
import hearthstone.logic.models.Player;
import hearthstone.logic.models.card.Card;
import hearthstone.util.HearthStoneException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GameBoard extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, settingsButton;
    private ImageButton endTurnButton;
    private BoardHeroButton myHero, opponentHero;
    private Player myPlayer, opponentPlayer;
    private Game game;

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
    private final int manaStringX = 742;
    private final int manaStringY = 658;

    private final int myHeroX = midX - 60;
    private final int myHeroY = SizeConfigs.gameFrameHeight - 236;

    private final int myHeroPowerX = midX + 55;
    private final int myHeroPowerY = SizeConfigs.gameFrameHeight - 190;

    private final int myWeaponX = midX - 165;
    private final int myWeaponY = SizeConfigs.gameFrameHeight - 190;

    private final int heroWidth = SizeConfigs.medHeroWidth;
    private final int heroHeight = SizeConfigs.medHeroHeight;

    private final int opponentHeroX = midX - 60;
    private final int opponentHeroY = 60;

    private final int myHandX = SizeConfigs.gameFrameWidth / 2 - 40;
    private final int myHandY = SizeConfigs.gameFrameHeight - 80;
    private final int myHandDisCard = 220;

    private final int myLandStartY = midY;
    private final int myLandEndY = midY + 180;

    private final int myLandX = midX;
    private final int myLandY = myLandStartY;
    private final int myLandDisCard = 85;

    private final int deckCardsNumberX = midX + 450;
    private final int deckCardsNumberY = midY + 100;

    // Finals END

    public GameBoard(Player myPlayer, Player opponentPlayer, Game game) {
        this.myPlayer = myPlayer;
        this.opponentPlayer = opponentPlayer;
        this.game = game;

        configPanel();

        makeIcons();

        iconLayout();

        makeGameStuff();

        gameStuffLayout();
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
            System.out.println(e.getMessage());
        }
        g.drawImage(image, 0, 0, null);

        drawMana(g2, myPlayer.getMana(), myPlayer.getTurnNumber());

        drawDeckNumberOfCards(g2, myPlayer.getDeck().getCards().size());

        drawHeroPower();

        drawWeapon();
    }

    private void configPanel() {
        setLayout(null);
        setDoubleBuffered(true);
        setVisible(true);
    }

    private void drawMana(Graphics2D g, int number, int maxNumber) {
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
                System.out.println(e.getMessage());
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
                manaStringX - fontMetrics.stringWidth(text) / 2, manaStringY);
    }

    private void drawDeckNumberOfCards(Graphics2D g, int number) {
        int fontSize = 50;

        g.setFont(GameFrame.getInstance().getCustomFont(0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(0, fontSize));

        g.drawString(String.valueOf(number),
                deckCardsNumberX - fontMetrics.stringWidth(String.valueOf(number)) / 2,
                deckCardsNumberY);
    }

    private void drawHeroPower() {
        if (myPlayer.getHeroPower() == null)
            return;
        HeroPowerButton heroPowerButton = new HeroPowerButton(myPlayer.getHeroPower(),
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight);
        heroPowerButton.setBounds(myHeroPowerX, myHeroPowerY,
                SizeConfigs.heroPowerWidth, SizeConfigs.heroPowerHeight);
        add(heroPowerButton);
    }

    private void drawWeapon() {
        if (myPlayer.getWeapon() == null)
            return;
        WeaponButton weaponButton = new WeaponButton(myPlayer.getWeapon(),
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight);
        weaponButton.setBounds(myWeaponX, myWeaponY,
                SizeConfigs.weaponWidth, SizeConfigs.weaponHeight);
        add(weaponButton);
    }

    private void drawCardsOnHand() {
        ArrayList<Card> cards = myPlayer.getHand();
        if (cards.size() == 0)
            return;
        int dis = myHandDisCard / cards.size();
        int startX = myHandX;
        int startY = myHandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);

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

    private void drawCardsOnLand() {
        ArrayList<Card> cards = myPlayer.getLand();
        if (cards.size() == 0)
            return;
        int dis = myLandDisCard;
        int startX = myLandX;
        int startY = myLandY;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            BoardCardButton cardButton = new BoardCardButton(card,
                    SizeConfigs.smallCardWidth, SizeConfigs.smallCardHeight);

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

    private void playCard(BoardCardButton button, Card card, BoardCardButton cardButton,
                          int startX, int startY, int width, int height) {
        try {
            myPlayer.playCard(card);
            cardButton.makePlaySound();
            hearthstone.util.Logger.saveLog("Play card",
                    card.getName() + " played");
            restart();
        } catch (HearthStoneException e) {
            try {
                hearthstone.util.Logger.saveLog("ERROR",
                        e.getClass().getName() + ": " + e.getMessage()
                                + "\nStack Trace: " + e.getStackTrace());
            } catch (Exception f) {
            }

            System.out.println(e.getMessage());
            button.setBounds(startX, startY, width, height);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void makeMouseListener(BoardCardButton button, Card card, BoardCardButton cardButton,
                                   int startX, int startY, int width, int height) {
        button.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {

            }
            public void mousePressed(MouseEvent e) {

            }
            public void mouseEntered(MouseEvent e) {

            }
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent E) {
                if (!isInLand(startX, startY) &&
                        isInLand(E.getX() + button.getX(), E.getY() + button.getY())) {
                    playCard(button, card, cardButton,
                            startX, startY, width, height);
                } else {
                    button.setBounds(startX, startY, width, height);
                }
            }
        });

        button.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isInLand(startX, startY))
                    return;
                int newX = e.getX() + button.getX();
                int newY = e.getY() + button.getY();
                button.setBounds(newX, newY, width, height);
                //updateUI();
            }
        });
    }

    private void makeIcons() {
        backButton = new ImageButton("icons/back.png",
                "icons/back_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        settingsButton = new SettingIcon("icons/settings.png",
                "icons/settings_active.png",
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

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    hearthstone.util.Logger.saveLog("Click_button",
                            "back_button");
                } catch (Exception e) {
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
                    } catch (HearthStoneException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
                }
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
                try {
                    File file = new File(this.getClass().getResource(
                            "/sounds/ding.wav").getFile());
                    AudioInputStream audioInputStream =
                            AudioSystem.getAudioInputStream(file.getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);

                    FloatControl gainControl =
                            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(CredentialsFrame.getInstance().getSoundValue());

                    clip.start();

                    hearthstone.util.Logger.saveLog("Click_button",
                            "End turn_button");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                game.endTurn();
                restart();
            }
        });

        myHero = new BoardHeroButton(HearthStone.currentAccount.getSelectedHero(), heroWidth, heroHeight); // player hero

        opponentHero = new BoardHeroButton(HearthStone.currentAccount.getSelectedHero(), heroWidth, heroHeight); // opponent hero
    }

    private void iconLayout() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(backButton);

        settingsButton.setBounds(iconX, startIconY + iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(settingsButton);

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
        drawCardsOnHand();

        drawCardsOnLand();

        endTurnButton.setBounds(endTurnButtonX, endTurnButtonY,
                SizeConfigs.endTurnButtonWidth, SizeConfigs.endTurnButtonHeight);
        add(endTurnButton);

        myHero.setBounds(myHeroX, myHeroY,
                heroWidth, heroHeight);
        add(myHero);

        opponentHero.setBounds(opponentHeroX, opponentHeroY,
                heroWidth, heroHeight);
        add(opponentHero);
    }

    private boolean isInLand(int x, int y) {
        return x >= boardStartX && x <= boardEndX && y >= myLandStartY && y <= myLandEndY;
    }

    private void restart() {
        removeAll();
        iconLayout();
        gameStuffLayout();
        revalidate();
        repaint();
    }
}