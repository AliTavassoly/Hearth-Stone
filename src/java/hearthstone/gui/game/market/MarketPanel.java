package hearthstone.gui.game.market;

import hearthstone.HearthStone;
import hearthstone.gui.controls.card.CardsPanel;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.logic.models.cards.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MarketPanel extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private CardsPanel buyPanel, sellPanel;
    private JScrollPane buyScroll, sellScroll;
    private ImageButton sellButton, buyButton;
    private JPanel informationPanel;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startListY = (DefaultSizes.gameFrameHeight - DefaultSizes.marketListHeight) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + DefaultSizes.marketListWidth;

    private final int disInfo = 70;
    private final int startInfoX = endListX + disInfo;
    private final int startInfoY = startListY;
    private final int infoWidth = (DefaultSizes.gameFrameWidth - endListX) - (2 * disInfo);

    private final int disChoose = 20;
    private final int startChooseX = endListX + (DefaultSizes.gameFrameWidth - endListX) / 2
            - DefaultSizes.medButtonWidth / 2;
    private final int startChooseY = DefaultSizes.gameFrameHeight / 2 + (DefaultSizes.marketInfoHeight + startInfoY) / 2
            - DefaultSizes.medButtonHeight - disChoose / 2;

    public MarketPanel() {
        configPanel();

        makeIcons();

        makeChoosePanel();

        makeInformationPanel();

        makeBuyPanel();
        makeSellPanel();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g = (Graphics2D)(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/market_background.png"));

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

    private void makeChoosePanel(){
        sellButton = new ImageButton("SELL", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                20, 0, DefaultSizes.medButtonWidth, DefaultSizes.medButtonHeight);
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buyScroll.setVisible(false);
                sellScroll.setVisible(true);
            }
        });

        buyButton = new ImageButton("BUY", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                20, 0, DefaultSizes.medButtonWidth, DefaultSizes.medButtonHeight);
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sellScroll.setVisible(false);
                buyScroll.setVisible(true);
            }
        });
    }

    private void makeInformationPanel(){
        informationPanel = new JPanel();
        JLabel label = new JLabel("50"); // number of account gems
        label.setForeground(Color.WHITE);
        label.setFont(GameFrame.getInstance().getCustomFont(0, 62));

        ImagePanel imagePanel = new ImagePanel("gem.png",
                DefaultSizes.bigGemButtonWidth,
                DefaultSizes.bigGemButtonHeight);

        informationPanel.setBackground(new Color(0, 0, 0, 0));
        informationPanel.add(label, BorderLayout.WEST);
        informationPanel.add(imagePanel, BorderLayout.EAST);
    }

    private void makeBuyPanel() {
        ArrayList<Card> testCard = new ArrayList<>();
        ArrayList<JPanel> testPanel = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Card card = HearthStone.baseCards.get(6).copy();
            testCard.add(card);
            testPanel.add(getBuyPanel(card));
        }

        buyPanel = new CardsPanel(testCard, testPanel,
                2, DefaultSizes.medCardWidth, DefaultSizes.medCardHeight);
        buyScroll = new JScrollPane(buyPanel);
        buyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        buyScroll.getHorizontalScrollBar().setUI(new hearthstone.util.CustomScrollBarUI());
        buyScroll.setOpaque(false);
        buyScroll.getViewport().setOpaque(true);
        buyScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        buyScroll.setBorder(null);
    }

    private void makeSellPanel() {
        ArrayList<Card> testCard = new ArrayList<>();
        ArrayList<JPanel> testPanel = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Card card = HearthStone.baseCards.get(6).copy();
            testCard.add(card);
            testPanel.add(getSellPanel(card));
        }

        sellPanel = new CardsPanel(testCard, testPanel,
                2, DefaultSizes.medCardWidth, DefaultSizes.medCardHeight);
        sellScroll = new JScrollPane(sellPanel);
        sellScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sellScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sellScroll.getHorizontalScrollBar().setUI(new hearthstone.util.CustomScrollBarUI());
        sellScroll.setOpaque(false);
        sellScroll.getViewport().setOpaque(true);
        sellScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        sellScroll.setOpaque(false);
        sellScroll.setBorder(null);
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
        buyScroll.setBounds(startListX, startListY,
                DefaultSizes.marketListWidth,
                DefaultSizes.marketListHeight);
        add(buyScroll);

        sellScroll.setBounds(startListX, startListY,
                DefaultSizes.marketListWidth,
                DefaultSizes.marketListHeight);
        sellScroll.setVisible(false);
        add(sellScroll);

        // CHOOSE
        buyButton.setBounds(startChooseX, startChooseY,
                DefaultSizes.medButtonWidth,
                DefaultSizes.medButtonHeight);
        add(buyButton);

        sellButton.setBounds(startChooseX, startChooseY + DefaultSizes.medButtonHeight + disChoose,
                DefaultSizes.medButtonWidth,
                DefaultSizes.medButtonHeight);
        add(sellButton);

        // INFORMATION
        informationPanel.setBounds(startInfoX, startInfoY,
                infoWidth,
                (int)informationPanel.getPreferredSize().getHeight());
        add(informationPanel);
    }

    private JPanel getSellPanel(Card card){
        JPanel panel = new JPanel();
        ImageButton button = new ImageButton("SELL", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                20, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // SELL CARD
            }
        });
        Price price = new Price(String.valueOf(card.getBuyPrice()));

        panel.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridy = 0;
        grid.gridx = 0;
        panel.add(price, grid);

        grid.gridy = 1;
        grid.gridx = 0;
        panel.add(button, grid);

        panel.setOpaque(false);
        return panel;
    }

    private JPanel getBuyPanel(Card card){
        JPanel panel = new JPanel();
        ImageButton button = new ImageButton("BUY", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                20, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // BUY CARD
                buyPanel.removeCard(card);
            }
        });
        Price price = new Price(String.valueOf(card.getBuyPrice()));

        panel.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridy = 0;
        grid.gridx = 0;
        panel.add(price, grid);

        grid.gridy = 1;
        grid.gridx = 0;
        panel.add(button, grid);

        panel.setOpaque(false);
        return panel;
    }

    class Price extends JPanel{
        public Price(String text){
            JLabel label = new JLabel(text);
            label.setForeground(Color.WHITE);
            label.setFont(GameFrame.getInstance().getCustomFont(0, 20));

            ImagePanel gemImage = new ImagePanel("gem.png",
                    DefaultSizes.smallGemButtonWidth,
                    DefaultSizes.smallGemButtonHeight);

            add(label, BorderLayout.WEST);
            add(gemImage, BorderLayout.EAST);
            setOpaque(false);
        }
    }
}
