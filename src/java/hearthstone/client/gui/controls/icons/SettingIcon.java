package hearthstone.client.gui.controls.icons;

import hearthstone.client.network.HSClient;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.settings.SettingsPanel;

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
                    if (HSClient.currentAccount != null)
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
