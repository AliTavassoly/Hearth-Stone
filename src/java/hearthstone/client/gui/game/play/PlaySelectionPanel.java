package hearthstone.client.gui.game.play;

import hearthstone.client.gui.game.waitingpanels.CancelOperation;
import hearthstone.client.gui.game.waitingpanels.LoadingPanel;
import hearthstone.client.gui.game.waitingpanels.WaitForOpponentPanel;
import hearthstone.client.network.ClientMapper;
import hearthstone.client.network.HSClient;
import hearthstone.client.gui.BaseFrame;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.HearthStoneException;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PlaySelectionPanel extends JPanel {
    private static PlaySelectionPanel instance;

    private ImageButton backButton, logoutButton, minimizeButton, closeButton;
    private ImageButton playOnline, practicePlay, soloPlay, deckReaderPlay, tavernBrawlPlay;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int buttonDisY = 100;
    private final int startButtonY = GUIConfigs.gameFrameHeight / 2 - buttonDisY * 4 / 2 - GUIConfigs.largeButtonHeight / 2;
    private final int buttonX = GUIConfigs.gameFrameWidth - 200;

    private PlaySelectionPanel() {
        configPanel();

        makeIcons();

        makeButtons();

        layoutComponent();
    }

    public static PlaySelectionPanel makeInstance() {
        return instance = new PlaySelectionPanel();
    }

    public static PlaySelectionPanel getInstance() {
        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/play_selection_background.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
    }

    private void makeButtons() {
        playOnline = new ImageButton("Play Online", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        practicePlay = new ImageButton("Practice", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        soloPlay = new ImageButton("Solo play", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        deckReaderPlay = new ImageButton("Deck Reader", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        tavernBrawlPlay = new ImageButton("Tavern Brawl", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        playOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HSClient.currentAccount.readyForPlay();

                    ClientMapper.onlineGameRequest();

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new WaitForOpponentPanel(new CancelOperation() {
                        @Override
                        public void operation() {
                            ClientMapper.onlineGameCancelRequest();
                            GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
                        }
                    }));
                } catch (HearthStoneException hse) {
                    BaseFrame.error(hse.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        practicePlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HSClient.currentAccount.readyForPlay();

                    ClientMapper.practiceGameRequest();

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new LoadingPanel());
                } catch (HearthStoneException hse) {
                    BaseFrame.error(hse.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        soloPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HSClient.currentAccount.readyForPlay();

                    ClientMapper.soloGameRequest();

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new LoadingPanel());
                } catch (HearthStoneException hse) {
                    BaseFrame.error(hse.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        deckReaderPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    ClientMapper.deckReaderGameRequest();

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new WaitForOpponentPanel(new CancelOperation() {
                        @Override
                        public void operation() {
                            ClientMapper.deckReaderGameCancelRequest();
                            GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
                        }
                    }));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tavernBrawlPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HSClient.currentAccount.readyForPlay();

                    ClientMapper.tavernBrawlGameRequest();

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new WaitForOpponentPanel(new CancelOperation() {
                        @Override
                        public void operation() {
                            ClientMapper.tavernBrawlGameCancelRequest();
                            GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
                        }
                    }));
                } catch (HearthStoneException hse) {
                    BaseFrame.error(hse.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
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
        // ICONS
        backButton.setBounds(iconX, startIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(backButton);

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
        playOnline.setBounds(buttonX,
                startButtonY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(playOnline);

        practicePlay.setBounds(buttonX,
                startButtonY + buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(practicePlay);

        soloPlay.setBounds(buttonX,
                startButtonY + 2 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(soloPlay);

        deckReaderPlay.setBounds(buttonX,
                startButtonY + 3 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(deckReaderPlay);

        tavernBrawlPlay.setBounds(buttonX,
                startButtonY + 4 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(tavernBrawlPlay);
    }
}
