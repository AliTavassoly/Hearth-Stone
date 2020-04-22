package hearthstone.gui.game.play;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.controls.SureDialog;
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
    private ImageButton manaButton;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;

    private final int midX = DefaultSizes.gameFrameWidth / 2;
    private final int midY = DefaultSizes.gameFrameHeight / 2;

    private final int endTurnButtonX = 880;
    private final int endTurnButtonY = 303;

    private final int manaX = 695;
    private final int manaY = 590;

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
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/board_background.jpg"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        g.drawImage(image, 0, 0, null);
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
        endTurnButton = new ImageButton("end_turn.png", "end_turn_active.png",
                BoardDefault.endTurnButtonWidth, BoardDefault.endTurnButtonHeight);

        manaButton = new ImageButton("mana.png", "mana_active.png", BoardDefault.manaWidth, BoardDefault.manaHeight);
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

        manaButton.setBounds(manaX, manaY,
                BoardDefault.manaWidth, BoardDefault.manaHeight);
        add(manaButton);
    }
}
