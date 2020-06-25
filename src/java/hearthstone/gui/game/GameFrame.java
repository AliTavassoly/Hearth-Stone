package hearthstone.gui.game;

import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;

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

    public void setCursor(String path){
        Image cursorImage = null;
        try {
            cursorImage = ImageResource.getInstance().getImage(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point(0, 0), "customCursor");
        setCursor(customCursor);
    }
}