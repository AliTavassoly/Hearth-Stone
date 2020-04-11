package hearthstone.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class ImageButton extends JButton implements MouseListener {
    private String path, pathNormal, pathClicked;
    private int width, height;

    public ImageButton(String pathNormal, int width, int height){
        this.pathNormal = pathNormal;
        this.pathClicked = pathNormal;
        path = pathNormal;

        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);
        //setMargin(new Insets(2, 3 + 35, 2, 3 + 35));

        addMouseListener(this);
    }

    public ImageButton(String pathNormal, String pathClicked, int width, int height){
        this.pathNormal = pathNormal;
        this.pathClicked = pathClicked;
        path = pathNormal;

        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);
        setMargin(new Insets(2, 3 + 35, 2, 3 + 35));

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g){
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/" + path));
        } catch (Exception e){
            System.out.println(e);
        }
        g.clearRect(0, 0, width, height);
        g.drawImage(image, 0, 0, null);
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
        path = pathClicked;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        path = pathNormal;
        repaint();
    }
}
