package hearthstone.gui.game.market;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.BaseFrame;
import hearthstone.gui.controls.ErrorDialog;
import hearthstone.gui.controls.SureDialog;
import hearthstone.gui.controls.card.CardsPanel;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.card.Card;
import hearthstone.util.HearthStoneException;

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
    private JLabel gemLabel;

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
            System.out.println(e.getMessage());
        }
        g.drawImage(image, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
    }

    private void makeChoosePanel(){
        sellButton = new ImageButton("SELL", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                20, 0, DefaultSizes.medButtonWidth, DefaultSizes.medButtonHeight);
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buyScroll.setVisible(false);
                try {
                    hearthstone.util.Logger.saveLog("panel chang",
                            "panel changed to sell panel");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
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
                try {
                    hearthstone.util.Logger.saveLog("panel chang",
                            "panel changed to buy panel");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                buyScroll.setVisible(true);
            }
        });
    }

    private void makeInformationPanel(){
        informationPanel = new JPanel();
        gemLabel = new JLabel();
        gemLabel.setText(String.valueOf(HearthStone.currentAccount.getGem()));
        gemLabel.setForeground(Color.WHITE);
        gemLabel.setFont(GameFrame.getInstance().getCustomFont(0, 62));

        ImagePanel imagePanel = new ImagePanel("gem.png",
                DefaultSizes.bigGemButtonWidth,
                DefaultSizes.bigGemButtonHeight);

        informationPanel.setBackground(new Color(0, 0, 0, 0));
        informationPanel.add(gemLabel, BorderLayout.WEST);
        informationPanel.add(imagePanel, BorderLayout.EAST);
        informationPanel.setOpaque(false);
    }

    private void makeBuyPanel() {
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Card card : HearthStone.market.getCards()){
            cards.add(card);
            panels.add(getBuyPanel(card));
        }

        buyPanel = new CardsPanel(cards, panels,
                2, DefaultSizes.medCardWidth, DefaultSizes.medCardHeight);
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

        for(Card card : HearthStone.currentAccount.getCollection().getCards()){
            cards.add(card);
            panels.add(getSellPanel(card));
        }

        sellPanel = new CardsPanel(cards, panels,
                2, DefaultSizes.medCardWidth, DefaultSizes.medCardHeight);
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
                try {
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                            "Are you sure you want to sell " + card.getName()
                                    + " for " + card.getSellPrice() + "gems" + " ?",
                            DefaultSizes.dialogWidth, DefaultSizes.dialogHeight);
                    boolean sure = sureDialog.getValue();
                    if (sure) {
                        HearthStone.currentAccount.sellCards(card, 1);
                        buyPanel.addCard(card, getBuyPanel(card));
                        sellPanel.removeCard(card);
                        HearthStone.market.addCard(card.copy(), 1);
                        gemLabel.setText(String.valueOf(HearthStone.currentAccount.getGem()));
                        hearthstone.util.Logger.saveLog("sell",
                                "in market, sold " + 1 + " of " +
                                        card.getName() + "!");
                        DataBase.save();
                    }
                } catch (HearthStoneException e){
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage() +
                                        "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) { }
                    BaseFrame.error(e.getMessage());
                    System.out.println(e.getMessage());
                } catch (Exception ex){
                    System.out.println(ex.getMessage());
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

    private JPanel getBuyPanel(Card card){
        JPanel panel = new JPanel();
        ImageButton button = new ImageButton("BUY", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                20, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(),
                            "Are you sure you want to buy " + card.getName()
                                    + " for  " + card.getBuyPrice() + "  gems " + " ?",
                            DefaultSizes.dialogWidth, DefaultSizes.dialogHeight);
                    boolean sure = sureDialog.getValue();
                    if(sure) {
                        HearthStone.currentAccount.buyCards(card, 1);
                        buyPanel.removeCard(card);
                        HearthStone.market.removeCard(card, 1);
                        gemLabel.setText(String.valueOf(HearthStone.currentAccount.getGem()));
                        hearthstone.util.Logger.saveLog("buy",
                                "in market, bought " + 1 + " of " +
                                        card.getName() + "!");
                        DataBase.save();
                    }
                } catch (HearthStoneException e){
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage()
                                        + "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) { }
                    System.out.println(e.getMessage());
                    BaseFrame.error(e.getMessage());
                } catch (Exception ex){
                    System.out.println(ex.getMessage());
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
