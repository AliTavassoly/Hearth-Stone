package hearthstone.client.gui.controls.panels;

import hearthstone.client.gui.controls.buttons.DeckButton;
import hearthstone.models.Deck;
import hearthstone.shared.GUIConfigs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DecksPanel extends JPanel {
    private ArrayList<Deck> decks;
    private ArrayList<JPanel> panels;
    private ArrayList<DeckButton> deckButtons;
    private int deckWidth, deckHeight;

    private int startX = 10;
    private int startY = 10;
    private int disY = 10;

    public DecksPanel(ArrayList<Deck> decks, ArrayList<JPanel> panels,
                      int deckWidth, int deckHeight) {
        this.decks = decks;
        this.panels = panels;
        this.deckWidth = deckWidth;
        this.deckHeight = deckHeight;

        deckButtons = new ArrayList<>();

        for (Deck deck : decks) {
            DeckButton deckButton = new DeckButton(deck,
                    deckWidth,
                    deckHeight);
            deckButtons.add(deckButton);
        }

        disY += deckHeight;

        configPanel();

        layoutComponent();
    }

    private void configPanel() {
        setLayout(null);
        setBackground(new Color(0, 0, 0, 120));
        setPreferredSize(
                new Dimension(GUIConfigs.statusListWidth, decks.size() * disY + 10));  // ------------------------------
        setOpaque(false);
        setVisible(true);
    }

    private void layoutComponent() {
        for (int i = 0; i < decks.size(); i++) {
            DeckButton deckButton = deckButtons.get(i);
            JPanel panel = panels.get(i);

            deckButton.setBounds(startX, startY + i * disY,
                    deckWidth, deckHeight);
            if (panel != null) {
                panel.setBounds(startX + deckWidth + 10, startY + i * disY + 7,
                        (int) panel.getPreferredSize().getWidth(),
                        (int) panel.getPreferredSize().getHeight());
            }

            add(deckButton);
            if (panel != null)
                add(panel);
        }
    }
}
