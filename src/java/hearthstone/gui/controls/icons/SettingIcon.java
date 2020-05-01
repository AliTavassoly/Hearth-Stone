package hearthstone.gui.controls.icons;

import hearthstone.HearthStone;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.settings.SettingsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingIcon extends ImageButton {
    public SettingIcon(String normalPath, String activePath,
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
                                "Setting_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SettingsDialog settingsDialog = new SettingsDialog(GameFrame.getInstance(),
                        SizeConfigs.settingsWidth, SizeConfigs.settingsHeight);
            }
        });
    }
}
