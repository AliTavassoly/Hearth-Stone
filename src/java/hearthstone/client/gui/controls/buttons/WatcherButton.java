package hearthstone.client.gui.controls.buttons;

import hearthstone.client.gui.game.GameFrame;
import hearthstone.models.WatcherInfo;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class WatcherButton extends ImageButton{
    int width, height;
    private WatcherInfo watcherInfo;

    private BufferedImage watcherInfoImage;
    private static BufferedImage watcherInfoImageHovered;
    private static BufferedImage watcherInfoImageNormal;

    private final int stringStartY = 30;
    private final int stringsX = 60;

    public WatcherButton(WatcherInfo watcherInfo, int width, int height) {
        this.watcherInfo = watcherInfo;
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        try {
            if (watcherInfoImageNormal == null) {
                watcherInfoImageNormal = ImageResource.getInstance().getImage("/images/watcher_info.png");
            }

            if (watcherInfoImageHovered == null)
                watcherInfoImageHovered = ImageResource.getInstance().getImage("/images/watcher_info_hovered.png");

            if(watcherInfoImage == null){
                watcherInfoImage = watcherInfoImageNormal;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.drawImage(watcherInfoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0,
                width, height, null);

        drawStringOnWatcherInfo(g2);
    }

    private void drawStringOnWatcherInfo(Graphics2D g2) {
        Font font = GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, 15);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);
        g2.setColor(new Color(69, 27, 27));

        g2.drawString(watcherInfo.getUsername(), stringsX, stringStartY);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        watcherInfoImage = watcherInfoImageHovered;
        repaint();
        revalidate();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        watcherInfoImage = watcherInfoImageNormal;
        repaint();
        revalidate();
    }
}
