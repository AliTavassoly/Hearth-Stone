package hearthstone.client.gui.controls.icons;

import hearthstone.client.network.HSClient;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.game.GameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackIcon extends ImageButton {
    private JPanel panel;

    public BackIcon(String normalPath, String hoveredPath,
                    int width, int height, JPanel panel) {
        super(normalPath, hoveredPath, width, height);
        this.panel = panel;
        configIcon();
    }

    private void configIcon() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (HSClient.currentAccount != null)
                        hearthstone.util.Logger.saveLog("Click_icon",
                                "Back_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                        panel);
            }
        });
    }
}
