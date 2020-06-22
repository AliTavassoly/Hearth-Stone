package hearthstone.gui.game.play.dialogs;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MessageDialog extends ImageButton {
    private String text;
    private int width, height;
    private int textStyle, textSize;
    private Color currentColor, textColor;

    private BufferedImage backgroundImage;

    public MessageDialog(String text, Color textColor,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.textColor = textColor;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.width = width;
        this.height = height;
        currentColor = textColor;

        configButton();
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        //setVisible(false);
        setOpaque(true);

        addMouseListener(this);

        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/" + "think_dialog.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(backgroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);

        // DRAW TEXT
        drawText(g2);
    }

    private void drawText(Graphics2D g) {
        if (text != null) {
            Font font = CredentialsFrame.getInstance().getCustomFont(textStyle, textSize);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            int textWidth = fontMetrics.stringWidth(text);

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g.setFont(font);
            g.setColor(currentColor);

            g.drawString(text, width / 2 - textWidth / 2,
                    (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) - 10);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        setVisible(false);
    }
    public void mousePressed(MouseEvent mouseEvent) { }
    public void mouseReleased(MouseEvent mouseEvent) { }
    public void mouseEntered(MouseEvent mouseEvent) { }
    public void mouseExited(MouseEvent mouseEvent) { }
}
