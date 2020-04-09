package hearthstone.gui.credetials;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageButton extends JButton {
    private String path;

    public ImageButton(String path, int width, int height){
        this.path = path;
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);
        setMargin(new Insets(2, 3 + 35, 2, 3 + 35));
    }

    @Override
    protected void paintComponent(Graphics g){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (Exception e){
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }
}
