package hearthstone.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.card.CardsPanel;
import hearthstone.gui.controls.hero.HeroButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.HearthStoneException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DeckArrangement extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private ImageButton searchButton, allCardsButton, myCardsButton, lockCardsButton;
    private CardsPanel cardsPanel, deckCardsPanel;
    private JScrollPane allCardsScroll, deckCardsScroll;
    private HeroButton heroButton;
    private JLabel nameLabel, manaLabel;
    private JTextField nameField, manaField;
    private int selectedButton;
    private Hero.Deck deck;
    private Hero hero;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;

    private final int listDis = 70;
    private final int startListY = (DefaultSizes.gameFrameHeight - 2 * DefaultSizes.arrangementListHeight - listDis) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + DefaultSizes.arrangementListWidth;

    private final int startHeroX = (DefaultSizes.gameFrameWidth + endListX) / 2 - DefaultSizes.bigHeroWidth / 2;
    private final int startHeroY = startListY;

    private final int filterX = (DefaultSizes.gameFrameWidth + endListX) / 2;
    private final int filterY = 350;
    private final int filterDisY = 50;
    private final int filterDisX = 10;

    public DeckArrangement(Hero hero, Hero.Deck deck) {
        this.hero = hero;
        this.deck = deck;

        selectedButton = 0;

        configPanel();

        makeIcons();

        makeCardsPanel();

        makeDeckCardsPanel();

        makeHeroButton();

        makeLabels();

        makeFields();

        makeButtons();

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

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HearthStone.logout();
                } catch (HearthStoneException e){
                    System.out.println(e.getMessage());
                } catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                GameFrame.getInstance().setVisible(false);
                GameFrame.getInstance().dispose();
                CredentialsFrame.getNewInstance().setVisible(true);
            }
        });
    }

    private void makeCardsPanel() {
        // MAKE CARDS FILTERING
        ArrayList<Card> cards = HearthStone.currentAccount.getCollection().getCards();
        ArrayList<JPanel> panels = new ArrayList<>();

        for(Card card : cards){
            panels.add(getAllCardsPanel(card));
        }

        cardsPanel = new CardsPanel(cards, panels,
                1, DefaultSizes.medCardWidth, DefaultSizes.medCardHeight);
        allCardsScroll = new JScrollPane(cardsPanel);
        allCardsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        allCardsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        allCardsScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        allCardsScroll.setOpaque(false);
        allCardsScroll.getViewport().setOpaque(true);
        allCardsScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        allCardsScroll.setBorder(null);
    }

    private void makeDeckCardsPanel() {
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for(Card card : deck.getCards()){
            cards.add(card.copy());
            panels.add(getDeckCardsPanel(card));
        }

        deckCardsPanel = new CardsPanel(cards, panels,
                1, DefaultSizes.medCardWidth, DefaultSizes.medCardHeight);
        deckCardsScroll = new JScrollPane(deckCardsPanel);
        deckCardsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        deckCardsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        deckCardsScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        deckCardsScroll.setOpaque(false);
        deckCardsScroll.getViewport().setOpaque(true);
        deckCardsScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        deckCardsScroll.setOpaque(false);
        deckCardsScroll.setBorder(null);
    }

    private void makeHeroButton(){
        heroButton = new HeroButton(hero,   // REAL HERO SHOULD BE
                DefaultSizes.bigHeroWidth,
                DefaultSizes.bigHeroHeight);
    }

    private void makeLabels(){
        nameLabel = new JLabel("name  :  ");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(GameFrame.getInstance().getCustomFont(0, 15));

        manaLabel = new JLabel("mana cost  :  ");
        manaLabel.setForeground(Color.WHITE);
        manaLabel.setFont(GameFrame.getInstance().getCustomFont(0, 15));
    }

    private void makeFields(){
        nameField = new JTextField(8);
        nameField.setFont(GameFrame.getInstance().getCustomFont(0, 15));

        manaField = new JTextField(8);
        manaField.setFont(GameFrame.getInstance().getCustomFont(0, 15));

    }

    private void makeButtons(){
        allCardsButton = new ImageButton("All Cards", "buttons/blue_background.png",
                0, Color.white, Color.yellow, true, 12, 0,
                DefaultSizes.smallButtonWidth,
                DefaultSizes.smallButtonHeight);
        allCardsButton.mouseEntered();

        myCardsButton = new ImageButton("My Cards", "buttons/blue_background.png",
                0, Color.white, Color.yellow, false, 12, 0,
                DefaultSizes.smallButtonWidth,
                DefaultSizes.smallButtonHeight);

        lockCardsButton = new ImageButton("Lock Cards", "buttons/blue_background.png",
                0, Color.white, Color.yellow, false, 12, 0,
                DefaultSizes.smallButtonWidth,
                DefaultSizes.smallButtonHeight);

        searchButton = new ImageButton("search", "buttons/green_background.png",
                0, Color.white, Color.yellow, 14, 0,
                DefaultSizes.medButtonWidth,
                DefaultSizes.medButtonHeight);

        allCardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                allCardsButton.setRadio(true);

                myCardsButton.setRadio(false);
                myCardsButton.mouseExited();

                lockCardsButton.setRadio(false);
                lockCardsButton.mouseExited();
            }
        });

        myCardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                allCardsButton.mouseExited();
                allCardsButton.setRadio(false);

                myCardsButton.setRadio(true);

                lockCardsButton.mouseExited();
                lockCardsButton.setRadio(false);
            }
        });

        lockCardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                allCardsButton.mouseExited();
                allCardsButton.setRadio(false);

                myCardsButton.mouseExited();
                myCardsButton.setRadio(false);

                lockCardsButton.setRadio(true);
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                removeAll();
                repaint();

                layoutComponent();
                repaint();
            }
        });
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
                try {
                    deck.remove(card, 1);

                    deckCardsPanel.removeCard(card);
                    cardsPanel.addCard(card, getAllCardsPanel(card));
                } catch (HearthStoneException e){
                    System.out.println(e.getMessage());
                } catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                restart();
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
                cardsPanel.removeCard(card);
                deckCardsPanel.addCard(card, getDeckCardsPanel(card));
            }
        });

        panel.add(addCard);
        panel.setOpaque(false);
        return panel;
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

        // LABELS
        nameLabel.setBounds(filterX - (int)nameLabel.getPreferredSize().getWidth(), filterY,
                (int)nameLabel.getPreferredSize().getWidth(), (int)nameLabel.getPreferredSize().getHeight());
        add(nameLabel);

        manaLabel.setBounds(filterX - (int)manaLabel.getPreferredSize().getWidth(), filterY + filterDisY,
                (int)manaLabel.getPreferredSize().getWidth(), (int)manaLabel.getPreferredSize().getHeight());
        add(manaLabel);

        // FIELDS
        nameField.setBounds(filterX, filterY,
                (int)nameField.getPreferredSize().getWidth(), (int)nameField.getPreferredSize().getHeight());
        add(nameField);

        manaField.setBounds(filterX, filterY + filterDisY,
                (int)manaField.getPreferredSize().getWidth(), (int)manaField.getPreferredSize().getHeight());
        add(manaField);

        // BUTTONS
        allCardsButton.setBounds(filterX - (int)(DefaultSizes.smallButtonWidth * 1.5) - filterDisX,
                filterY + 2 * filterDisY,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);
        add(allCardsButton);

        myCardsButton.setBounds(filterX - DefaultSizes.smallButtonWidth / 2,
                filterY + 2 * filterDisY,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);
        add(myCardsButton);

        lockCardsButton.setBounds(filterX + DefaultSizes.smallButtonWidth / 2 + filterDisX,
                filterY + 2 * filterDisY,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);
        add(lockCardsButton);

        searchButton.setBounds(filterX - DefaultSizes.medButtonWidth / 2,
                filterY + 3 * filterDisY + 20,
                DefaultSizes.medButtonWidth, DefaultSizes.medButtonHeight);
        add(searchButton);
    }

    private void restart(){
        try {
            DataBase.save();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckArrangement(hero, deck));
    }
}
