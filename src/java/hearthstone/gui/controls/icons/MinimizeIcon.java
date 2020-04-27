package hearthstone.gui.controls.icons;

import hearthstone.HearthStone;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.game.GameFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinimizeIcon extends ImageButton {
    public MinimizeIcon(String normalPath, String activePath,
                        int width, int height) {
        super(normalPath, activePath, width, height);
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
                    System.out.println(e.getMessage());
                }
                GameFrame.getInstance().setState(Frame.ICONIFIED);
                GameFrame.getInstance().setState(Frame.NORMAL);
            }
        });
    }
}
