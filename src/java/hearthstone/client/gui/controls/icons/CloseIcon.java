package hearthstone.client.gui.controls.icons;

import hearthstone.HearthStone;
import hearthstone.client.data.GUIConfigs;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.dialogs.SureDialog;
import hearthstone.client.gui.game.GameFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseIcon extends ImageButton {
    public CloseIcon(String normalPath, String hoveredPath,
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
                                "Close_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SureDialog sureDialog = new SureDialog(GameFrame.getInstance(), "Are you sure you want to Exit Game ?",
                        GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);
                boolean sure = sureDialog.getValue();
                if (sure) {
                    System.exit(0);
                }
            }
        });
    }
}