package hearthstone.gui.controls;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.logic.models.cards.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CardsPanel extends JPanel {
    private ArrayList<Card> cards;
    private ArrayList<JPanel> panels;
    private ArrayList<CardButton> cardButtons;
    private int cardHeight, cardWidth;

    private int disX = 10;
    private int disY = 120;
    private int rows;

    public CardsPanel(ArrayList<Card> cards, ArrayList<JPanel> panels,
                      int rows, int cardWidth, int cardHeight) {
        this.cards = cards;
        this.panels = panels;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.rows = rows;

        cardButtons = new ArrayList<>();

        for (Card card : cards) {
            CardButton cardButton = new CardButton(HearthStone.baseCards.get(6).copy(),
                    cardWidth,
                    cardHeight);
            cardButtons.add(cardButton);
        }

        disY += cardHeight;
        for (JPanel panel : panels) {
            if (panel != null) {
                disY += panels.get(0).getHeight();
                break;
            }
        }
        disX += cardWidth;

        configPanel();

        layoutComponent();
    }

    public void addCard(Card card, JPanel panel) {
        CardButton cardButton = new CardButton(HearthStone.baseCards.get(6).copy(),
                cardWidth,
                cardHeight);

        cardButtons.add(cardButton);
        cards.add(card);
        panels.remove(panel);

        removeAll();
        layoutComponent();
    }

    public void removeCard(Card card) {
        int ind = cards.indexOf(card);

        removeAll();
        repaint();
        cardButtons.remove(ind);
        cards.remove(ind);
        panels.remove(ind);
        layoutComponent();
        repaint();
    }

    private void configPanel() {
        setLayout(null);
        setBackground(new Color(0, 0, 0, 50));
        setPreferredSize(
                new Dimension((cards.size() + rows - 1) / rows * disX, rows * disY));
        setVisible(true);
    }

    private void layoutComponent() {
        for (int i = 0; i < cards.size(); i++) {
            CardButton cardButton = cardButtons.get(i);
            JPanel panel = panels.get(i);

            int col = (i - (i % rows)) / rows;
            int row = i % rows;

            cardButton.setBounds(col * disX, row * disY,
                    cardWidth, cardHeight);
            if (panel != null) {
                panel.setBounds(col * disX + cardWidth / 2
                                - (int) panel.getPreferredSize().getWidth() / 2,
                        row * disY + cardHeight,
                        (int) panel.getPreferredSize().getWidth(),
                        (int) panel.getPreferredSize().getHeight());
            }
            add(cardButton);
            if (panel != null)
                add(panel);
        }
    }
}
