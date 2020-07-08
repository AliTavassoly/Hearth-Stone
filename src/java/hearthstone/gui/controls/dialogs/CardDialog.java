package hearthstone.gui.controls.dialogs;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.buttons.CardButton;
import hearthstone.gui.controls.buttons.ImageButton;
import hearthstone.gui.controls.panels.ImagePanel;
import hearthstone.logic.models.card.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CardDialog extends HSDialog {
    protected static final int extraX = 80;
    protected static final int extraY = 150;

    // statics ended

    private ArrayList<Card> selectedCards;
    private ArrayList<Card> cards;
    private ArrayList<CardButton> buttons;
    private ImagePanel backgroundPanel;

    private ImageButton okButton;

    private int width, height;

    public CardDialog(JFrame frame,
                      ArrayList<Card> cards) {
        super(frame, cards.size() * SizeConfigs.medCardWidth + extraX,
                SizeConfigs.medCardHeight + extraY);

        this.width = cards.size() * SizeConfigs.medCardWidth + extraX;
        this.height = SizeConfigs.medCardHeight + extraY;

        this.cards = cards;

        buttons = new ArrayList<>();
        selectedCards = new ArrayList<>();

        configDialog();

        makeCards();

        makeOkButton();

        layoutComponent();
    }

    private void makeOkButton() {
        okButton = new ImageButton("OK", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                20, 0,
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(CardButton button: buttons){
                    if(button.isMark()){
                        selectedCards.add(button.getCard());
                    }
                }
                setVisible(false);
                dispose();
            }
        });
    }

    private void configDialog() {
        getRootPane().setOpaque(false);
        setBackground(new Color(0, 0, 0, 100));
        backgroundPanel = new ImagePanel("dialog_background.png",
                width, height);
        setContentPane(backgroundPanel);
        backgroundPanel.setPreferredSize(new Dimension(width, height));
    }

    private void makeCards() {
        for (Card card : cards) {
            CardButton cardButton = new CardButton(
                    card,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight,
                    -1, true);

            buttons.add(cardButton);
        }
    }

    private void layoutComponent() {
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        grid.gridy = 0;
        for (int i = 0; i < buttons.size(); i++) {
            CardButton cardButton = buttons.get(i);
            grid.gridx = i;
            backgroundPanel.add(cardButton, grid);
        }

        grid.gridy = 1;
        grid.gridx = 0;
        grid.gridwidth = cards.size();
        backgroundPanel.add(okButton, grid);

        //backgroundPanel.add(okButton);
        //okButton.setBounds(width / 2, height / 2, SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);
    }

    public ArrayList<Card> getCards() {
        setVisible(true);
        return selectedCards;
    }
}
