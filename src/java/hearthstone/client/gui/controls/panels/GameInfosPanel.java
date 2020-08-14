package hearthstone.client.gui.controls.panels;

import hearthstone.client.gui.controls.buttons.GameInfoButton;
import hearthstone.client.network.ClientMapper;
import hearthstone.client.network.HSClient;
import hearthstone.models.GameInfo;
import hearthstone.shared.GUIConfigs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameInfosPanel extends JPanel {
    private ArrayList<GameInfo> gameInfos;
    private ArrayList<GameInfoButton> infoButtons;
    private int infoWidth, infoHeight;

    private int startX = 10;
    private int startY = 10;
    private int disY = 10;

    public GameInfosPanel(ArrayList<GameInfo> gameInfos,
                          int infoWidth, int infoHeight) {
        this.gameInfos = gameInfos;
        this.infoWidth = infoWidth;
        this.infoHeight = infoHeight;

        infoButtons = new ArrayList<>();

        for (GameInfo gameInfo : this.gameInfos) {
            GameInfoButton infoButton = new GameInfoButton(gameInfo, infoWidth, infoHeight);

            infoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    ClientMapper.viewRequest(gameInfo.getPlayer0(), HSClient.currentAccount.getUsername());
                }
            });

            infoButtons.add(infoButton);
        }

        disY += infoHeight;

        configPanel();

        layoutComponent();
    }

    private void configPanel() {
        setLayout(null);
        setBackground(new Color(0, 0, 0, 120));
        setPreferredSize(
                new Dimension(GUIConfigs.statusListWidth, gameInfos.size() * disY + 10));
        setOpaque(false);
        setVisible(true);
    }

    private void layoutComponent() {
        for (int i = 0; i < gameInfos.size(); i++) {
            GameInfoButton button = infoButtons.get(i);

            button.setBounds(startX, startY + i * disY,
                    infoWidth, infoHeight);
            add(button);
        }
    }
}
