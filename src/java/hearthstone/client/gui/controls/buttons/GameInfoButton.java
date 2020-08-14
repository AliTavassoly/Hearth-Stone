package hearthstone.client.gui.controls.buttons;

import hearthstone.client.gui.game.GameFrame;
import hearthstone.models.GameInfo;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class GameInfoButton extends ImageButton implements MouseListener {
    int width, height;
    private GameInfo gameInfo;

    private static BufferedImage gameIcon;
    private BufferedImage gameInfoImage;
    private static BufferedImage gameInfoImageHovered;
    private static BufferedImage gameInfoImageNormal;

    private final int stringDis = 30;
    private final int stringStartY = 35;
    private final int stringsX = 150;

    public GameInfoButton(GameInfo gameInfo, int width, int height) {
        this.gameInfo = gameInfo;
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
            if (gameIcon == null)
                gameIcon = ImageResource.getInstance().getImage(
                        "/images/game_icon.png");

            if (gameInfoImageNormal == null) {
                gameInfoImageNormal = ImageResource.getInstance().getImage("/images/game_info.png");
            }

            if (gameInfoImageHovered == null)
                gameInfoImageHovered = ImageResource.getInstance().getImage("/images/game_info_hovered.png");

            if(gameInfoImage == null){
                gameInfoImage = gameInfoImageNormal;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.drawImage(gameIcon.getScaledInstance(95, 95,
                Image.SCALE_SMOOTH), 15, 15,
                95, 95, null);

        g2.drawImage(gameInfoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0,
                width, height, null);

        drawStringOnGameInfo(g2);
    }

    private void drawStringOnGameInfo(Graphics2D g2) {
        Font font = GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, 15);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);
        g2.setColor(new Color(69, 27, 27));

        g2.drawString("Player 1: " + gameInfo.getPlayer0(), stringsX, stringStartY);

        g2.drawString("Player 2: " + gameInfo.getPlayer1(), stringsX, stringStartY + 2 * stringDis);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        gameInfoImage = gameInfoImageHovered;
        repaint();
        revalidate();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        gameInfoImage = gameInfoImageNormal;
        repaint();
        revalidate();
    }
}
