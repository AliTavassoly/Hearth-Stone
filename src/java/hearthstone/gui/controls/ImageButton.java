package hearthstone.gui.controls;

import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class ImageButton extends JButton implements MouseListener {
    private String imagePath, text;
    private String normalPath, hoveredPath;
    private int width, height;
    private int textStyle, textSize;
    private Color currentColor, textColor, hoveredTextColor;
    private boolean isRadio;
    private int tof;

    private BufferedImage image, normalImage, hoveredImage;

    public ImageButton() {

    }

    public ImageButton(String normalPath, String hoveredPath, int width, int height) {
        this.normalPath = normalPath;
        this.hoveredPath = hoveredPath;
        this.width = width;
        this.height = height;
        imagePath = normalPath;

        configButton();
    }

    public ImageButton(String text, String normalPath, String hoveredPath,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.normalPath = normalPath;
        this.hoveredPath = hoveredPath;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.width = width;
        this.height = height;
        imagePath = normalPath;

        configButton();
    }

    public ImageButton(String text, String imagePath, int tof,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.imagePath = imagePath;
        this.tof = tof;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.width = width;
        this.height = height;
        currentColor = textColor;

        configButton();
    }

    public ImageButton(String text, String imagePath, int tof, Color textColor,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.imagePath = imagePath;
        this.tof = tof;
        this.textColor = textColor;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.width = width;
        this.height = height;
        currentColor = textColor;

        configButton();
    }

    public ImageButton(String text, String imagePath, int tof, Color textColor, Color hoveredTextColor,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.imagePath = imagePath;
        this.tof = tof;
        this.textColor = textColor;
        this.hoveredTextColor = hoveredTextColor;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.width = width;
        this.height = height;
        currentColor = textColor;

        configButton();
    }

    public ImageButton(String text, String imagePath, int tof, Color textColor, Color hoveredTextColor,
                       boolean isRadio,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.imagePath = imagePath;
        this.tof = tof;
        this.textColor = textColor;
        this.hoveredTextColor = hoveredTextColor;
        this.isRadio = isRadio;
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

        addMouseListener(this);

        try {
            if (normalPath != null) {
                normalImage = ImageResource.getInstance().getImage("/images/" + normalPath);
                image = normalImage;
            }
            if (hoveredPath != null)
                hoveredImage = ImageResource.getInstance().getImage("/images/" + hoveredPath);

            if (normalPath == null)
                image = ImageResource.getInstance().getImage("/images/" + imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        return text;
    }

    public void mouseExited() {
        if (textColor != null) {
            currentColor = textColor;
            repaint();
        } else if (normalPath != null) {
            imagePath = normalPath;
            repaint();
        }
    }

    public void mouseEntered() {
        if (hoveredTextColor != null) {
            currentColor = hoveredTextColor;
            repaint();
        } else if (hoveredPath != null) {
            imagePath = hoveredPath;
            repaint();
        }
    }

    public void setRadio(boolean isRadio) {
        this.isRadio = isRadio;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);

        // DRAW TEXT
        drawText(g2);
    }

    private void drawText(Graphics2D g) {
        if (text != null) {
            Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT, textStyle, textSize);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            int textWidth = fontMetrics.stringWidth(text);

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g.setFont(font);
            g.setColor(currentColor);

            g.drawString(text, width / 2 - textWidth / 2,
                    (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (hoveredTextColor != null) {
            currentColor = hoveredTextColor;
            revalidate();
            repaint();
        } else if (hoveredPath != null) {
            image = hoveredImage;
            revalidate();
            repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (isRadio)
            return;
        if (textColor != null) {
            currentColor = textColor;
            revalidate();
            repaint();
        } else if (normalPath != null) {
//            imagePath = normalPath;
            image = normalImage;
            revalidate();
            repaint();
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {

    }

    public void mousePressed(MouseEvent mouseEvent) {

    }

    public void mouseReleased(MouseEvent mouseEvent) {

    }
}
