package hearthstone.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.buttons.HeroButton;
import hearthstone.gui.controls.buttons.ImageButton;
import hearthstone.gui.controls.dialogs.ErrorDialog;
import hearthstone.gui.controls.dialogs.NameDialog;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.controls.panels.DecksPanel;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.hero.IHero;
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
    private IHero hero;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int startListY = (SizeConfigs.gameFrameHeight - SizeConfigs.deckSelectionListHeight) / 2;
    private final int startListX = 100;
    private final int endListX = startListX + SizeConfigs.deckSelectionListWidth;

    private final int startHeroX = (SizeConfigs.gameFrameWidth + endListX) / 2 - SizeConfigs.bigHeroWidth / 2;
    private final int startHeroY = startListY;

    private final int startAddButtonX = startHeroX + SizeConfigs.bigHeroWidth / 2 - SizeConfigs.medButtonWidth / 2;
    private final int startAddButtonY = startHeroY + SizeConfigs.bigHeroHeight + 20;

    public DeckSelection(IHero hero) {
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
        setSize(new Dimension(SizeConfigs.gameFrameWidth, SizeConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight, new HeroSelection());

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
                SizeConfigs.deckWidth, SizeConfigs.deckHeight);
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
                SizeConfigs.bigHeroWidth,
                SizeConfigs.bigHeroHeight);
    }

    private void makeAddButton(){
        addButton = new ImageButton("New Deck", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                SizeConfigs.medButtonWidth, SizeConfigs.medButtonHeight);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                NameDialog nameDialog = new NameDialog(GameFrame.getInstance(),
                        "Deck Name : ", SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);
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
                        HearthStone.currentAccount.getDecks().add(deck);

                        Mapper.getInstance().saveDataBase();

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
                                    SizeConfigs.errorWidth, SizeConfigs.errorHeight);
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
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);
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
                    SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);
        } else {
            selectionButton = new ImageButton("select", "buttons/blue_background.png", 0,
                    Color.white, Color.yellow,
                    15, 0,
                    SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);

            selectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        hero.setSelectedDeck(deck);

                        Mapper.getInstance().saveDataBase();

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
        deckCardScroll.setBounds(startListX, startListY,
                SizeConfigs.deckSelectionListWidth,
                SizeConfigs.deckSelectionListHeight);
        add(deckCardScroll);

        // HERO
        heroButton.setBounds(startHeroX, startHeroY,
                SizeConfigs.bigHeroWidth,
                SizeConfigs.bigHeroHeight);
        add(heroButton);

        // BUTTON
        addButton.setBounds(startAddButtonX, startAddButtonY,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);
        add(addButton);
    }

    private void restart() {
        try {
            Mapper.getInstance().saveDataBase();
        } catch (Exception e){
            e.printStackTrace();
        }
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckSelection(hero));
    }
}
