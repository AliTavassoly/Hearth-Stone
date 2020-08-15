package hearthstone.client.gui.controls.icons;

import hearthstone.client.network.HSClient;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.dialogs.SureDialog;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.shared.GUIConfigs;

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
