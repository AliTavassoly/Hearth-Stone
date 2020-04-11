package hearthstone.gui.game;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.ImageButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel {
    private ImageButton settingsButton, logoutButton, minimizeButton, closeButton;
    private ImageButton playButton, collectionButton, marketButton, statusButton;
    private ImageButton logoButton;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startButtonY = 219;
    private final int buttonX = 870;
    private final int buttonDis = 97;


    public MainMenuPanel(){
        configPanel();

        makeIcons();

        makeLogo();

        makeButtons();

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

    private void makeIcons(){
        logoutButton = new ImageButton("logout.png", "logout_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        settingsButton = new ImageButton("settings.png", "settings_active.png",
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
    }

    private void makeLogo(){
        logoButton = new ImageButton("logo.png",
                DefaultSizes.mainMenuLogoWidth,
                DefaultSizes.mainMenuLogoHeight);
    }

    private void makeButtons(){
        playButton = new ImageButton("register.png", "register_active.png",
                DefaultSizes.mainMenuButtonWidth,
                DefaultSizes.mainMenuButtonHeight);

        collectionButton = new ImageButton("register.png", "register_active.png",
                DefaultSizes.mainMenuButtonWidth,
                DefaultSizes.mainMenuButtonHeight);

        marketButton = new ImageButton("register.png", "register_active.png",
                DefaultSizes.mainMenuButtonWidth,
                DefaultSizes.mainMenuButtonHeight);

        statusButton = new ImageButton("register.png", "register_active.png",
                DefaultSizes.mainMenuButtonWidth,
                DefaultSizes.mainMenuButtonHeight);
    }

    private void configPanel(){
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent(){
        // LOGO
        logoButton.setBounds(buttonX + DefaultSizes.mainMenuButtonWidth / 2 - DefaultSizes.mainMenuLogoWidth / 2,
                startButtonY - (int)(1.80 * buttonDis),
                DefaultSizes.mainMenuLogoWidth,
                DefaultSizes.mainMenuLogoHeight);
        add(logoButton);

        // ICONS
        logoutButton.setBounds(iconX, startIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(logoutButton);

        settingsButton.setBounds(iconX, startIconY + iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(settingsButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);

        // BUTTONS
        playButton.setBounds(buttonX, startButtonY + 0 * buttonDis,
                DefaultSizes.mainMenuButtonWidth,
                DefaultSizes.mainMenuButtonHeight);
        add(playButton);

        collectionButton.setBounds(buttonX, startButtonY + 1 * buttonDis,
                DefaultSizes.mainMenuButtonWidth,
                DefaultSizes.mainMenuButtonHeight);
        add(collectionButton);

        marketButton.setBounds(buttonX, startButtonY + 2 * buttonDis,
                DefaultSizes.mainMenuButtonWidth,
                DefaultSizes.mainMenuButtonHeight);
        add(marketButton);

        statusButton.setBounds(buttonX, startButtonY + 3 * buttonDis,
                DefaultSizes.mainMenuButtonWidth,
                DefaultSizes.mainMenuButtonHeight);
        add(statusButton);
    }
}
