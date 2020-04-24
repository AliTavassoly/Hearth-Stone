package hearthstone.gui.controls;

import hearthstone.gui.credetials.CredentialsFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class ImageButton extends JButton implements MouseListener {
    private String imagePath, text;
    private String normalPath, activePath;
    private int width, height;
    private int textStyle, textSize;
    private Color currentColor, textColor, textColorActive;
    private boolean isRadio;
    private int tof;

    public ImageButton() {

    }

    public ImageButton(String normalPath, String activePath, int width, int height) {

        this.normalPath = normalPath;
        this.activePath = activePath;
        this.width = width;
        this.height = height;
        imagePath = normalPath;

        configButton();
    }

    public ImageButton(String text, String normalPath, String activePath,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.normalPath = normalPath;
        this.activePath = activePath;
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

    public ImageButton(String text, String imagePath, int tof, Color textColor, Color textColorActive,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.imagePath = imagePath;
        this.tof = tof;
        this.textColor = textColor;
        this.textColorActive = textColorActive;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.width = width;
        this.height = height;
        currentColor = textColor;

        configButton();
    }

    public ImageButton(String text, String imagePath, int tof, Color textColor, Color textColorActive,
                       boolean isRadio,
                       int textSize, int textStyle, int width, int height) {
        this.text = text;
        this.imagePath = imagePath;
        this.tof = tof;
        this.textColor = textColor;
        this.textColorActive = textColorActive;
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
        //setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    public String getText() {
        return text;
    }

    public int getTextSize() {
        return textSize;
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
        if (textColorActive != null) {
            currentColor = textColorActive;
            repaint();
        } else if (activePath != null) {
            imagePath = activePath;
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

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/" + imagePath));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
        //g2.drawImage(inputImage, 0, 0, null);
        g2.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);

        // DRAW TEXT
        if (text != null) {
            Font font = CredentialsFrame.getInstance().getCustomFont(textStyle, textSize);
            FontMetrics fontMetrics = g2.getFontMetrics(font);
            int textWidth = fontMetrics.stringWidth(text);

            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.setFont(font);
            g2.setColor(currentColor);

            g2.drawString(text, width / 2 - textWidth / 2,
                    (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (textColorActive != null) {
            currentColor = textColorActive;
            revalidate();
            repaint();
        } else if (activePath != null) {
            imagePath = activePath;
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
            imagePath = normalPath;
            revalidate();
            repaint();
        }
    }
}
