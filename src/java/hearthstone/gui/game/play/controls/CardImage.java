package hearthstone.gui.game.play.controls;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CardImage{
    private BoardCardButton cardButton;
    private int x, y, width, height;

    public CardImage(int x, int y, int width, int height, BoardCardButton cardButton){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.cardButton = cardButton;
    }

    public BoardCardButton getCardButton() {
        return cardButton;
    }
    public void setCardButton(BoardCardButton cardButton) {
        this.cardButton = cardButton;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getImage(){
        int w = cardButton.getWidth();
        int h = cardButton.getHeight();

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        cardButton.paint(g);

        return image;
    }
}
