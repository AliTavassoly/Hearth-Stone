package hearthstone.gui.controls;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.logic.models.cards.Card;

import javax.swing.*;
import java.awt.*;

public class CardsPanel extends JPanel {

    public CardsPanel() {
        int dis = 210;
        CardButton [] cardButtons = new CardButton[10];
        for(int i = 0; i < 10; i++) {
            cardButtons[i] = new CardButton(HearthStone.baseCards.get(6).copy(),
                    DefaultSizes.bigCardWidth,
                    DefaultSizes.bigCardHeight);
            cardButtons[i].setBounds(i * dis, 0, DefaultSizes.bigCardWidth, DefaultSizes.bigCardHeight);
            add(cardButtons[i]);
        }
        configPanel();
    }

    private void addCard(Card card){ }

    private void removeCard(Card card){ }

    private void configPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(DefaultSizes.bigScroll, DefaultSizes.marketListHeight));
        setBackground(new Color(0, 0, 0, 150));
        setVisible(true);
    }
}
