package hearthstone.gui.game;

import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;

import javax.sound.sampled.Clip;
import javax.swing.*;

public class GameFrame extends BaseFrame {
    private MainMenuPanel mainMenuPanel;
    private static GameFrame gameFrame;

    private GameFrame() {
        mainMenuPanel = new MainMenuPanel();

        configFrame();
    }

    public static GameFrame getInstance() {
        if (gameFrame == null) {
            return gameFrame = new GameFrame();
        } else {
            return gameFrame;
        }
    }

    public static GameFrame getNewInstance() {
        return gameFrame = new GameFrame();
    }

    private void configFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(SizeConfigs.gameFrameWidth, SizeConfigs.gameFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainMenuPanel);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }
}
