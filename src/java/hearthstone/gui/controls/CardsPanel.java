package hearthstone.gui.controls;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.logic.models.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardsPanel extends JPanel {
    private ArrayList<Card> cards;

    public CardsPanel() {
        int dis = 210;
        CardButton [] cardButtons = new CardButton[10];
        for(int i = 0; i < 10; i++) {
            cardButtons[i] = new CardButton(HearthStone.baseCards.get(6).copy(),
                    DefaultSizes.bigCardWidth,
                    DefaultSizes.bigCardHeight);
            cardButtons[i].setBounds(i * dis, -30, DefaultSizes.bigCardWidth, DefaultSizes.bigCardHeight);
            add(cardButtons[i]);
        }
        configPanel();
    }

    private void addCard(Card card){
        cards.add(card);
    }

    private void removeCard(Card card){
        cards.remove(card);
    }

    private void configPanel() {
        setLayout(null);
        setBackground(new Color(0, 0, 0, 150));
        setVisible(true);
    }
}
