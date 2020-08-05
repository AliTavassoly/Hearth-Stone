package hearthstone.client.gui.game;

import hearthstone.client.configs.GUIConfigs;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.icons.SettingIcon;
import hearthstone.client.gui.controls.panels.ImagePanel;
import hearthstone.client.gui.game.collection.HeroSelection;
import hearthstone.client.gui.game.market.MarketPanel;
import hearthstone.client.gui.game.play.PlaySelectionPanel;
import hearthstone.client.gui.game.status.StatusPanel;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel {
    private ImageButton settingsButton, logoutButton, minimizeButton, closeButton;
    private ImageButton playButton, collectionButton, marketButton, statusButton;
    private ImagePanel logoImage;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startButtonY = 260;
    private final int halfButtonDisX = 130;
    private final int firstButtonX = GUIConfigs.gameFrameWidth / 2 - halfButtonDisX
            - GUIConfigs.largeButtonWidth;
    private final int secondButtonX = GUIConfigs.gameFrameWidth / 2 + halfButtonDisX;
    private final int buttonDisY = 120;


    public MainMenuPanel() {
        configPanel();

        makeIcons();

        makeLogo();

        makeButtons();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/main_menu_background.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void makeIcons() {
        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        settingsButton = new SettingIcon("icons/settings.png", "icons/settings_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
    }

    private void makeLogo() {
        logoImage = new ImagePanel("logo.png",
                GUIConfigs.mainMenuLogoWidth,
                GUIConfigs.mainMenuLogoHeight);
    }

    private void makeButtons() {
        playButton = new ImageButton("play", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        collectionButton = new ImageButton("collection", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        statusButton = new ImageButton("status", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        marketButton = new ImageButton("market", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        // listeners

        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    hearthstone.util.Logger.saveLog("Click_button",
                            "market_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MarketPanel());
            }
        });

        statusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    hearthstone.util.Logger.saveLog("Click_button",
                            "status_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new StatusPanel());
            }
        });

        collectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    hearthstone.util.Logger.saveLog("Click_button",
                            "collection_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new HeroSelection());
            }
        });

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    try {
                        hearthstone.util.Logger.saveLog("Click_button",
                                "play_button");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                            new PlaySelectionPanel());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void configPanel() {
        setSize(new Dimension(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent() {
        // LOGO
        /*logoImage.setBounds(buttonX + DefaultSizes.largeButtonWidth / 2 - DefaultSizes.mainMenuLogoWidth / 2,
                startButtonY - (int) (1.80 * buttonDis),
                DefaultSizes.mainMenuLogoWidth,
                DefaultSizes.mainMenuLogoHeight);*/
        logoImage.setBounds(GUIConfigs.gameFrameWidth / 2 - GUIConfigs.mainMenuLogoWidth / 2,
                450,
                GUIConfigs.mainMenuLogoWidth,
                GUIConfigs.mainMenuLogoHeight);
        add(logoImage);

        // ICONS
        settingsButton.setBounds(iconX, startIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(settingsButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(closeButton);

        // BUTTONS
        playButton.setBounds(firstButtonX, startButtonY + 0 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(playButton);

        collectionButton.setBounds(firstButtonX, startButtonY + 1 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(collectionButton);

        statusButton.setBounds(secondButtonX, startButtonY + 0 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(statusButton);

        marketButton.setBounds(secondButtonX, startButtonY + 1 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(marketButton);
    }
}
