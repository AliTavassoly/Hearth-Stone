package hearthstone.client.gui.game.waitingpanels;

import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.play.PlaySelectionPanel;
import hearthstone.client.network.ClientMapper;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class WaitForOpponentPanel extends JPanel {
    public static BufferedImage backgroundImage;
    private ImageButton cancelButton;

    private final int iconX = 20;
    private final int iconY = 20;

    public WaitForOpponentPanel(){
        configPanel();

        makeButtons();

        layoutComponent();
    }

    private void configPanel() {
        setSize(new Dimension(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight));
        setLayout(null);
    }

    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/wait_background.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void makeButtons() {
        cancelButton = new ImageButton("icons/cancel.png", "icons/cancel_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ClientMapper.onlineGameCancelRequest();
                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), PlaySelectionPanel.makeInstance());
            }
        });
    }

    private void layoutComponent() {
        cancelButton.setBounds(iconX, iconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(cancelButton);
    }
}
