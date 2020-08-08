package hearthstone.client.gui.game;

import hearthstone.client.gui.BaseFrame;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GameFrame extends BaseFrame {
    private static GameFrame gameFrame;

    private GameFrame() {
        configFrame();

        changeLocationMouseListener();
    }

    private void changeLocationMouseListener() {
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent mouseEvent) {
                GameFrame.this.setLocation(
                        (int) mouseEvent.getLocationOnScreen().getX() - GUIConfigs.gameFrameWidth / 2,
                        (int) mouseEvent.getLocationOnScreen().getY() - GUIConfigs.gameFrameHeight / 2
                );
            }

            public void mouseMoved(MouseEvent mouseEvent) { }
        });
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

        this.setSize(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(new MainMenuPanel());

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }

    public void setCursor(String path) {
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