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
import hearthstone.gui.game.play.boards.PracticeGameBoard;
import hearthstone.gui.game.play.boards.SoloGameBoard;
import hearthstone.logic.gamestuff.Game;
import hearthstone.logic.models.player.AIPlayer;
import hearthstone.logic.models.player.Player;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PlaySelectionPanel extends JPanel {
    private ImageButton backButton, logoutButton, minimizeButton, closeButton;
    private ImageButton playOnline, practicePlay, soloPlay;

    private static BufferedImage backgroundImage;

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
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/play_selection_background.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, null);
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

        practicePlay = new ImageButton("Practice", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        soloPlay = new ImageButton("Solo play", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        // listeners
        playOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // Nothing in this phase
            }
        });

        practicePlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Player myPlayer = HearthStone.currentAccount.getPlayer();
                Player enemyPlayer = HearthStone.currentAccount.getPlayer();

                makeNewPracticeGame(myPlayer, enemyPlayer);

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                        HearthStone.currentGameBoard);
            }
        });

        soloPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Player myPlayer = HearthStone.currentAccount.getPlayer();
                Player computerPlayer = new AIPlayer(HearthStone.currentAccount.getSelectedHero(),
                        HearthStone.currentAccount.getSelectedHero().getSelectedDeck());

                makeNewSoloGame(myPlayer, computerPlayer);

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                        HearthStone.currentGameBoard);
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

        practicePlay.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY + buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(practicePlay);

        soloPlay.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY + 2 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(soloPlay);
    }

    private void makeNewPracticeGame(Player player0, Player player1){
        player0.setEnemyPlayerId(1);
        player1.setEnemyPlayerId(0);

        HearthStone.currentGame = new Game(player0, player1);
        HearthStone.currentGameBoard = new PracticeGameBoard(0, 1);
        HearthStone.currentGame.startGame();
        HearthStone.currentGameBoard.restart();
    }

    private void makeNewSoloGame(Player player0, Player player1){
        player0.setEnemyPlayerId(1);
        player1.setEnemyPlayerId(0);

        HearthStone.currentGame = new Game(player0, player1);
        HearthStone.currentGameBoard = new SoloGameBoard(0, 1);
        HearthStone.currentGame.startGame();
        HearthStone.currentGameBoard.restart();
    }
}
