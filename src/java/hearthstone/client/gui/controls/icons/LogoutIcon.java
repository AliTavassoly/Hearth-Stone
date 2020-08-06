package hearthstone.client.gui.controls.icons;

import hearthstone.HearthStone;
import hearthstone.client.ClientMapper;
import hearthstone.client.data.GUIConfigs;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.dialogs.SureDialog;
import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.util.HearthStoneException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutIcon extends ImageButton {
    public LogoutIcon(String normalPath, String hoveredPath,
                      int width, int height) {
        super(normalPath, hoveredPath, width, height);
        configIcon();
    }

    private void configIcon() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ClientMapper.logoutRequest();
                //try {
                    /*try {
                        if (HearthStone.currentAccount != null)
                            hearthstone.util.Logger.saveLog("Click_icon",
                                    "Logout_button");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(), "Are you sure you want to logout ?",
                            GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);
                    boolean sure = sureDialog.getValue();
                    if (sure) {
                        HearthStone.logout();
                        GameFrame.getInstance().setVisible(false);
                        GameFrame.getInstance().dispose();
                        CredentialsFrame.getNewInstance().setVisible(true);
                    }*/
                /*} catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage()
                                        + "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) {
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }*/
            }
        });
    }
}
