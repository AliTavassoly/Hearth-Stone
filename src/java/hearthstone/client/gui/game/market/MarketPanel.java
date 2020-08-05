package hearthstone.client.gui.game.market;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.client.gui.BaseFrame;
import hearthstone.client.configs.GUIConfigs;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.dialogs.SureDialog;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.panels.CardsPanel;
import hearthstone.client.gui.controls.panels.ImagePanel;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.gui.util.CustomScrollBarUI;
import hearthstone.models.card.Card;
import hearthstone.util.FontType;
import hearthstone.util.HearthStoneException;
import hearthstone.util.getresource.ImageResource;

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
    private JLabel gemLabel;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startListY = (GUIConfigs.gameFrameHeight - GUIConfigs.marketListHeight) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + GUIConfigs.marketListWidth;

    private final int disInfo = 70;
    private final int startInfoX = endListX + disInfo;
    private final int startInfoY = startListY;
    private final int infoWidth = (GUIConfigs.gameFrameWidth - endListX) - (2 * disInfo);

    private final int disChoose = 20;
    private final int startChooseX = endListX + (GUIConfigs.gameFrameWidth - endListX) / 2
            - GUIConfigs.medButtonWidth / 2;
    private final int startChooseY = GUIConfigs.gameFrameHeight / 2 + (GUIConfigs.marketInfoHeight + startInfoY) / 2
            - GUIConfigs.medButtonHeight - disChoose / 2;

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
        BufferedImage image = null;
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/market_background.png");

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

    private void makeChoosePanel() {
        sellButton = new ImageButton("SELL", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                20, 0, GUIConfigs.medButtonWidth, GUIConfigs.medButtonHeight);
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buyScroll.setVisible(false);
                try {
                    hearthstone.util.Logger.saveLog("Click_button",
                            "sell panel_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sellScroll.setVisible(true);
            }
        });

        buyButton = new ImageButton("BUY", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                20, 0, GUIConfigs.medButtonWidth, GUIConfigs.medButtonHeight);
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sellScroll.setVisible(false);
                try {
                    hearthstone.util.Logger.saveLog("Button_click",
                            "buy panel_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                buyScroll.setVisible(true);
            }
        });
    }

    private void makeInformationPanel() {
        informationPanel = new JPanel();
        gemLabel = new JLabel();
        gemLabel.setText(String.valueOf(HearthStone.currentAccount.getGem()));
        gemLabel.setForeground(Color.WHITE);
        gemLabel.setFont(GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, 62));

        ImagePanel imagePanel = new ImagePanel("gem.png",
                GUIConfigs.bigGemButtonWidth,
                GUIConfigs.bigGemButtonHeight);

        informationPanel.setBackground(new Color(0, 0, 0, 0));
        informationPanel.add(gemLabel, BorderLayout.WEST);
        informationPanel.add(imagePanel, BorderLayout.EAST);
        informationPanel.setOpaque(false);
    }

    private void makeBuyPanel() {
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Card card : HearthStone.market.getCards()) {
            cards.add(card);
            panels.add(getBuyPanel(card));
        }

        buyPanel = new CardsPanel(cards, panels,
                2, GUIConfigs.medCardWidth, GUIConfigs.medCardHeight);
        buyScroll = new JScrollPane(buyPanel);
        buyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        buyScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        buyScroll.setOpaque(false);
        buyScroll.getViewport().setOpaque(true);
        buyScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        buyScroll.setBorder(null);
    }

    private void makeSellPanel() {
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Card card : HearthStone.currentAccount.getCollection().getCards()) {
            cards.add(card);
            panels.add(getSellPanel(card));
        }

        sellPanel = new CardsPanel(cards, panels,
                2, GUIConfigs.medCardWidth, GUIConfigs.medCardHeight);
        sellScroll = new JScrollPane(sellPanel);
        sellScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sellScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sellScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        sellScroll.setOpaque(false);
        sellScroll.getViewport().setOpaque(true);
        sellScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        sellScroll.setOpaque(false);
        sellScroll.setBorder(null);
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
        buyScroll.setBounds(startListX, startListY,
                GUIConfigs.marketListWidth,
                GUIConfigs.marketListHeight);
        add(buyScroll);

        sellScroll.setBounds(startListX, startListY,
                GUIConfigs.marketListWidth,
                GUIConfigs.marketListHeight);
        sellScroll.setVisible(false);
        add(sellScroll);

        // CHOOSE
        buyButton.setBounds(startChooseX, startChooseY,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);
        add(buyButton);

        sellButton.setBounds(startChooseX, startChooseY + GUIConfigs.medButtonHeight + disChoose,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);
        add(sellButton);

        // INFORMATION
        informationPanel.setBounds(startInfoX, startInfoY,
                infoWidth,
                (int) informationPanel.getPreferredSize().getHeight());
        add(informationPanel);
    }

    private JPanel getSellPanel(Card card) {
        JPanel panel = new JPanel();
        ImageButton button = new ImageButton("SELL", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                20, 0,
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                            "Are you sure you want to sell " + card.getName()
                                    + " for " + card.getSellPrice() + "gems" + " ?",
                            GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);

                    hearthstone.util.Logger.saveLog("Click_button",
                            "sell_button");
                    boolean sure = sureDialog.getValue();
                    if (sure) {
                        Mapper.sellCard(card.getId());

                        buyPanel.addCard(card, getBuyPanel(card));
                        sellPanel.removeCard(card);
                        gemLabel.setText(String.valueOf(HearthStone.currentAccount.getGem()));

                        Mapper.saveDataBase();
                    }
                } catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage() +
                                        "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception ignored) { }
                    BaseFrame.error(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

    private JPanel getBuyPanel(Card card) {
        JPanel panel = new JPanel();
        ImageButton button = new ImageButton("BUY", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                20, 0,
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                            "Are you sure you want to buy " + card.getName()
                                    + " for  " + card.getBuyPrice() + "  gems " + " ?",
                            GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);

                    hearthstone.util.Logger.saveLog("Click_button",
                            "buy_button");

                    boolean sure = sureDialog.getValue();
                    if (sure) {
                        Mapper.buyCard(card.getId());

                        buyPanel.removeCard(card);
                        gemLabel.setText(String.valueOf(HearthStone.currentAccount.getGem()));

                        Mapper.saveDataBase();
                    }
                } catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage()
                                        + "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) {
                    }
                    BaseFrame.error(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

    class Price extends JPanel {
        public Price(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(Color.WHITE);
            label.setFont(GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, 20));

            ImagePanel gemImage = new ImagePanel("gem.png",
                    GUIConfigs.smallGemButtonWidth,
                    GUIConfigs.smallGemButtonHeight);

            add(label, BorderLayout.WEST);
            add(gemImage, BorderLayout.EAST);
            setOpaque(false);
        }
    }
}
