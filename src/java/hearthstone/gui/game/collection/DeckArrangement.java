package hearthstone.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.controls.card.CardsPanel;
import hearthstone.gui.controls.hero.HeroButton;
import hearthstone.gui.game.GameFrame;
import hearthstone.logic.models.cards.Card;
import hearthstone.logic.models.heroes.Hero;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DeckArrangement extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private CardsPanel allCardsPanel, deckCardsPanel;
    private JScrollPane allCardsScroll, deckCardsScroll;
    private HeroButton heroButton;
    private Hero hero;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;

    private final int listDis = 70;
    private final int startListY = (DefaultSizes.gameFrameHeight - 2 * DefaultSizes.arrangementListHeight - listDis) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + DefaultSizes.arrangementListWidth;

    private final int allTitleY = 100;
    private final int deckTitleY = 100;

    private final int startHeroX = (DefaultSizes.gameFrameWidth + endListX) / 2 - DefaultSizes.bigHeroWidth / 2;
    private final int startHeroY = startListY;

    public DeckArrangement(Hero hero) {
        this.hero = hero;

        configPanel();

        makeIcons();

        makeAllCardsPanel();

        makeDeckCardsPanel();

        makeHeroButton();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g = (Graphics2D)(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/hero_selection_background.png"));
        } catch (Exception e) {
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void configPanel() {
        setLayout(null);
        setVisible(true);
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
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckSelection(hero));
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

    private void makeAllCardsPanel() {
        ArrayList<Card> testCard = new ArrayList<>();
        ArrayList<JPanel> testPanel = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Card card = HearthStone.baseCards.get(6).copy();
            testCard.add(card);
            testPanel.add(getAllCardsPanel(card));
        }

        allCardsPanel = new CardsPanel(testCard, testPanel,
                1, DefaultSizes.medCardWidth, DefaultSizes.medCardHeight);
        allCardsScroll = new JScrollPane(allCardsPanel);
        allCardsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        allCardsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        allCardsScroll.getHorizontalScrollBar().setUI(new hearthstone.util.CustomScrollBarUI());
        allCardsScroll.setOpaque(false);
        allCardsScroll.getViewport().setOpaque(true);
        allCardsScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        allCardsScroll.setBorder(null);
    }

    private void makeDeckCardsPanel() {
        ArrayList<Card> testCard = new ArrayList<>();
        ArrayList<JPanel> testPanel = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Card card = HearthStone.baseCards.get(6).copy();
            testCard.add(card);
            testPanel.add(getDeckCardsPanel(card));
        }

        deckCardsPanel = new CardsPanel(testCard, testPanel,
                1, DefaultSizes.medCardWidth, DefaultSizes.medCardHeight);
        deckCardsScroll = new JScrollPane(deckCardsPanel);
        deckCardsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        deckCardsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        deckCardsScroll.getHorizontalScrollBar().setUI(new hearthstone.util.CustomScrollBarUI());
        deckCardsScroll.setOpaque(false);
        deckCardsScroll.getViewport().setOpaque(true);
        deckCardsScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        deckCardsScroll.setOpaque(false);
        deckCardsScroll.setBorder(null);
    }

    private void makeHeroButton(){
        heroButton = new HeroButton(HearthStone.baseHeroes.get(0).copy(),   // REAL HERO SHOULD BE
                DefaultSizes.bigHeroWidth,
                DefaultSizes.bigHeroHeight);
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
        allCardsScroll.setBounds(startListX, startListY,
                DefaultSizes.arrangementListWidth,
                DefaultSizes.arrangementListHeight);
        add(allCardsScroll);

        deckCardsScroll.setBounds(startListX, startListY + DefaultSizes.arrangementListHeight + listDis,
                DefaultSizes.arrangementListWidth,
                DefaultSizes.arrangementListHeight);
        add(deckCardsScroll);

        // HERO
        heroButton.setBounds(startHeroX, startHeroY,
                DefaultSizes.bigHeroWidth,
                DefaultSizes.bigHeroHeight);
        add(heroButton);
    }

    private JPanel getDeckCardsPanel(Card card){
        JPanel panel = new JPanel();
        ImageButton removeCard = new ImageButton("REMOVE", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

        removeCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // REMOVE CARD FROM DECK
                deckCardsPanel.removeCard(card);
                allCardsPanel.addCard(card, getAllCardsPanel(card));
            }
        });

        panel.add(removeCard);
        panel.setOpaque(false);
        return panel;
    }

    private JPanel getAllCardsPanel(Card card){
        JPanel panel = new JPanel();
        ImageButton addCard = new ImageButton("ADD", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

        addCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // REMOVE CARD FROM DECK
                allCardsPanel.removeCard(card);
                deckCardsPanel.addCard(card, getDeckCardsPanel(card));
            }
        });

        panel.add(addCard);
        panel.setOpaque(false);
        return panel;
    }
}
