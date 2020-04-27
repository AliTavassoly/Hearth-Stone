package hearthstone.gui.game.play;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.SureDialog;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.logic.gamestuff.Game;
import hearthstone.logic.models.Player;
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
    private Player player;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int halfButtonDisY = 30;
    private final int startButtonY = DefaultSizes.gameFrameHeight / 2 + 100;


    public PlaySelectionPanel(Player player) {
        this.player = player;

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
            System.out.println(e.getMessage());
        }
        g.drawImage(image, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
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
                Game game = new Game(player, player);
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                        new GameBoard(player, player, game));
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
