package hearthstone.client.gui.game;

import hearthstone.client.gui.controls.icons.*;
import hearthstone.client.gui.game.onlinegames.GamesPanel;
import hearthstone.client.network.ClientMapper;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.panels.ImagePanel;
import hearthstone.client.gui.game.collection.HeroSelection;
import hearthstone.client.gui.game.play.PlaySelectionPanel;
import hearthstone.client.gui.game.waitingpanels.LoadingPanel;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel {
    private ImageButton settingsButton, logoutButton, minimizeButton, closeButton, gamesButton;
    private ImageButton playButton, collectionButton, marketButton, statusButton, rankingButton;
    private ImagePanel logoImage;

    private static BufferedImage backgroundImage;

    private final int leftIconX = 20;
    private final int rightIconX = GUIConfigs.gameFrameWidth - 20 - GUIConfigs.iconWidth;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startButtonY = 260;
    private final int halfButtonDisX = 130;
    private final int firstButtonX = GUIConfigs.gameFrameWidth / 2 - halfButtonDisX
            - GUIConfigs.largeButtonWidth;
    private final int secondButtonX = GUIConfigs.gameFrameWidth / 2 + halfButtonDisX;
    private final int buttonDisY = 120;

    private final int rankingButtonY = GUIConfigs.gameFrameHeight / 2 + 80;

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

        gamesButton = new GameIcon("icons/games.png", "icons/games_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        gamesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new LoadingPanel());
                ClientMapper.gamesListRequest();
            }
        });
    }

    private void makeLogo() {
        logoImage = new ImagePanel("logo.png",
                GUIConfigs.mainMenuLogoWidth,
                GUIConfigs.mainMenuLogoHeight);
    }

    private void makeButtons() {
        playButton = new ImageButton("Play", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        collectionButton = new ImageButton("Collection", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        statusButton = new ImageButton("Status", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        marketButton = new ImageButton("Market", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        rankingButton = new ImageButton("Ranking", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        // listeners

        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new LoadingPanel());
                ClientMapper.marketCardsRequest();
                ClientMapper.startUpdateMarketCards();
            }
        });

        rankingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new LoadingPanel());
                ClientMapper.rankingRequest();
                ClientMapper.startUpdateRanking();
            }
        });

        statusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new LoadingPanel());
                ClientMapper.statusDecksRequest();
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

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), HeroSelection.makeInstance());
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
                            PlaySelectionPanel.makeInstance());
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
        logoImage.setBounds(GUIConfigs.gameFrameWidth / 2 - GUIConfigs.mainMenuLogoWidth / 2,
                460,
                GUIConfigs.mainMenuLogoWidth,
                GUIConfigs.mainMenuLogoHeight);
        add(logoImage);

        // ICONS
        settingsButton.setBounds(leftIconX, startIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(settingsButton);

        logoutButton.setBounds(leftIconX, startIconY + iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(leftIconX, endIconY - iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(leftIconX, endIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(closeButton);

        gamesButton.setBounds(rightIconX, startIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(gamesButton);

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

        rankingButton.setBounds(GUIConfigs.gameFrameWidth / 2 - GUIConfigs.largeButtonWidth / 2,
                rankingButtonY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(rankingButton);
    }
}
