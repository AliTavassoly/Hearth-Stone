package hearthstone.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.hero.HeroesPanel;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.HearthStoneException;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HeroSelection extends JPanel {
    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private HeroesPanel heroesPanel;
    private JScrollPane heroesScroll;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;

    private final int startListX = SizeConfigs.gameFrameWidth / 2 - SizeConfigs.heroesListWidth / 2;
    private final int startListY = SizeConfigs.gameFrameHeight / 2 - SizeConfigs.heroesListHeight / 2;


    public HeroSelection() {
        configPanel();

        makeIcons();

        makeList();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
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

    private void makeList() {
        ArrayList<Hero> heroes = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (int i = 0; i < HearthStone.currentAccount.getHeroes().size(); i++) {
            if (HearthStone.currentAccount.getSelectedHero() == null)
                break;
            Hero hero = HearthStone.currentAccount.getHeroes().get(i);
            Hero zero = HearthStone.currentAccount.getHeroes().get(0);

            if (HearthStone.currentAccount.getSelectedHero().getName().equals(hero.getName())) {
                HearthStone.currentAccount.getHeroes().set(0, hero);
                HearthStone.currentAccount.getHeroes().set(i, zero);
            }
        }

        for (Hero hero : HearthStone.currentAccount.getHeroes()) {
            heroes.add(hero);
            panels.add(getHeroPanel(hero));
        }

        heroesPanel = new HeroesPanel(heroes, panels,
                SizeConfigs.bigHeroWidth, SizeConfigs.bigHeroHeight);
        heroesScroll = new JScrollPane(heroesPanel);
        heroesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        heroesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        heroesScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        heroesScroll.setOpaque(false);
        heroesScroll.getViewport().setOpaque(true);
        heroesScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        heroesScroll.setBorder(null);
    }

    private JPanel getHeroPanel(Hero hero) {
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
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckSelection(hero));
            }
        });

        ImageButton selectionButton;
        if (HearthStone.currentAccount.getSelectedHero() != null && hero.getName().equals(HearthStone.currentAccount.getSelectedHero().getName())) {
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
                        HearthStone.currentAccount.setSelectedHero(hero);
                        Mapper.getInstance().saveDataBase();

                        hearthstone.util.Logger.saveLog("Click_button",
                                "select");
                        hearthstone.util.Logger.saveLog("Select hero",
                                hero.getName() + " selected for battle!");
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
        //
        backButton.setBounds(iconX, startIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(logoutButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(closeButton);

        // LISTS
        heroesScroll.setBounds(startListX, startListY,
                SizeConfigs.heroesListWidth,
                SizeConfigs.heroesListHeight);
        add(heroesScroll);
    }

    private void restart() {
        try {
            Mapper.getInstance().saveDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new HeroSelection());
    }
}
