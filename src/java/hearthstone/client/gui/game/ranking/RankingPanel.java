package hearthstone.client.gui.game.ranking;

import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.panels.AccountInfosPanel;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.gui.util.CustomScrollBarUI;
import hearthstone.models.AccountInfo;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RankingPanel extends JPanel {
    private static RankingPanel instance;

    private ImageButton backButton, minimizeButton, closeButton, logoutButton;

    private AccountInfosPanel topRanksPanel;
    private JScrollPane topRanksScroll;

    private AccountInfosPanel nearRanksPanel;
    private JScrollPane nearRanksScroll;

    public static ArrayList<AccountInfo> topRanks = new ArrayList<>();
    public static ArrayList<AccountInfo> nearRanks = new ArrayList<>();

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;

    private final int startTopRankListY = (GUIConfigs.gameFrameHeight - GUIConfigs.rankingListHeight) / 2;
    private final int startTopRankListX = 100;

    private final int startMyRankListY = (GUIConfigs.gameFrameHeight - GUIConfigs.rankingListHeight) / 2;
    private final int startMtRankListX = GUIConfigs.gameFrameWidth - GUIConfigs.rankingListWidth - 20;

    private RankingPanel() {
        configPanel();

        makeIcons();

        makeTopRanksList();

        makeMyRanksList();

        layoutComponent();
    }

    public static RankingPanel makeInstance() {
        return instance = new RankingPanel();
    }

    public static RankingPanel getInstance() {
        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage("/images/ranking_background.jpg");
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

    private void makeTopRanksList() {
        ArrayList<AccountInfo> accounts = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (AccountInfo accountInfo : topRanks) {
            accounts.add(accountInfo);
            panels.add(null);
        }

        topRanksPanel = new AccountInfosPanel(accounts, panels,
                GUIConfigs.accountInfoWidth, GUIConfigs.accountInfoHeight);
        topRanksScroll = new JScrollPane(topRanksPanel);
        topRanksScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        topRanksScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        topRanksScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        topRanksScroll.setOpaque(false);
        topRanksScroll.getViewport().setOpaque(true);
        topRanksScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        topRanksScroll.setBorder(null);
    }

    private void makeMyRanksList() {
        ArrayList<AccountInfo> accounts = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (AccountInfo accountInfo : nearRanks) {
            accounts.add(accountInfo);
            panels.add(null);
        }

        nearRanksPanel = new AccountInfosPanel(accounts, panels,
                GUIConfigs.accountInfoWidth, GUIConfigs.accountInfoHeight);
        nearRanksScroll = new JScrollPane(nearRanksPanel);
        nearRanksScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        nearRanksScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        nearRanksScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        nearRanksScroll.setOpaque(false);
        nearRanksScroll.getViewport().setOpaque(true);
        nearRanksScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        nearRanksScroll.setBorder(null);
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
        topRanksScroll.setBounds(startTopRankListX, startTopRankListY,
                GUIConfigs.rankingListWidth,
                GUIConfigs.rankingListHeight);
        add(topRanksScroll);

        nearRanksScroll.setBounds(startMtRankListX, startMyRankListY,
                GUIConfigs.rankingListWidth,
                GUIConfigs.rankingListHeight);
        add(nearRanksScroll);
    }

    public void updatePanel(ArrayList<AccountInfo> topRanks, ArrayList<AccountInfo> nearRanks) {
        RankingPanel.topRanks = topRanks;
        RankingPanel.nearRanks = nearRanks;

        makeMyRanksList();
        makeTopRanksList();

        revalidate();
        repaint();
    }
}
