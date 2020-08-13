package hearthstone.client.gui.controls.panels;

import hearthstone.client.gui.controls.buttons.GameInfoButton;
import hearthstone.models.GameInfo;
import hearthstone.shared.GUIConfigs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameInfosPanel extends JPanel {
    private ArrayList<GameInfo> gameInfos;
    private ArrayList<JPanel> panels;
    private ArrayList<GameInfoButton> infoButtons;
    private int infoWidth, infoHeight;

    private Color color;

    private int startX = 10;
    private int startY = 10;
    private int disY = 10;

    public GameInfosPanel(ArrayList<GameInfo> gameInfos, ArrayList<JPanel> panels,
                          int infoWidth, int infoHeight) {
        this.gameInfos = gameInfos;
        this.panels = panels;
        this.infoWidth = infoWidth;
        this.infoHeight = infoHeight;

        infoButtons = new ArrayList<>();

        for (GameInfo gameInfo : this.gameInfos) {
            GameInfoButton infoButton = new GameInfoButton(gameInfo, infoWidth, infoHeight);
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
            JPanel panel = panels.get(i);

            button.setBounds(startX, startY + i * disY,
                    infoWidth, infoHeight);
            if (panel != null) {
                panel.setBounds(startX + infoWidth + 10, startY + i * disY + 7,
                        (int) panel.getPreferredSize().getWidth(),
                        (int) panel.getPreferredSize().getHeight());
            }

            add(button);
            if (panel != null)
                add(panel);
        }
    }
}
