package hearthstone.gui.game.play;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.SureDialog;
import hearthstone.gui.controls.hero.HeroButton;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PlayBoard extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton;
    private ImageButton endTurnButton;
    private BoardHeroButton myHero, opponentHero;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;

    private final int midX = DefaultSizes.gameFrameWidth / 2 - 60;
    private final int midY = DefaultSizes.gameFrameHeight / 2 - 43;

    private final int endTurnButtonX = 880;
    private final int endTurnButtonY = 300;

    private final int manaX = 770;
    private final int manaY = 640;
    private final int manaDis = 0;
    private final int manaStringX = 742;
    private final int manaStringY = 658;

    private final int myHeroX = midX;
    private final int myHeroY = DefaultSizes.gameFrameHeight - 240;

    private final int heroWidth = DefaultSizes.medHeroWidth;
    private final int heroHeight = DefaultSizes.medHeroHeight;

    private final int opponentHeroX = midX;
    private final int opponentHeroY = 60;


    public PlayBoard() {
        configPanel();

        makeIcons();

        iconLayout();

        makeGameStuff();

        gameStuffLayout();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/game_board_background.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        g.drawImage(image, 0, 0, null);

        drawMana(g2, 9);  // NUMBER OF MANA
    }

    private void drawMana(Graphics2D g, int number){
        int fontSize = 25;
        for(int i = 0; i < number; i++){
            try {
                BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(
                        "/images/mana.png"));
                g.drawImage(image.getScaledInstance(BoardDefault.manaWidth, BoardDefault.manaHeight,
                        Image.SCALE_SMOOTH),
                        manaX + (BoardDefault.manaWidth + manaDis) * i, manaY,
                        BoardDefault.manaWidth, BoardDefault.manaHeight, null);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        g.setFont(GameFrame.getInstance().getCustomFont(0, fontSize));
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        FontMetrics fontMetrics = g.getFontMetrics
                (GameFrame.getInstance().getCustomFont(0, fontSize));
        g.drawString(String.valueOf(number),
                manaStringX - fontMetrics.stringWidth(String.valueOf(number)) / 2, manaStringY);
    }

    private void makeIcons() {
        backButton = new ImageButton("icons/back.png", "icons/back_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new ImageButton("icons/minimize.png", "icons/minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        closeButton = new ImageButton("icons/close.png", "icons/close_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                SureDialog sureDialog = new SureDialog(GameFrame.getInstance(), "Are you sure you want to exit game?!",
                        DefaultSizes.dialogWidth, DefaultSizes.dialogHeight);
                boolean sure = sureDialog.getValue();
                if (sure)
                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
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
                SureDialog sureDialog = new SureDialog(GameFrame.getInstance(), "Are you sure you want to logout ?",
                        DefaultSizes.dialogWidth, DefaultSizes.dialogHeight);
                boolean sure = sureDialog.getValue();
                if (sure) {
                    System.exit(0);
                }
            }
        });
    }

    private void makeGameStuff() {
        endTurnButton = new ImageButton("End Turn", "end_turn.png",
                "end_turn_active.png", 15, 0,
                BoardDefault.endTurnButtonWidth, BoardDefault.endTurnButtonHeight);

        myHero = new BoardHeroButton(HearthStone.currentAccount.getSelectedHero(), heroWidth, heroHeight); // player hero

        opponentHero = new BoardHeroButton(HearthStone.currentAccount.getSelectedHero(), heroWidth, heroHeight); // opponent hero
    }

    private void configPanel() {
        setLayout(null);
        setVisible(true);
    }

    private void iconLayout() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(backButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);
    }

    private void gameStuffLayout(){
        endTurnButton.setBounds(endTurnButtonX, endTurnButtonY,
                BoardDefault.endTurnButtonWidth, BoardDefault.endTurnButtonHeight);
        add(endTurnButton);

        myHero.setBounds(myHeroX, myHeroY,
                heroWidth, heroHeight);
        add(myHero);

        opponentHero.setBounds(opponentHeroX, opponentHeroY,
                heroWidth, heroHeight);
        add(opponentHero);


    }
}
