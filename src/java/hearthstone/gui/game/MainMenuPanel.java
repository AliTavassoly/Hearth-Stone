package hearthstone.gui.game;

import hearthstone.HearthStone;
import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.controls.icons.SettingIcon;
import hearthstone.gui.game.collection.HeroSelection;
import hearthstone.gui.game.market.MarketPanel;
import hearthstone.gui.game.play.PlaySelectionPanel;
import hearthstone.gui.game.status.StatusPanel;
import hearthstone.util.HearthStoneException;

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
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startButtonY = 260;
    private final int halfButtonDisX = 130;
    private final int firstButtonX = SizeConfigs.gameFrameWidth / 2 - halfButtonDisX
            - SizeConfigs.largeButtonWidth;
    private final int secondButtonX = SizeConfigs.gameFrameWidth / 2 + halfButtonDisX;
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
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/main_menu_background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(image, 0, 0, null);
    }

    private void makeIcons() {
        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        settingsButton = new SettingIcon("icons/settings.png", "icons/settings_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
    }

    private void makeLogo() {
        logoImage = new ImagePanel("logo.png",
                SizeConfigs.mainMenuLogoWidth,
                SizeConfigs.mainMenuLogoHeight);
    }

    private void makeButtons() {
        playButton = new ImageButton("play", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        collectionButton = new ImageButton("collection", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        statusButton = new ImageButton("status", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        marketButton = new ImageButton("market", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

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

                    HearthStone.currentAccount.readyForPlay();
                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                            new PlaySelectionPanel(HearthStone.currentAccount.getPlayer(), HearthStone.currentAccount.getPlayer()));
                } catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage()
                                        + "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) { }
                    BaseFrame.error(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
        logoImage.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.mainMenuLogoWidth / 2,
                450,
                SizeConfigs.mainMenuLogoWidth,
                SizeConfigs.mainMenuLogoHeight);
        add(logoImage);

        // ICONS
        settingsButton.setBounds(iconX, startIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(settingsButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(closeButton);

        // BUTTONS
        playButton.setBounds(firstButtonX, startButtonY + 0 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(playButton);

        collectionButton.setBounds(firstButtonX, startButtonY + 1 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(collectionButton);

        statusButton.setBounds(secondButtonX, startButtonY + 0 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(statusButton);

        marketButton.setBounds(secondButtonX, startButtonY + 1 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(marketButton);
    }
}
