package hearthstone.gui.game.status;

import hearthstone.HearthStone;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.deck.DecksPanel;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.Deck;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StatusPanel extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private DecksPanel deckPanel;
    private JScrollPane deckCardScroll;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startListY = (SizeConfigs.gameFrameHeight - SizeConfigs.statusListHeight) / 2;
    private final int startListX = 100;

    public StatusPanel() {
        configPanel();

        makeIcons();

        makeDeckList();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage("/images/status_background.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
    }

    private void makeDeckList() {
        ArrayList<Deck> decks = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Deck deck : HearthStone.currentAccount.getBestDecks(10)) {
            decks.add(deck);
            panels.add(null);
        }

        deckPanel = new DecksPanel(decks, panels,
                SizeConfigs.deckWidth, SizeConfigs.deckHeight);
        deckCardScroll = new JScrollPane(deckPanel);
        deckCardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        deckCardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        deckCardScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        deckCardScroll.setOpaque(false);
        deckCardScroll.getViewport().setOpaque(true);
        deckCardScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        deckCardScroll.setBorder(null);
    }

    private void configPanel() {
        setSize(new Dimension(SizeConfigs.gameFrameWidth, SizeConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(backButton);

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

        // LISTS
        deckCardScroll.setBounds(startListX, startListY,
                SizeConfigs.statusListWidth,
                SizeConfigs.statusListHeight);
        add(deckCardScroll);
    }
}
