package hearthstone.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.BaseFrame;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.NameDialog;
import hearthstone.gui.controls.deck.DecksPanel;
import hearthstone.gui.controls.hero.HeroButton;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.HearthStoneException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DeckSelection extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private DecksPanel deckPanel;
    private JScrollPane deckCardScroll;
    private HeroButton heroButton;
    private ImageButton addButton;
    private Hero hero;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startListY = (DefaultSizes.gameFrameHeight - DefaultSizes.heroSelectionListHeight) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + DefaultSizes.heroSelectionListWidth;

    private final int startHeroX = (DefaultSizes.gameFrameWidth + endListX) / 2 - DefaultSizes.bigHeroWidth / 2;
    private final int startHeroY = startListY;

    private final int startAddButtonX = startHeroX + DefaultSizes.bigHeroWidth / 2 - DefaultSizes.medButtonWidth / 2;
    private final int startAddButtonY = startHeroY + DefaultSizes.bigHeroHeight + 20;

    public DeckSelection(Hero hero) {
        this.hero = hero;
        configPanel();

        makeIcons();

        makeDeckList();

        makeHeroButton();

        makeAddButton();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new HeroSelection());
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

    private void makeDeckList() {
        ArrayList<Deck> decks = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for(Deck deck : hero.getDecks()){
            decks.add(deck);
            panels.add(getDeckPanel(deck));
        }

        deckPanel = new DecksPanel(decks, panels,
                DefaultSizes.deckWidth, DefaultSizes.deckHeight);
        deckCardScroll = new JScrollPane(deckPanel);
        deckCardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        deckCardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        deckCardScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        deckCardScroll.setOpaque(false);
        deckCardScroll.getViewport().setOpaque(true);
        deckCardScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        deckCardScroll.setBorder(null);
    }

    private void makeHeroButton() {
        heroButton = new HeroButton(hero,  // REAL HERO SHOULD BE
                DefaultSizes.bigHeroWidth,
                DefaultSizes.bigHeroHeight);
    }

    private void makeAddButton(){
        addButton = new ImageButton("New Deck", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                DefaultSizes.medButtonWidth, DefaultSizes.medButtonHeight);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                NameDialog nameDialog = new NameDialog(GameFrame.getInstance(), "Deck Name : ", DefaultSizes.dialogWidth, DefaultSizes.dialogHeight);
                String name = nameDialog.getValue();
                Deck beforeDeck = hero.getDecks().stream().
                        filter(deck -> name.equals(deck.getName())).findAny().orElse(null);

                if(beforeDeck != null){
                    BaseFrame.error("this name is already token!");
                }

                if (beforeDeck == null && name.length() != 0) {
                    Deck deck = new Deck(name, hero.getType());
                    hero.getDecks().add(deck);
                    HearthStone.currentAccount.getDecks().add(deck);

                    restart();
                }
            }
        });
    }

    private JPanel getDeckPanel(Deck deck) {
        JPanel panel = new JPanel();
        ImageButton arrangeButton = new ImageButton("arrange", "buttons/pink_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);
        arrangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckArrangement(hero, deck));
            }
        });

        ImageButton selectionButton;
        if (hero.getSelectedDeck() != null && hero.getSelectedDeck().getName().equals(deck.getName())) {
            selectionButton = new ImageButton("selected", "buttons/green_background.png", 0,
                    Color.white, Color.yellow,
                    15, 0,
                    DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);
        } else {
            selectionButton = new ImageButton("select", "buttons/blue_background.png", 0,
                    Color.white, Color.yellow,
                    15, 0,
                    DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

            selectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    hero.setSelectedDeck(deck);
                    restart();
                }
            });
        }

        panel.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridy = 0;
        grid.gridx = 0;
        panel.add(arrangeButton, grid);

        grid.gridy = 1;
        grid.gridx = 0;
        grid.insets = new Insets(10, 0, 0, 0);
        panel.add(selectionButton, grid);

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
        deckCardScroll.setBounds(startListX, startListY,
                DefaultSizes.heroSelectionListWidth,
                DefaultSizes.heroSelectionListHeight);
        add(deckCardScroll);

        // HERO
        heroButton.setBounds(startHeroX, startHeroY,
                DefaultSizes.bigHeroWidth,
                DefaultSizes.bigHeroHeight);
        add(heroButton);

        // BUTTON
        addButton.setBounds(startAddButtonX, startAddButtonY,
                DefaultSizes.medButtonWidth,
                DefaultSizes.medButtonHeight);
        add(addButton);
    }

    private void restart() {
        try {
            DataBase.save();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckSelection(hero));
    }
}
