package hearthstone.gui.controls.card;

import hearthstone.gui.controls.ImageButton;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CardBackButton extends ImageButton implements MouseListener {
    private int width, height;
    private int id;

    private BufferedImage cardImage;

    public CardBackButton(int id, int width, int height){
        this.width = width;
        this.height = height;
        this.id = id;

        configPanel();
    }

    private void configPanel() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if(cardImage == null)
            cardImage = ImageResource.getInstance().getImage("/images/cards/cards_back/" +
                    "card_back" + id + ".png");

        g2.drawImage(cardImage.getScaledInstance(
                width, height,
                Image.SCALE_SMOOTH),
                0, 0,
                width, height,
                null);
    }

    public int getId(){
        return id;
    }
}
