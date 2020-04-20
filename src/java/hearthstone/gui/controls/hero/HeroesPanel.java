package hearthstone.gui.controls.hero;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.logic.models.hero.Hero;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HeroesPanel extends JPanel {
    private ArrayList<Hero> heroes;
    private ArrayList<JPanel> panels;
    private ArrayList<HeroButton> heroButtons;
    private int heroWidth, heroHeight;

    private int startX = 10;
    private int startY = 10;
    private int disX = 10;
    private int disY = -30;

    public HeroesPanel(ArrayList<Hero> heroes, ArrayList<JPanel> panels,
                       int heroWidth, int heroHeight) {
        this.heroes = heroes;
        this.panels = panels;
        this.heroWidth = heroWidth;
        this.heroHeight = heroHeight;

        heroButtons = new ArrayList<>();

        for (Hero hero : heroes) {
            HeroButton heroButton = new HeroButton(hero,
                    DefaultSizes.bigHeroWidth,
                    DefaultSizes.bigHeroHeight);
            heroButtons.add(heroButton);
        }

        disX += heroWidth;

        configPanel();

        layoutComponent();
    }

    public void addDeck(Hero hero, JPanel panel) {
        HeroButton heroButton = new HeroButton(HearthStone.baseHeroes.get(0).copy(),
                heroWidth,
                heroHeight);

        heroButtons.add(heroButton);
        heroes.add(hero);
        panels.remove(panel);

        removeAll();
        layoutComponent();
    }

    public void removeDeck(Hero hero) {
        int ind = heroes.indexOf(hero);

        removeAll();
        repaint();
        heroButtons.remove(ind);
        heroes.remove(ind);
        panels.remove(ind);
        layoutComponent();
        repaint();
    }

    private void configPanel() {
        setLayout(null);
        setBackground(new Color(0, 0, 0, 120));
        setPreferredSize(
                new Dimension(heroes.size() * disX, DefaultSizes.heroesListHeight + disY));  // ------------------------------
        setOpaque(false);
        setVisible(true);
    }

    private void layoutComponent() {
        for (int i = 0; i < heroes.size(); i++) {
            HeroButton heroButton = heroButtons.get(i);
            JPanel panel = panels.get(i);

            heroButton.setBounds(startX + i * disX, startY,
                    heroWidth, heroHeight);

            if (panel != null) {
                panel.setBounds(startX + i * disX + heroWidth / 2 -
                                (int) panel.getPreferredSize().getWidth() / 2,
                        startY + heroHeight + disY,
                        (int) panel.getPreferredSize().getWidth(),
                        (int) panel.getPreferredSize().getHeight());
            }

            add(heroButton);
            if (panel != null)
                add(panel);
        }
    }
}
