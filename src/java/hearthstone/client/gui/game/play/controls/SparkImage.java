package hearthstone.client.gui.game.play.controls;

import hearthstone.util.getresource.ImageResource;

import java.awt.image.BufferedImage;

public class SparkImage {
    private int x;
    private int y;
    private int width;
    private int height;
    private String imagePath;
    private static BufferedImage[] sparks = new BufferedImage[10];

    public SparkImage(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public BufferedImage getImage(){
        try {
            int id = (imagePath.charAt(imagePath.length() - 5) - '0');
            if(sparks[id] == null)
                sparks[id] = ImageResource.getInstance().getImage(imagePath);
            return sparks[id];
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
