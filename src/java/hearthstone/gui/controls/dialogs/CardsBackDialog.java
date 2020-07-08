package hearthstone.gui.controls.dialogs;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.buttons.CardBackButton;
import hearthstone.gui.controls.panels.ImagePanel;
import hearthstone.logic.GameConfigs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardsBackDialog extends HSDialog {
    protected static final int extraPassiveX = 60;
    protected static final int extraPassiveY = 50;

    // statics ended

    private int selectedBackId;
    private final ArrayList<CardBackButton> buttons;
    private ImagePanel backgroundPanel;

    private int width, height;

    private JFrame frame;

    public CardsBackDialog(JFrame frame){
        super(frame, GameConfigs.initialCardsBack * SizeConfigs.medCardWidth + extraPassiveX,
                SizeConfigs.medCardHeight + extraPassiveY);

        this.width = GameConfigs.initialCardsBack * SizeConfigs.medCardWidth + extraPassiveX;
        this.height = SizeConfigs.medCardHeight + extraPassiveY;

        this.frame = frame;

        buttons = new ArrayList<>();

        configDialog();

        makeBacks();

        layoutComponent();
    }

    private void configDialog(){
        getRootPane().setOpaque(false);
        setBackground(new Color(0, 0, 0, 100));
        backgroundPanel = new ImagePanel("dialog_background.png",
                width, height);
        setContentPane(backgroundPanel);
    }

    private void makeBacks(){
        for(int i = 0; i < GameConfigs.initialCardsBack; i++){
            CardBackButton cardBackButton = new CardBackButton(i,
                    SizeConfigs.medCardWidth, SizeConfigs.medCardHeight);

            cardBackButton.addActionListener(actionEvent -> {
                selectedBackId = cardBackButton.getId();
                setVisible(false);
                dispose();
            });

            buttons.add(cardBackButton);
        }
    }

    private void layoutComponent(){
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        grid.gridy = 0;
        for(int i = 0; i < buttons.size(); i++){
            CardBackButton cardBackButton =  buttons.get(i);
            grid.gridx = i;
            backgroundPanel.add(cardBackButton, grid);
        }
    }

    public int getId(){
        setVisible(true);
        return selectedBackId;
    }
}
