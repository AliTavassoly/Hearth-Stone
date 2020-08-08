package hearthstone.client.gui.game.settings;

import hearthstone.client.network.ClientMapper;
import hearthstone.client.network.HSClient;
import hearthstone.server.data.ServerData;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.dialogs.CardsBackDialog;
import hearthstone.client.gui.controls.dialogs.NameDialog;
import hearthstone.client.gui.controls.dialogs.PasswordDialog;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.shared.GUIConfigs;
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
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int buttonDisY = 100;
    private final int startButtonY = GUIConfigs.gameFrameHeight / 2 - 100;

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
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
    }

    private void makeButtons() {
        changePassword = new ImageButton("Change Password", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        changeName = new ImageButton("Change Name", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        changeCardsBackground = new ImageButton("Change Cards Back", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        volume = new ImageButton("Volume", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        deletedAccount = new ImageButton("Delete Account", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        // listeners
        changePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                PasswordDialog passwordDialog = new PasswordDialog(GameFrame.getInstance(),
                        "Choose you new name: ", GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);

                String newPassword = passwordDialog.getValue();

                if (newPassword.length() > 0) {
                    ServerData.changePassword(HSClient.currentAccount.getUsername(),
                            newPassword);
                } else {
                    return;
                }
            }
        });

        changeName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                NameDialog nameDialog = new NameDialog(GameFrame.getInstance(),
                        "Choose you new name: ", GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);

                String newName = nameDialog.getValue();

                if (newName.length() > 0)
                    HSClient.currentAccount.setName(newName);
                else
                    return;
            }
        });

        changeCardsBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardsBackDialog cardsBackDialog = new CardsBackDialog(GameFrame.getInstance());

                HSClient.currentAccount.setCardsBackId(cardsBackDialog.getId());
            }
        });

        volume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                VolumesDialog volumesDialog = new VolumesDialog(GameFrame.getInstance());
            }
        });

        deletedAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                ClientMapper.deleteAccountRequest();
                /*try {
                    SureDialog sureDialog = new SureDialog(GameFrame.getInstance(), "Are you sure you want to delete you account ?",
                            GUIConfigs.dialogWidth, GUIConfigs.dialogHeight);

                    boolean sure = sureDialog.getValue();

                    if (sure) {
                        ServerData.deleteAccount(HSClient.currentAccount.getUsername());
                        GameFrame.getInstance().setVisible(false);
                        GameFrame.getInstance().dispose();
                        CredentialsFrame.getNewInstance().setVisible(true);

                        Logger.saveLog("Delete account",
                                "account deleted");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });
    }

    private void configPanel() {
        setSize(new Dimension(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void layoutIcons() {
        backButton.setBounds(iconX, startIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(closeButton);
    }

    private void layoutButtons() {
        changeName.setBounds(GUIConfigs.gameFrameWidth / 2 - GUIConfigs.largeButtonWidth / 2,
                startButtonY - buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(changeName);

        changePassword.setBounds(GUIConfigs.gameFrameWidth / 2 - GUIConfigs.largeButtonWidth / 2,
                startButtonY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(changePassword);

        changeCardsBackground.setBounds(GUIConfigs.gameFrameWidth / 2 - GUIConfigs.largeButtonWidth / 2,
                startButtonY + buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(changeCardsBackground);

        volume.setBounds(GUIConfigs.gameFrameWidth / 2 - GUIConfigs.largeButtonWidth / 2,
                startButtonY + 2 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(volume);

        deletedAccount.setBounds(GUIConfigs.gameFrameWidth / 2 - GUIConfigs.largeButtonWidth / 2,
                startButtonY + 3 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(deletedAccount);
    }
}
