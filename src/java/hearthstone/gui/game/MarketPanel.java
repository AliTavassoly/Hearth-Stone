package hearthstone.gui.game;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.ImageButton;
import hearthstone.gui.game.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MarketPanel extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;

    public MarketPanel(){
        configPanel();

        backButton = new ImageButton("back.png", "back_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new ImageButton("minimize.png", "minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        closeButton = new ImageButton("close.png", "close_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().setState(Frame.ICONIFIED);
                GameFrame.getInstance().setState(Frame.NORMAL);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/main_menu_background.jpg"));
        } catch (Exception e){
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void configPanel(){
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent(){
        //
        backButton.setBounds(iconX, startIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(backButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);
    }
}
