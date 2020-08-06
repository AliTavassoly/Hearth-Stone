package hearthstone.client.gui.game.waitingpanels;

import hearthstone.client.data.GUIConfigs;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LoadingPanel extends JPanel {
    public static BufferedImage backgroundImage;

    public LoadingPanel(){
        configPanel();
    }

    private void configPanel() {
        setSize(new Dimension(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight));
    }

    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/loading_background.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.drawImage(backgroundImage, 0, 0, null);
    }
}
