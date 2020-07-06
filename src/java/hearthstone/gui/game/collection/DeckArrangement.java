package hearthstone.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.card.CardsPanel;
import hearthstone.gui.controls.fields.TextField;
import hearthstone.gui.controls.hero.HeroButton;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.FontType;
import hearthstone.util.HearthStoneException;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;

    private final int listDis = 50;
    private final int startListY = (SizeConfigs.gameFrameHeight - 2 * SizeConfigs.arrangementListHeight - listDis) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + SizeConfigs.arrangementListWidth;

    private final int startHeroX = (SizeConfigs.gameFrameWidth + endListX) / 2 - SizeConfigs.bigHeroWidth / 2;
    private final int startHeroY = startListY;

    private final int filterX = (SizeConfigs.gameFrameWidth + endListX) / 2;
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
        setSize(new Dimension(SizeConfigs.gameFrameWidth, SizeConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight, new DeckSelection(hero));

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

    private void makeCardsPanel() {
        ArrayList<Card> initialCards = cardsInFilter(HearthStone.currentAccount.getCollection().getCards());
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Card card : initialCards) {
            Card card1 = card.copy();
            cards.add(card1);
            panels.add(getCardsPanel(card1));
        }

        cardsPanel = new CardsPanel(cards, panels,
                1, SizeConfigs.medCardWidth, SizeConfigs.medCardHeight);
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
                1, SizeConfigs.medCardWidth, SizeConfigs.medCardHeight);
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
                SizeConfigs.bigHeroWidth,
                SizeConfigs.bigHeroHeight);
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
                SizeConfigs.smallButtonWidth,
                SizeConfigs.smallButtonHeight);
        allCardsButton.mouseEntered();

        myCardsButton = new ImageButton("Hero Cards", "buttons/blue_background.png",
                0, Color.white, Color.yellow, false, 12, 0,
                SizeConfigs.smallButtonWidth,
                SizeConfigs.smallButtonHeight);

        lockCardsButton = new ImageButton("Addable", "buttons/blue_background.png",
                0, Color.white, Color.yellow, false, 12, 0,
                SizeConfigs.smallButtonWidth,
                SizeConfigs.smallButtonHeight);

        searchButton = new ImageButton("search", "buttons/green_background.png",
                0, Color.white, Color.yellow, 14, 0,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);

        deleteButton = new ImageButton("delete", "buttons/red_background.png",
                0, Color.white, Color.yellow, 14, 0,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);

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
                    HearthStone.currentAccount.getDecks().remove(deck);
                    hero.getDecks().remove(deck);
                    DataBase.save();
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

    private ArrayList<Card> cardsInFilter(ArrayList<Card> cards) {
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
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);

        removeCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    deck.remove(card, 1);
                    deckCardsPanel.removeCard(card);
                    cardsPanel.addCard(card, getCardsPanel(card));
                    DataBase.save();

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
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);

        addCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    deck.add(card, 1);
                    if (selectedButton != 0)
                        cardsPanel.removeCard(card);
                    deckCardsPanel.addCard(card, getDeckCardsPanel(card));
                    DataBase.save();

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
        cardsScroll.setBounds(startListX, startListY,
                SizeConfigs.arrangementListWidth,
                SizeConfigs.arrangementListHeight);
        add(cardsScroll);

        deckCardsScroll.setBounds(startListX, startListY + SizeConfigs.arrangementListHeight + listDis,
                SizeConfigs.arrangementListWidth,
                SizeConfigs.arrangementListHeight);
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
                SizeConfigs.bigHeroWidth,
                SizeConfigs.bigHeroHeight);
        add(heroButton, JLayeredPane.DEFAULT_LAYER);

        // BUTTONS
        allCardsButton.setBounds(filterX - (int) (SizeConfigs.smallButtonWidth * 1.5) - filterDisX,
                filterY + 2 * filterDisY,
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);
        add(allCardsButton);

        myCardsButton.setBounds(filterX - SizeConfigs.smallButtonWidth / 2,
                filterY + 2 * filterDisY,
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);
        add(myCardsButton);

        lockCardsButton.setBounds(filterX + SizeConfigs.smallButtonWidth / 2 + filterDisX,
                filterY + 2 * filterDisY,
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);
        add(lockCardsButton);


        searchButton.setBounds(filterX - SizeConfigs.medButtonWidth / 2,
                filterY + 3 * filterDisY + 20,
                SizeConfigs.medButtonWidth, SizeConfigs.medButtonHeight);
        add(searchButton);

        deleteButton.setBounds(filterX - SizeConfigs.medButtonWidth / 2,
                startListY + 2 * SizeConfigs.arrangementListHeight + listDis - SizeConfigs.medButtonHeight,
                SizeConfigs.medButtonWidth, SizeConfigs.medButtonHeight);
        add(deleteButton);
    }

    private void update() {
        ArrayList<Card> cards = cardsInFilter(HearthStone.currentAccount.getCollection().getCards());
        ArrayList<JPanel> panels = new ArrayList<>();

        for (Card card : cards) {
            panels.add(getCardsPanel(card));
        }

        cardsPanel.update(cards, panels);
    }
}
