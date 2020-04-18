package hearthstone.gui.game;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel {
    private ImageButton settingsButton, logoutButton, minimizeButton, closeButton;
    private ImageButton playButton, collectionButton, marketButton, statusButton;
    private ImagePanel logoImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startButtonY = 300;
    private final int halfButtonDisX = 100;
    private final int firstButtonX = DefaultSizes.gameFrameWidth / 2 - halfButtonDisX
            - DefaultSizes.largeButtonWidth;
    private final int secondButtonX = DefaultSizes.gameFrameWidth / 2 + halfButtonDisX;
    private final int buttonDisY = 97;


    public MainMenuPanel() {
        configPanel();

        makeIcons();

        makeLogo();

        makeButtons();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/main_menu_background.png"));
        } catch (Exception e) {
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void makeIcons() {
        logoutButton = new ImageButton("icons/logout.png", "icons/logout_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        settingsButton = new ImageButton("icons/settings.png", "icons/settings_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new ImageButton("icons/minimize.png", "icons/minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        closeButton = new ImageButton("icons/close.png", "icons/close_active.png",
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

    private void makeLogo() {
        logoImage = new ImagePanel("logo.png",
                DefaultSizes.mainMenuLogoWidth,
                DefaultSizes.mainMenuLogoHeight);
    }

    private void makeButtons() {
        playButton = new ImageButton("play", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);

        collectionButton = new ImageButton("collection", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);

        statusButton = new ImageButton("status", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);

        marketButton = new ImageButton("market", "buttons/long_pink_market_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);

        // listeners

        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().getContentPane().setVisible(false);
                GameFrame.getInstance().setContentPane(new MarketPanel());
            }
        });

        statusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().getContentPane().setVisible(false);
                GameFrame.getInstance().setContentPane(new StatusPanel());
            }
        });

        collectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().getContentPane().setVisible(false);
                GameFrame.getInstance().setContentPane(new HeroSelection());
            }
        });
    }

    private void configPanel() {
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent() {
        // LOGO
        /*logoImage.setBounds(buttonX + DefaultSizes.largeButtonWidth / 2 - DefaultSizes.mainMenuLogoWidth / 2,
                startButtonY - (int) (1.80 * buttonDis),
                DefaultSizes.mainMenuLogoWidth,
                DefaultSizes.mainMenuLogoHeight);*/
        logoImage.setBounds(DefaultSizes.gameFrameWidth / 2 - DefaultSizes.mainMenuLogoWidth / 2,
                450,
                DefaultSizes.mainMenuLogoWidth,
                DefaultSizes.mainMenuLogoHeight);
        add(logoImage);

        // ICONS
        settingsButton.setBounds(iconX, startIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(settingsButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);

        // BUTTONS
        playButton.setBounds(firstButtonX, startButtonY + 0 * buttonDisY,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);
        add(playButton);

        collectionButton.setBounds(firstButtonX, startButtonY + 1 * buttonDisY,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);
        add(collectionButton);

        statusButton.setBounds(secondButtonX, startButtonY + 0 * buttonDisY,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);
        add(statusButton);

        marketButton.setBounds(secondButtonX, startButtonY + 1 * buttonDisY,
                DefaultSizes.largeButtonWidth,
                DefaultSizes.largeButtonHeight);
        add(marketButton);
    }
}
