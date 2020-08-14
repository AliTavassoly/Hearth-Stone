package hearthstone.client.gui.game.onlinegames;

import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.panels.GameInfosPanel;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.gui.util.CustomScrollBarUI;
import hearthstone.models.GameInfo;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamesPanel extends JPanel {
    private static GamesPanel instance;

    private ImageButton backButton, minimizeButton, closeButton, logoutButton;

    private GameInfosPanel gamesPanel;
    private JScrollPane gamesScroll;

    public static ArrayList<GameInfo> games = new ArrayList<>();

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;

    private final int startListY = (GUIConfigs.gameFrameHeight - GUIConfigs.rankingListHeight) / 2;
    private final int startListX = 100;

    private GamesPanel() {
        configPanel();

        makeIcons();

        makeGamesList();

        layoutComponent();
    }

    public static GamesPanel makeInstance() {
        return instance = new GamesPanel();
    }

    public static GamesPanel getInstance() {
        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage("/images/games_background.jpg");
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

    private void makeGamesList() {
        ArrayList<GameInfo> games = new ArrayList<>(GamesPanel.games);

        gamesPanel = new GameInfosPanel(games,
                GUIConfigs.gameInfoWidth, GUIConfigs.gameInfoHeight);
        gamesScroll = new JScrollPane(gamesPanel);
        gamesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gamesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gamesScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        gamesScroll.setOpaque(false);
        gamesScroll.getViewport().setOpaque(true);
        gamesScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        gamesScroll.setBorder(null);
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

        // LISTS
        gamesScroll.setBounds(startListX, startListY,
                GUIConfigs.gamesListWidth,
                GUIConfigs.gamesListHeight);
        add(gamesScroll);
    }
}
