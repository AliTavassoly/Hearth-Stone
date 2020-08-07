package hearthstone.client.gui.game.collection;

import hearthstone.client.network.ClientMapper;
import hearthstone.client.network.HSClient;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.panels.HeroesPanel;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.gui.util.CustomScrollBarUI;
import hearthstone.models.hero.Hero;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HeroSelection extends JPanel {
    private static HeroSelection instance;

    private ImageButton backButton, minimizeButton, closeButton, logoutButton;
    private HeroesPanel heroesPanel;
    private JScrollPane heroesScroll;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;

    private final int startListX = GUIConfigs.gameFrameWidth / 2 - GUIConfigs.heroesListWidth / 2;
    private final int startListY = GUIConfigs.gameFrameHeight / 2 - GUIConfigs.heroesListHeight / 2;


    private HeroSelection() {
        configPanel();

        makeIcons();

        makeList();

        layoutComponent();
    }

    public static HeroSelection getInstance(){
        return instance;
    }

    public static HeroSelection makeInstance(){
        return instance = new HeroSelection();
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
        setSize(new Dimension(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight, new MainMenuPanel());

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

    private void makeList() {
        ArrayList<Hero> heroes = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for (int i = 0; i < HSClient.currentAccount.getHeroes().size(); i++) {
            if (HSClient.currentAccount.getSelectedHero() == null)
                break;
            Hero hero = HSClient.currentAccount.getHeroes().get(i);
            Hero zero = HSClient.currentAccount.getHeroes().get(0);

            if (HSClient.currentAccount.getSelectedHero().getName().equals(hero.getName())) {
                HSClient.currentAccount.getHeroes().set(0, hero);
                HSClient.currentAccount.getHeroes().set(i, zero);
            }
        }

        for (Hero hero : HSClient.currentAccount.getHeroes()) {
            heroes.add(hero);
            panels.add(getHeroPanel(hero));
        }

        heroesPanel = new HeroesPanel(heroes, panels,
                GUIConfigs.bigHeroWidth, GUIConfigs.bigHeroHeight);
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
                GUIConfigs.smallButtonWidth, GUIConfigs.smallButtonHeight);
        arrangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), DeckSelection.makeInstance(hero.getName()));
            }
        });

        ImageButton selectionButton;
        if (HSClient.currentAccount.getSelectedHero() != null && hero.getName().equals(HSClient.currentAccount.getSelectedHero().getName())) {
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
                    //try {
                        //HSClient.currentAccount.setSelectedHero(hero);
                        //Mapper.saveDataBase();

                        /*hearthstone.util.Logger.saveLog("Click_button",
                                "select");
                        hearthstone.util.Logger.saveLog("Select hero",
                                hero.getName() + " selected for battle!");*/
                    /*} catch (HearthStoneException e) {
                        try {
                            hearthstone.util.Logger.saveLog("ERROR",
                                    e.getClass().getName() + ": " + e.getMessage()
                                            + "\nStack Trace: " + e.getStackTrace());
                        } catch (Exception f) {
                        }
                        BaseFrame.error(e.getMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }*/

                    ClientMapper.selectHeroRequest(hero.getName());

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
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(logoutButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(closeButton);

        // LISTS
        heroesScroll.setBounds(startListX, startListY,
                GUIConfigs.heroesListWidth,
                GUIConfigs.heroesListHeight);
        add(heroesScroll);
    }

    public void restart() {
        /*try {
            Mapper.saveDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new HeroSelection());
    }
}
