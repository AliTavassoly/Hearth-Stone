package hearthstone.gui.game.play;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.SureDialog;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.game.play.boardstuff.BoardCardButton;
import hearthstone.gui.game.play.boardstuff.BoardHeroButton;
import hearthstone.gui.settings.SettingsDialog;
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
    private final int boardStartX = DefaultSizes.gameFrameWidth / 2 - 360;
    private final int boardEndX = DefaultSizes.gameFrameWidth / 2 + 360;

    private final int midX = DefaultSizes.gameFrameWidth / 2;
    private final int midY = DefaultSizes.gameFrameHeight / 2 - 23;


    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;


    private final int endTurnButtonX = 882;
    private final int endTurnButtonY = 300;

    private final int manaX = 770;
    private final int manaY = 638;
    private final int manaDis = 0;
    private final int manaStringX = 742;
    private final int manaStringY = 658;

    private final int myHeroX = midX - 60;
    private final int myHeroY = DefaultSizes.gameFrameHeight - 236;

    private final int heroWidth = BoardDefault.medHeroWidth;
    private final int heroHeight = BoardDefault.medHeroHeight;

    private final int opponentHeroX = midX - 60;
    private final int opponentHeroY = 60;

    private final int myHandX = DefaultSizes.gameFrameWidth / 2 - 40;
    private final int myHandY = DefaultSizes.gameFrameHeight - 80;
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
    }

    private void drawMana(Graphics2D g, int number, int maxNumber) {
        int fontSize = 25;
        maxNumber = Math.min(maxNumber, HearthStone.maxManaInGame);

        for (int i = 0; i < number; i++) {
            try {
                BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(
                        "/images/mana.png"));
                g.drawImage(image.getScaledInstance(BoardDefault.manaWidth, BoardDefault.manaHeight,
                        Image.SCALE_SMOOTH),
                        manaX + (BoardDefault.manaWidth + manaDis) * i, manaY,
                        BoardDefault.manaWidth, BoardDefault.manaHeight, null);
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
                    DefaultSizes.smallCardWidth, DefaultSizes.smallCardHeight);

            makeMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2),
                    startY,
                    DefaultSizes.smallCardWidth,
                    DefaultSizes.smallCardHeight);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2),
                    startY,
                    DefaultSizes.smallCardWidth, DefaultSizes.smallCardHeight);
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
                    DefaultSizes.smallCardWidth, DefaultSizes.smallCardHeight);

            makeMouseListener(cardButton, card, cardButton,
                    startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? DefaultSizes.smallCardWidth / 2 : 0),
                    startY,
                    DefaultSizes.smallCardWidth,
                    DefaultSizes.smallCardHeight);

            cardButton.setBounds(startX + dis * (i - cards.size() / 2)
                            - (cards.size() % 2 == 1 ? DefaultSizes.smallCardWidth / 2 : 0),
                    startY,
                    DefaultSizes.smallCardWidth, DefaultSizes.smallCardHeight);
            add(cardButton);
        }
    }

    private void makeMouseListener(BoardCardButton button, Card card, BoardCardButton cardButton,
                                   int startX, int startY, int width, int height) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent E) {
                if (!isInLand(startX, startY) &&
                        isInLand(E.getX() + button.getX(), E.getY() + button.getY())) {
                    try {
                        myPlayer.playCard(card);
                        cardButton.makePlaySound();
                        restart();
                    } catch (HearthStoneException e) {
                        System.out.println(e.getMessage());
                        button.setBounds(startX, startY, width, height);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    button.setBounds(startX, startY, width, height);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        button.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isInLand(startX, startY))
                    return;
                int newX = e.getX() + button.getX();
                int newY = e.getY() + button.getY();
                button.setBounds(newX, newY, width, height);
                updateUI();
            }
        });
    }

    private void makeIcons() {
        backButton = new ImageButton("icons/back.png",
                "icons/back_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        settingsButton = new ImageButton("icons/settings.png",
                "icons/settings_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new ImageButton("icons/minimize.png",
                "icons/minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        closeButton = new ImageButton("icons/close.png",
                "icons/close_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                SureDialog sureDialog = new SureDialog(GameFrame.getInstance(), "Are you sure you want to exit game?!",
                        DefaultSizes.dialogWidth, DefaultSizes.dialogHeight);
                boolean sure = sureDialog.getValue();
                if (sure) {
                    try {
                        DataBase.save();
                    } catch (HearthStoneException e){
                        System.out.println(e.getMessage());
                    } catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
                }
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                SettingsDialog settingsDialog = new SettingsDialog(GameFrame.getInstance(),
                        DefaultSizes.settingsWidth, DefaultSizes.settingsHeight);
            }
        });

        minimizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().setState(Frame.ICONIFIED);
                GameFrame.getInstance().setState(Frame.NORMAL);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                SureDialog sureDialog = new SureDialog(GameFrame.getInstance(), "Are you sure you want to Exit Game ?",
                        DefaultSizes.dialogWidth, DefaultSizes.dialogHeight);
                boolean sure = sureDialog.getValue();
                if (sure) {
                    try {
                        DataBase.save();
                    } catch (HearthStoneException e){
                        System.out.println(e.getMessage());
                    } catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    System.exit(0);
                }
            }
        });
    }

    private void makeGameStuff() {
        endTurnButton = new ImageButton("End Turn", "end_turn.png",
                "end_turn_active.png", 15, 1,
                BoardDefault.endTurnButtonWidth, BoardDefault.endTurnButtonHeight);
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

    private void configPanel() {
        setLayout(null);
        setDoubleBuffered(true);
        setVisible(true);
    }

    private void iconLayout() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(backButton);

        settingsButton.setBounds(iconX, startIconY + iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(settingsButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);
    }

    private void gameStuffLayout() {
        drawCardsOnHand();

        drawCardsOnLand();

        endTurnButton.setBounds(endTurnButtonX, endTurnButtonY,
                BoardDefault.endTurnButtonWidth, BoardDefault.endTurnButtonHeight);
        add(endTurnButton);

        myHero.setBounds(myHeroX, myHeroY,
                heroWidth, heroHeight);
        add(myHero);

        opponentHero.setBounds(opponentHeroX, opponentHeroY,
                heroWidth, heroHeight);
        add(opponentHero);

        /*JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setBounds(midX, myLandStartY, boardEndX - boardStartX, 180);
        add(panel);*/
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
