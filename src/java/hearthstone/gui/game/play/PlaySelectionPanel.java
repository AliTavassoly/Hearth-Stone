package hearthstone.gui.game.play;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.game.collection.HeroSelection;
import hearthstone.gui.game.market.MarketPanel;
import hearthstone.gui.game.status.StatusPanel;
import hearthstone.util.HearthStoneException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PlaySelectionPanel extends JPanel {
    private ImageButton backButton, logoutButton, minimizeButton, closeButton;
    private ImageButton playOnline, playOffline;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int halfButtonDisY = 30;
    private final int startButtonY = DefaultSizes.gameFrameHeight / 2 + 100;


    public PlaySelectionPanel() {
        configPanel();

        makeIcons();

        makeButtons();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/play_selection_background.jpg"));
        } catch (Exception e) {
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new ImageButton("icons/back.png", "icons/back_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        logoutButton = new ImageButton("icons/logout.png", "icons/logout_active.png",
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
                System.exit(0);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HearthStone.logout();
                } catch (HearthStoneException e){
                    System.out.println(e.getMessage());
                } catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                GameFrame.getInstance().setVisible(false);
                GameFrame.getInstance().dispose();
                CredentialsFrame.getNewInstance().setVisible(true);
            }
        });
    }

    private void makeButtons() {
        playOffline = new ImageButton("Play Offline", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);

        playOnline = new ImageButton("Play Online", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);

        // listeners

        playOffline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // SHOW BOARD
                //GameFrame.getInstance(GameFrame.getInstance(), new PlayBoard());
            }
        });

        playOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // Nothing in this faze
            }
        });
    }

    private void configPanel() {
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);

        // BUTTONS
        playOnline.setBounds(DefaultSizes.gameFrameWidth / 2 - DefaultSizes.largeButtonWidth / 2,
                startButtonY - DefaultSizes.largeButtonHeight,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);
        add(playOnline);

        playOffline.setBounds(DefaultSizes.gameFrameWidth / 2 - DefaultSizes.largeButtonWidth / 2,
                startButtonY + halfButtonDisY,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);
        add(playOffline);
    }
}
