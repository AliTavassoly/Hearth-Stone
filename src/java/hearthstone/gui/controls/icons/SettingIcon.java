package hearthstone.gui.controls.icons;

import hearthstone.HearthStone;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.settings.SettingsPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingIcon extends ImageButton {
    public SettingIcon(String normalPath, String hoveredPath,
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
                                "Setting_button");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new SettingsPanel());
            }
        });
    }
}
