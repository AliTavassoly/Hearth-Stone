package hearthstone.gui.controls.icons;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.SureDialog;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.collection.DeckSelection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackIcon extends ImageButton {
    private JPanel panel;

    public BackIcon(String normalPath, String activePath,
                    int width, int height, JPanel panel) {
        super(normalPath, activePath, width, height);
        this.panel = panel;
        configIcon();
    }

    private void configIcon() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (HearthStone.currentAccount != null)
                        hearthstone.util.Logger.saveLog("Click_icon",
                                "Back_button");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                        panel);
            }
        });
    }
}
