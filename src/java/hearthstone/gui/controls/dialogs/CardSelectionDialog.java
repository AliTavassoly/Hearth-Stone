package hearthstone.gui.controls.dialogs;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.buttons.CardButtonI;
import hearthstone.gui.controls.panels.ImagePanel;
import hearthstone.logic.models.card.Card;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardSelectionDialog extends HSDialog {
    protected static final int extraPassiveX = 100;
    protected static final int extraPassiveY = 50;

    // statics ended

    private Card selectedCard;
    private ArrayList<Card> cards;
    private ArrayList<CardButtonI> buttons;
    private ImagePanel backgroundPanel;

    private int width, height;

    private JFrame frame;

    public CardSelectionDialog(JFrame frame,
                         ArrayList<Card> cards){
        super(frame, cards.size() * SizeConfigs.medCardWidth + extraPassiveX,
                SizeConfigs.medCardHeight + extraPassiveY);

        this.width = cards.size() * SizeConfigs.medCardWidth + extraPassiveX;
        this.height = SizeConfigs.medCardHeight + extraPassiveY;

        this.frame = frame;
        this.cards = cards;

        buttons = new ArrayList<>();

        configDialog();

        makePassives();

        layoutComponent();
    }

    private void configDialog(){
        getRootPane().setOpaque(false);
        setBackground(new Color(0, 0, 0, 100));
        backgroundPanel = new ImagePanel("dialog_background.png",
                width, height);
        setContentPane(backgroundPanel);
    }

    private void makePassives(){
        for(Card card : cards){
            CardButtonI cardButton = new CardButtonI(
                    card,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight,
                    -1);

            buttons.add(cardButton);
            cardButton.addActionListener(actionEvent -> {
                selectedCard = card;
                setVisible(false);
                dispose();
            });
        }
    }

    private void layoutComponent(){
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        grid.gridy = 0;
        for(int i = 0; i < buttons.size(); i++){
            CardButtonI cardButton =  buttons.get(i);
            grid.gridx = i;
            backgroundPanel.add(cardButton, grid);
        }
    }

    public Card getCard(){
        setVisible(true);
        return selectedCard;
    }
}
