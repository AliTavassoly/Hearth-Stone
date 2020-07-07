package hearthstone.gui.game.market;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.controls.card.CardsPanel;
import hearthstone.gui.controls.dialogs.SureDialog;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.card.Card;
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
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startListY = (SizeConfigs.gameFrameHeight - SizeConfigs.marketListHeight) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + SizeConfigs.marketListWidth;

    private final int disInfo = 70;
    private final int startInfoX = endListX + disInfo;
    private final int startInfoY = startListY;
    private final int infoWidth = (SizeConfigs.gameFrameWidth - endListX) - (2 * disInfo);

    private final int disChoose = 20;
    private final int startChooseX = endListX + (SizeConfigs.gameFrameWidth - endListX) / 2
            - SizeConfigs.medButtonWidth / 2;
    private final int startChooseY = SizeConfigs.gameFrameHeight / 2 + (SizeConfigs.marketInfoHeight + startInfoY) / 2
            - SizeConfigs.medButtonHeight - disChoose / 2;

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
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
    }

    private void makeChoosePanel() {
        sellButton = new ImageButton("SELL", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                20, 0, SizeConfigs.medButtonWidth, SizeConfigs.medButtonHeight);
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
                20, 0, SizeConfigs.medButtonWidth, SizeConfigs.medButtonHeight);
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
                SizeConfigs.bigGemButtonWidth,
                SizeConfigs.bigGemButtonHeight);

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
                2, SizeConfigs.medCardWidth, SizeConfigs.medCardHeight);
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
                2, SizeConfigs.medCardWidth, SizeConfigs.medCardHeight);
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
        buyScroll.setBounds(startListX, startListY,
                SizeConfigs.marketListWidth,
                SizeConfigs.marketListHeight);
        add(buyScroll);

        sellScroll.setBounds(startListX, startListY,
                SizeConfigs.marketListWidth,
                SizeConfigs.marketListHeight);
        sellScroll.setVisible(false);
        add(sellScroll);

        // CHOOSE
        buyButton.setBounds(startChooseX, startChooseY,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);
        add(buyButton);

        sellButton.setBounds(startChooseX, startChooseY + SizeConfigs.medButtonHeight + disChoose,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);
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
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                            "Are you sure you want to sell " + card.getName()
                                    + " for " + card.getSellPrice() + "gems" + " ?",
                            SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);

                    hearthstone.util.Logger.saveLog("Click_button",
                            "sell_button");
                    boolean sure = sureDialog.getValue();
                    if (sure) {
                        Mapper.getInstance().sellCard(card);

                        buyPanel.addCard(card, getBuyPanel(card));
                        sellPanel.removeCard(card);
                        gemLabel.setText(String.valueOf(HearthStone.currentAccount.getGem()));

                        Mapper.getInstance().saveDataBase();
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
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                            "Are you sure you want to buy " + card.getName()
                                    + " for  " + card.getBuyPrice() + "  gems " + " ?",
                            SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);

                    hearthstone.util.Logger.saveLog("Click_button",
                            "buy_button");

                    boolean sure = sureDialog.getValue();
                    if (sure) {
                        Mapper.getInstance().buyCard(card);

                        buyPanel.removeCard(card);
                        gemLabel.setText(String.valueOf(HearthStone.currentAccount.getGem()));

                        Mapper.getInstance().saveDataBase();
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
                    SizeConfigs.smallGemButtonWidth,
                    SizeConfigs.smallGemButtonHeight);

            add(label, BorderLayout.WEST);
            add(gemImage, BorderLayout.EAST);
            setOpaque(false);
        }
    }
}
