package hearthstone.gui.game.collection;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.hero.HeroesPanel;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.util.CustomScrollBarUI;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.HearthStoneException;

import javax.imageio.ImageIO;
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

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.gameFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;

    private final int startListX = DefaultSizes.gameFrameWidth / 2 - DefaultSizes.heroesListWidth / 2;
    private final int startListY = DefaultSizes.gameFrameHeight / 2 - DefaultSizes.heroesListHeight / 2;


    public HeroSelection() {
        configPanel();

        makeIcons();

        makeList();

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
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
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
                    GameFrame.getInstance().stopSound();
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

    private void makeList() {
        ArrayList<Hero> heroes = new ArrayList<>();
        ArrayList<JPanel> panels = new ArrayList<>();

        for(Hero hero : HearthStone.currentAccount.getHeroes()){
            heroes.add(hero);
            panels.add(getHeroPanel(hero));
        }

        heroesPanel = new HeroesPanel(heroes, panels,
                DefaultSizes.bigHeroWidth, DefaultSizes.bigHeroHeight);
        heroesScroll = new JScrollPane(heroesPanel);
        heroesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        heroesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        heroesScroll.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        heroesScroll.setOpaque(false);
        heroesScroll.getViewport().setOpaque(true);
        heroesScroll.getViewport().setBackground(new Color(0, 0, 0, 150));
        heroesScroll.setBorder(null);
    }

    private JPanel getHeroPanel(Hero hero){
        JPanel panel = new JPanel();
        ImageButton arrangeButton = new ImageButton("arrange", "buttons/pink_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);
        arrangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new DeckSelection(hero));
            }
        });

        ImageButton selectionButton;
        if(HearthStone.currentAccount.getSelectedHero() != null && hero.getName().equals(HearthStone.currentAccount.getSelectedHero().getName())) {
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
                    HearthStone.currentAccount.setSelectedHero(hero);
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
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(logoutButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);

        // LISTS
        heroesScroll.setBounds(startListX, startListY,
                DefaultSizes.heroesListWidth,
                DefaultSizes.heroesListHeight);
        add(heroesScroll);
    }

    private void restart(){
        try {
            DataBase.save();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new HeroSelection());
    }
}
