package hearthstone.client.gui.controls.dialogs;

import hearthstone.client.data.ClientData;
import hearthstone.client.gui.controls.buttons.PassiveButton;
import hearthstone.client.gui.controls.panels.ImagePanel;
import hearthstone.models.passive.Passive;
import hearthstone.server.network.HSServer;
import hearthstone.shared.GUIConfigs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PassiveDialog extends HSDialog {
    protected static final int extraPassiveX = 60;
    protected static final int extraPassiveY = 50;

    // statics ended

    private Passive selectedPassive;
    private ArrayList<Passive> passives;
    private ArrayList<PassiveButton> passiveButtons;
    private ImagePanel backgroundPanel;

    private int width, height;

    private JFrame frame;

    public PassiveDialog(JFrame frame,
                         ArrayList<Integer> passivesId){
        super(frame, passivesId.size() * GUIConfigs.medCardWidth + extraPassiveX,
                GUIConfigs.medCardHeight + extraPassiveY);

        this.width = passivesId.size() * GUIConfigs.medCardWidth + extraPassiveX;
        this.height = GUIConfigs.medCardHeight + extraPassiveY;


        this.frame = frame;

        passives = new ArrayList<>();

        for(int id : passivesId) {
            passives.add(ClientData.basePassives.get(id).copy());
        }

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
                    GUIConfigs.medCardWidth,
                    GUIConfigs.medCardHeight);
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
