package hearthstone.gui.game.play;

import hearthstone.HearthStone;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.logic.gamestuff.Game;
import hearthstone.logic.models.ComputerPlayer;
import hearthstone.logic.models.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PlaySelectionPanel extends JPanel {
    private ImageButton backButton, logoutButton, minimizeButton, closeButton;
    private ImageButton playOnline, playMySelf, playComputer;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int buttonDisY = 100;
    private final int startButtonY = SizeConfigs.gameFrameHeight / 2;


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
            e.printStackTrace();
        }
        g.drawImage(image, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
    }

    private void makeButtons() {
        playOnline = new ImageButton("Play Online", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        playMySelf = new ImageButton("Play Yourself", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        playComputer = new ImageButton("Play Computer", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        // listeners
        playOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // Nothing in this faze
            }
        });

        playMySelf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Player myPlayer = HearthStone.currentAccount.getPlayer();
                Player enemyPlayer = HearthStone.currentAccount.getPlayer();

                Game game = new Game(myPlayer, enemyPlayer);
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                        new GameBoardSelfPlay(myPlayer, enemyPlayer, game));
            }
        });

        playComputer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Player myPlayer = HearthStone.currentAccount.getPlayer();
                Player computerPlayer = new ComputerPlayer(HearthStone.currentAccount.getSelectedHero(),
                        HearthStone.currentAccount.getSelectedHero().getSelectedDeck());

                Game game = new Game(myPlayer, computerPlayer);
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                        new GameBoardComputerPlay(myPlayer, computerPlayer, game));
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
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(closeButton);

        // BUTTONS
        playOnline.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(playOnline);

        playMySelf.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY + buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(playMySelf);

        playComputer.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY + 2 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(playComputer);
    }
}
