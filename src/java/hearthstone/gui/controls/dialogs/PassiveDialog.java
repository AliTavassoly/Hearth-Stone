package hearthstone.gui.controls.dialogs;

import hearthstone.HearthStone;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.controls.PassiveButton;
import hearthstone.logic.models.Passive;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PassiveDialog extends MyDialog {
    private Passive selectedPassive;
    private ArrayList<Passive> passives;
    private ArrayList<PassiveButton> passiveButtons;
    private ImagePanel backgroundPanel;

    private int width, height;
    private JFrame frame;

    public PassiveDialog(JFrame frame, int width, int height,
                         ArrayList<Integer> passivesId){
        super(frame, width, height);
        this.frame = frame;
        this.width = width;
        this.height = height;

        passives = new ArrayList<>();
        for(int id : passivesId)
            passives.add(HearthStone.basePassives.get(id));

        passiveButtons = new ArrayList<>();

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
        for(Passive passive : passives){
            PassiveButton passiveButton = new PassiveButton(
                    passive,
                    SizeConfigs.medCardWidth,
                    SizeConfigs.medCardHeight);
            passiveButtons.add(passiveButton);
            passiveButton.addActionListener(actionEvent -> {
                selectedPassive = passive;
                setVisible(false);
                dispose();
            });
        }
    }

    private void layoutComponent(){
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        grid.gridy = 0;
        for(int i = 0; i < passiveButtons.size(); i++){
            PassiveButton passiveButton =  passiveButtons.get(i);
            grid.gridx = i;
            backgroundPanel.add(passiveButton, grid);
        }
    }

    public Passive getPassive(){
        setVisible(true);
        return selectedPassive;
    }
}
