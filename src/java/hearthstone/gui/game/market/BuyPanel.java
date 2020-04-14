package hearthstone.gui.game.market;

import hearthstone.HearthStone;
import hearthstone.gui.CardButton;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.ImageButton;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class BuyPanel extends JPanel {

    public BuyPanel() {
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

    private void configPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(DefaultSizes.bigScroll, DefaultSizes.marketListHeight));
        setBackground(new Color(0, 0, 0, 150));
        setVisible(true);
    }
}
