package hearthstone.gui.game.status;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.deck.DecksPanel;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.util.CustomScrollBarUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StatusPanel extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private DecksPanel deckPanel;
    private JScrollPane deckCardScroll;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startListY = (DefaultSizes.gameFrameHeight - DefaultSizes.statusListHeight) / 2;
    private final int startListX = 100;

    public StatusPanel() {
        configPanel();

        makeIcons();

        makeDeckList();

        //layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/status_background.png"));
        } catch (Exception e) {
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new ImageButton("icons/back.png", "icons/back_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        logoutButton = new ImageButton("icons/logout.png", "icons/logout_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new ImageButton("icons/minimize.png", "icons/minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        closeButton = new ImageButton("icons/close.png", "icons/close_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
            }
        });

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

    private void makeDeckList() {
        /*ArrayList<Deck> testDeck = new ArrayList<>();
        ArrayList<JPanel> testPanel = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Deck deck = new Deck("ali");
            testDeck.add(deck);
            testPanel.add(null);
        }

        deckPanel = new DecksPanel(testDeck, testPanel,
                DefaultSizes.deckWidth, DefaultSizes.deckHeight);
        deckCardScroll = new JScrollPane(deckPanel);
        deckCardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        deckCardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        deckCardScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        deckCardScroll.setOpaque(false);
        deckCardScroll.getViewport().setOpaque(true);
        deckCardScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        deckCardScroll.setBorder(null);*/
    }

    private void configPanel() {
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(backButton);

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

        // LISTS
        deckCardScroll.setBounds(startListX, startListY,
                DefaultSizes.statusListWidth,
                DefaultSizes.statusListHeight);
        add(deckCardScroll);
    }
}
