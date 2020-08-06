package hearthstone.client.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.client.HSClient;
import hearthstone.client.gui.BaseFrame;
import hearthstone.client.data.GUIConfigs;
import hearthstone.client.gui.controls.buttons.HeroButton;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.dialogs.ErrorDialog;
import hearthstone.client.gui.controls.dialogs.NameDialog;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.panels.DecksPanel;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.util.CustomScrollBarUI;
import hearthstone.models.Deck;
import hearthstone.models.hero.Hero;
import hearthstone.util.HearthStoneException;
import hearthstone.util.getresource.ImageResource;

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

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startListY = (GUIConfigs.gameFrameHeight - GUIConfigs.deckSelectionListHeight) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + GUIConfigs.deckSelectionListWidth;

    private final int startHeroX = (GUIConfigs.gameFrameWidth + endListX) / 2 - GUIConfigs.bigHeroWidth / 2;
    private final int startHeroY = startListY;

    private final int startAddButtonX = startHeroX + GUIConfigs.bigHeroWidth / 2 - GUIConfigs.medButtonWidth / 2;
    private final int startAddButtonY = startHeroY + GUIConfigs.bigHeroHeight + 20;

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
        BufferedImage image = null;
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
                GUIConfigs.iconHeight, new HeroSelection());

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

    private void makeDeckList() {
        ArrayList<Deck> decks = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for(int i = 0; i < hero.getDecks().size(); i++){
            if(hero.getSelectedDeck() == null)
                break;
            Deck deck = hero.getDecks().get(i);
            if(deck.getName().equals(hero.getSelectedDeck().getName())){
                Deck selected = deck;
                Deck zero = hero.getDecks().get(0);

                hero.getDecks().set(i, zero);
                hero.getDecks().set(0, selected);
                break;
            }
        }

        for(Deck deck : hero.getDecks()){
            decks.add(deck);
            panels.add(getDeckPanel(deck));
        }

        deckPanel = new DecksPanel(decks, panels,
                GUIConfigs.deckWidth, GUIConfigs.deckHeight);
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
                GUIConfigs.bigHeroWidth,
                GUIConfigs.bigHeroHeight);
    }

    private void makeAddButton(){
        addButton = new ImageButton("New Deck", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                GUIConfigs.medButtonWidth, GUIConfigs.medButtonHeight);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                NameDialog nameDialog = new NameDialog(GameFrame.getInstance(),
                        "Deck Name : ", GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);
                String name = nameDialog.getValue();
                Deck beforeDeck = hero.getDecks().stream().
                        filter(deck -> name.equals(deck.getName())).findAny().orElse(null);

                try {
                    hearthstone.util.Logger.saveLog("Click_button",
                            "new_deck_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(beforeDeck != null){
                    BaseFrame.error("this name is already token!");
                }

                if (beforeDeck == null && name.length() != 0) {
                    try {
                        Deck deck = new Deck(name, hero.getType());
                        hero.makeNewDeck(deck);
                        HSClient.currentAccount.getDecks().add(deck);

                        Mapper.saveDataBase();

                        hearthstone.util.Logger.saveLog("New Deck",
                                deck.getName() + " deck created!");
                        restart();
                    } catch (HearthStoneException e){
                        try {
                            hearthstone.util.Logger.saveLog("ERROR",
                                    e.getClass().getName() + ": " + e.getMessage()
                                            + "\nStack Trace: " + e.getStackTrace());

                            ErrorDialog errorDialog = new ErrorDialog(GameFrame.getInstance(),
                                    e.getMessage(),
                                    GUIConfigs.errorWidth, GUIConfigs.errorHeight);
                        } catch (Exception f) { }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private JPanel getDeckPanel(Deck deck) {
        JPanel panel = new JPanel();
        ImageButton arrangeButton = new ImageButton("arrange", "buttons/pink_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);
        arrangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    hearthstone.util.Logger.saveLog("Click_button",
                            "arrange");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckArrangement(hero, deck));
            }
        });

        ImageButton selectionButton;
        if (hero.getSelectedDeck() != null && hero.getSelectedDeck().getName().equals(deck.getName())) {
            selectionButton = new ImageButton("selected", "buttons/green_background.png", 0,
                    Color.white, Color.yellow,
                    15, 0,
                    GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);
        } else {
            selectionButton = new ImageButton("select", "buttons/blue_background.png", 0,
                    Color.white, Color.yellow,
                    15, 0,
                    GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);

            selectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        hero.setSelectedDeck(deck);

                        Mapper.saveDataBase();

                        restart();

                        hearthstone.util.Logger.saveLog("Click_button",
                                "select");

                        hearthstone.util.Logger.saveLog("Select deck",
                                deck.getName() + " deck, selected for " + hero.getName() + "!");
                    } catch (HearthStoneException e){
                        try {
                            hearthstone.util.Logger.saveLog("ERROR",
                                    e.getClass().getName() + ": " + e.getMessage()
                                            + "\nStack Trace: " + e.getStackTrace());
                        } catch (Exception f) { }
                        BaseFrame.error(e.getMessage());
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
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
        deckCardScroll.setBounds(startListX, startListY,
                GUIConfigs.deckSelectionListWidth,
                GUIConfigs.deckSelectionListHeight);
        add(deckCardScroll);

        // HERO
        heroButton.setBounds(startHeroX, startHeroY,
                GUIConfigs.bigHeroWidth,
                GUIConfigs.bigHeroHeight);
        add(heroButton);

        // BUTTON
        addButton.setBounds(startAddButtonX, startAddButtonY,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);
        add(addButton);
    }

    private void restart() {
        try {
            Mapper.saveDataBase();
        } catch (Exception e){
            e.printStackTrace();
        }
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckSelection(hero));
    }
}
