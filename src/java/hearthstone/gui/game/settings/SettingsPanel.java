package hearthstone.gui.game.settings;

import hearthstone.HearthStone;
import hearthstone.data.Data;
import hearthstone.data.DataBase;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.dialogs.CardsBackDialog;
import hearthstone.gui.controls.dialogs.NameDialog;
import hearthstone.gui.controls.dialogs.PasswordDialog;
import hearthstone.gui.controls.dialogs.SureDialog;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.util.Logger;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SettingsPanel extends JPanel {
    private ImageButton backButton, logoutButton, minimizeButton, closeButton;
    private ImageButton changeCardsBackground, changePassword, changeName, volume, deletedAccount;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int buttonDisY = 100;
    private final int startButtonY = SizeConfigs.gameFrameHeight / 2 - 100;

    public SettingsPanel() {
        configPanel();

        makeIcons();

        makeButtons();

        layoutIcons();

        layoutButtons();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/settings_background.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
    }

    private void makeButtons() {
        changePassword = new ImageButton("Change Password", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        changeName = new ImageButton("Change Name", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        changeCardsBackground = new ImageButton("Change Cards Back", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        volume = new ImageButton("Volume", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        deletedAccount = new ImageButton("Delete Account", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        // listeners
        changePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                PasswordDialog passwordDialog = new PasswordDialog(GameFrame.getInstance(),
                        "Choose you new name: ", SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);

                String newPassword = passwordDialog.getValue();

                if (newPassword.length() > 0) {
                    Data.changePassword(HearthStone.currentAccount.getUsername(),
                            newPassword);
                } else {
                    return;
                }

                try {
                    DataBase.save();
                    Logger.saveLog("Settings changed",
                            "password changed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        changeName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                NameDialog nameDialog = new NameDialog(GameFrame.getInstance(),
                        "Choose you new name: ", SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);

                String newName = nameDialog.getValue();

                if (newName.length() > 0)
                    HearthStone.currentAccount.setName(newName);
                else
                    return;

                try {
                    DataBase.save();
                    Logger.saveLog("Settings changed",
                            "name changed to: " + HearthStone.currentAccount.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        changeCardsBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardsBackDialog cardsBackDialog = new CardsBackDialog(GameFrame.getInstance());

                HearthStone.currentAccount.setCardsBackId(cardsBackDialog.getId());

                try {
                    DataBase.save();
                    Logger.saveLog("Settings changed",
                            "cards back id changed to: " + HearthStone.currentAccount.getCardsBackId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        volume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                VolumesDialog volumesDialog = new VolumesDialog(GameFrame.getInstance());
            }
        });

        deletedAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(), "Are you sure you want to delete you account ?",
                            SizeConfigs.dialogWidth, SizeConfigs.dialogHeight);

                    boolean sure = sureDialog.getValue();

                    if (sure) {
                        Data.deleteAccount(HearthStone.currentAccount.getUsername());
                        GameFrame.getInstance().setVisible(false);
                        GameFrame.getInstance().dispose();
                        CredentialsFrame.getNewInstance().setVisible(true);

                        Logger.saveLog("Delete account",
                                "account deleted");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void configPanel() {
        setSize(new Dimension(SizeConfigs.gameFrameWidth, SizeConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void layoutIcons() {
        backButton.setBounds(iconX, startIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(closeButton);
    }

    private void layoutButtons() {
        changeName.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY - buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(changeName);

        changePassword.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(changePassword);

        changeCardsBackground.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY + buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(changeCardsBackground);

        volume.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY + 2 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(volume);

        deletedAccount.setBounds(SizeConfigs.gameFrameWidth / 2 - SizeConfigs.largeButtonWidth / 2,
                startButtonY + 3 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(deletedAccount);
    }
}
