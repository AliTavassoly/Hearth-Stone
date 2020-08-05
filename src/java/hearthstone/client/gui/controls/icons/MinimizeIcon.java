package hearthstone.client.gui.controls.icons;

import hearthstone.HearthStone;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.game.GameFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinimizeIcon extends ImageButton {
    public MinimizeIcon(String normalPath, String hoveredPath,
                        int width, int height) {
        super(normalPath, hoveredPath, width, height);
        configIcon();
    }

    private void configIcon() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (HearthStone.currentAccount != null)
                        hearthstone.util.Logger.saveLog("Click_icon",
                                "Minimize_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GameFrame.getInstance().setState(Frame.ICONIFIED);
                GameFrame.getInstance().setState(Frame.NORMAL);
            }
        });
    }
}
