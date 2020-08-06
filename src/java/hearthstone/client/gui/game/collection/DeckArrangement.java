package hearthstone.client.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.client.HSClient;
import hearthstone.client.gui.BaseFrame;
import hearthstone.client.data.GUIConfigs;
import hearthstone.client.gui.controls.buttons.HeroButton;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.fields.TextField;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.panels.CardsPanel;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.util.CustomScrollBarUI;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.FontType;
import hearthstone.util.HearthStoneException;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class DeckArrangement extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private ImageButton searchButton, allCardsButton, myCardsButton, lockCardsButton, deleteButton;
    private CardsPanel cardsPanel, deckCardsPanel;
    private JScrollPane cardsScroll, deckCardsScroll;
    private HeroButton heroButton;
    private JLabel nameLabel, manaLabel;
    private TextField nameField, manaField;
    private int selectedButton;
    private Deck deck;
    private Hero hero;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;

    private final int listDis = 50;
    private final int startListY = (GUIConfigs.gameFrameHeight - 2 * GUIConfigs.arrangementListHeight - listDis) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + GUIConfigs.arrangementListWidth;

    private final int startHeroX = (GUIConfigs.gameFrameWidth + endListX) / 2 - GUIConfigs.bigHeroWidth / 2;
    private final int startHeroY = startListY;

    private final int filterX = (GUIConfigs.gameFrameWidth + endListX) / 2;
    private final int filterY = 350;
    private final int filterDisY = 50;
    private final int filterDisX = 10;

    public DeckArrangement(Hero hero, Deck deck) {
        this.hero = hero;
        this.deck = deck;

        selectedButton = 0;

        configPanel();

        makeIcons();

        makeLabels();

        makeFields();

        makeHeroButton();

        makeButtons();

        makeCardsPanel();

        makeDeckCardsPanel();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if(backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                    "/images/hero_selection_background.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void configPanel() {
        setSize(new Dimension(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight, new DeckSelection(hero));

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

    private void makeCardsPanel() {
        ArrayList<Card> initialCards = cardsInFilter(HSClient.currentAccount.getCollection().getCards());
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Card card : initialCards) {
            Card card1 = card.copy();
            cards.add(card1);
            panels.add(getCardsPanel(card1));
        }

        cardsPanel = new CardsPanel(cards, panels,
                1, GUIConfigs.medCardWidth, GUIConfigs.medCardHeight);
        cardsScroll = new JScrollPane(cardsPanel);
        cardsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cardsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        cardsScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        cardsScroll.setOpaque(false);
        cardsScroll.getViewport().setOpaque(true);
        cardsScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        cardsScroll.setBorder(null);
    }

    private void makeDeckCardsPanel() {
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Card card : deck.getCards()) {
            Card card1 = card.copy();
            cards.add(card1);
            panels.add(getDeckCardsPanel(card1));
        }

        deckCardsPanel = new CardsPanel(cards, panels,
                1, GUIConfigs.medCardWidth, GUIConfigs.medCardHeight);
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

    private void makeHeroButton() {
        heroButton = new HeroButton(hero,   // REAL HERO SHOULD BE
                GUIConfigs.bigHeroWidth,
                GUIConfigs.bigHeroHeight);
    }

    private void makeLabels() {
        nameLabel = new JLabel("name  :  ");
        nameLabel.setForeground(new Color(255, 255, 68));
        nameLabel.setFont(GameFrame.getInstance().getCustomFont(FontType.TEXT,0, 15));

        manaLabel = new JLabel("mana cost  :  ");
        manaLabel.setForeground(new Color(255, 255, 68));
        manaLabel.setFont(GameFrame.getInstance().getCustomFont(FontType.TEXT,0, 15));
    }

    private void makeFields() {
        nameField = new TextField(8);

        manaField = new TextField(8);
    }

    private void makeButtons() {
        allCardsButton = new ImageButton("All Cards", "buttons/blue_background.png",
                0, Color.white, Color.yellow, true, 12, 0,
                GUIConfigs.smallButtonWidth,
                GUIConfigs.smallButtonHeight);
        allCardsButton.mouseEntered();

        myCardsButton = new ImageButton("Hero Cards", "buttons/blue_background.png",
                0, Color.white, Color.yellow, false, 12, 0,
                GUIConfigs.smallButtonWidth,
                GUIConfigs.smallButtonHeight);

        lockCardsButton = new ImageButton("Addable", "buttons/blue_background.png",
                0, Color.white, Color.yellow, false, 12, 0,
                GUIConfigs.smallButtonWidth,
                GUIConfigs.smallButtonHeight);

        searchButton = new ImageButton("search", "buttons/green_background.png",
                0, Color.white, Color.yellow, 14, 0,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);

        deleteButton = new ImageButton("delete", "buttons/red_background.png",
                0, Color.white, Color.yellow, 14, 0,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);

        allCardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedButton = 0;
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
                selectedButton = 1;
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
                selectedButton = 2;
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
                try {
                    hearthstone.util.Logger.saveLog("Click_button",
                            "search");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                update();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HSClient.currentAccount.getDecks().remove(deck);
                    hero.getDecks().remove(deck);

                    Mapper.saveDataBase();

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckSelection(hero));

                    hearthstone.util.Logger.saveLog("Click_button",
                            "delete");
                    hearthstone.util.Logger.saveLog("Delete deck",
                            deck.getName() + " removed from decks!");
                } catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage()
                                        + "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) { }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private <T> ArrayList<Card> cardsInFilter(List<Card> cards) {
        ArrayList<Card> ans = new ArrayList<>();
        for (Card card : cards) {
            if (nameField.getText().length() != 0) {
                if (!card.getName().contains(nameField.getText()))
                    continue;
            }
            if (manaField.getText().length() != 0) {
                if (!String.valueOf(card.getManaCost()).equals(manaField.getText()))
                    continue;
            }
            if (selectedButton == 0) {
                if (card.getHeroType() != HeroType.ALL && card.getHeroType() != hero.getType())
                    continue;
            } else if (selectedButton == 1) {
                if (card.getHeroType() != hero.getType())
                    continue;
            } else if (selectedButton == 2) {
                if (!deck.canAdd(card, 1))
                    continue;

                int cardNumber = 1;
                for (Card card1 : ans) {
                    if (card1.getId() == card.getId()) {
                        cardNumber++;
                    }
                }

                if (!deck.canAdd(card, cardNumber))
                    continue;
            }
            ans.add(card.copy());
        }
        return ans;
    }

    private JPanel getDeckCardsPanel(Card card) {
        JPanel panel = new JPanel();
        ImageButton removeCard = new ImageButton("REMOVE", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);

        removeCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    //deck.remove(card, 1);
                    Mapper.removeFromDeck(deck, card, 1);

                    deckCardsPanel.removeCard(card);
                    cardsPanel.addCard(card, getCardsPanel(card));

                    Mapper.saveDataBase();

                    hearthstone.util.Logger.saveLog("Click_button",
                            "remove");
                    hearthstone.util.Logger.saveLog("Remove From Deck",
                            card.getName() + " removed from deck!");
                } catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage()
                                        + "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) { }
                    BaseFrame.error(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.add(removeCard);
        panel.setOpaque(false);
        return panel;
    }

    private JPanel getCardsPanel(Card card) {
        JPanel panel = new JPanel();
        ImageButton addCard = new ImageButton("ADD", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);

        addCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    //deck.add(card, 1);
                    Mapper.addToDeck(deck, card, 1);
                    if (selectedButton != 0)
                        cardsPanel.removeCard(card);
                    deckCardsPanel.addCard(card, getDeckCardsPanel(card));

                    Mapper.saveDataBase();

                    hearthstone.util.Logger.saveLog("Click_button",
                            "add");
                    hearthstone.util.Logger.saveLog("Add To Deck",
                            card.getName() + " added to deck!");
                } catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage()
                                        + "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) { }
                    BaseFrame.error(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.add(addCard);
        panel.setOpaque(false);
        return panel;
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
        cardsScroll.setBounds(startListX, startListY,
                GUIConfigs.arrangementListWidth,
                GUIConfigs.arrangementListHeight);
        add(cardsScroll);

        deckCardsScroll.setBounds(startListX, startListY + GUIConfigs.arrangementListHeight + listDis,
                GUIConfigs.arrangementListWidth,
                GUIConfigs.arrangementListHeight);
        add(deckCardsScroll);

        // LABELS
        nameLabel.setBounds(filterX - (int) nameLabel.getPreferredSize().getWidth(), filterY,
                (int) nameLabel.getPreferredSize().getWidth(), (int) nameLabel.getPreferredSize().getHeight());
        add(nameLabel);

        manaLabel.setBounds(filterX - (int) manaLabel.getPreferredSize().getWidth(), filterY + filterDisY,
                (int) manaLabel.getPreferredSize().getWidth(), (int) manaLabel.getPreferredSize().getHeight());
        add(manaLabel);

        // FIELDS
        nameField.setBounds(filterX, filterY,
                (int) nameField.getPreferredSize().getWidth(), (int) nameField.getPreferredSize().getHeight());
        add(nameField);

        manaField.setBounds(filterX, filterY + filterDisY,
                (int) manaField.getPreferredSize().getWidth(), (int) manaField.getPreferredSize().getHeight());
        add(manaField);

        // HERO
        heroButton.setBounds(startHeroX, startHeroY,
                GUIConfigs.bigHeroWidth,
                GUIConfigs.bigHeroHeight);
        add(heroButton, JLayeredPane.DEFAULT_LAYER);

        // BUTTONS
        allCardsButton.setBounds(filterX - (int) (GUIConfigs.smallButtonWidth * 1.5) - filterDisX,
                filterY + 2 * filterDisY,
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);
        add(allCardsButton);

        myCardsButton.setBounds(filterX - GUIConfigs.smallButtonWidth / 2,
                filterY + 2 * filterDisY,
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);
        add(myCardsButton);

        lockCardsButton.setBounds(filterX + GUIConfigs.smallButtonWidth / 2 + filterDisX,
                filterY + 2 * filterDisY,
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);
        add(lockCardsButton);


        searchButton.setBounds(filterX - GUIConfigs.medButtonWidth / 2,
                filterY + 3 * filterDisY + 20,
                GUIConfigs.medButtonWidth, GUIConfigs.medButtonHeight);
        add(searchButton);

        deleteButton.setBounds(filterX - GUIConfigs.medButtonWidth / 2,
                startListY + 2 * GUIConfigs.arrangementListHeight + listDis - GUIConfigs.medButtonHeight,
                GUIConfigs.medButtonWidth, GUIConfigs.medButtonHeight);
        add(deleteButton);
    }

    private void update() {
        ArrayList<Card> cards = cardsInFilter(HSClient.currentAccount.getCollection().getCards());
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Card card : cards) {
            panels.add(getCardsPanel(card));
        }

        cardsPanel.update(cards, panels);
    }
}