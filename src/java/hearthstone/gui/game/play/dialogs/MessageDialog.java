package hearthstone.gui.game.play.dialogs;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.util.getresource.ImageResource;
import hearthstone.util.timer.MyTask;
import hearthstone.util.timer.MyTimerTask;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MessageDialog extends ImageButton {
    private String text;
    private final int width, height, textStyle;
    private int textSize;
    private final Color currentColor;
    private final int tof;

    private long showTime;

    private String imagePath;

    public MessageDialog(String text, Color textColor,
                       int textSize, int textStyle, int tof, long showTime, int width, int height) {
        this.text = text;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.tof = tof;
        this.showTime = showTime;
        this.width = width;
        this.height = height;
        currentColor = textColor;

        configButton();
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        setVisible(false);
        setOpaque(true);

        addMouseListener(this);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setVisibility(boolean visibility){
        if(visibility){
            setVisible(true);
            MyTimerTask myTimerTask = new MyTimerTask(showTime, showTime, new MyTask() {
                public void startFunction() { }
                public void periodFunction() { }
                public void warningFunction() { }
                public void finishedFunction() { }
                public void closeFunction() { setVisible(false); }
                public boolean finishCondition() { return false; }
            });
        } else {
            setVisible(false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage backgroundImage = ImageResource.getInstance().getImage(imagePath);

        g2.drawImage(backgroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);

        // DRAW TEXT
        drawText(g2);
    }

    private void drawText(Graphics2D g) {
        if (text != null) {
            Font font = CredentialsFrame.getInstance().getCustomFont(textStyle, textSize);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            int textWidth = fontMetrics.stringWidth(text);

            while (textWidth >= width){
                textSize--;

                font = CredentialsFrame.getInstance().getCustomFont(textStyle, textSize);
                fontMetrics = g.getFontMetrics(font);
                textWidth = fontMetrics.stringWidth(text);
            }

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g.setFont(font);
            g.setColor(currentColor);

            g.drawString(text, width / 2 - textWidth / 2,
                    (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);
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
