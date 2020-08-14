package hearthstone.client.gui.controls.panels;

import hearthstone.client.gui.controls.buttons.WatcherButton;
import hearthstone.client.network.ClientMapper;
import hearthstone.client.network.HSClient;
import hearthstone.models.WatcherInfo;
import hearthstone.shared.GUIConfigs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WatchersPanel extends JPanel {
    private ArrayList<WatcherInfo> watcherInfos;
    private ArrayList<WatcherButton> watcherButtons;
    private int infoWidth, infoHeight;

    private int startX = 0;
    private int startY = 0;
    private int disY = 5;

    public WatchersPanel(ArrayList<WatcherInfo> watcherInfos,
                         int infoWidth, int infoHeight) {
        this.watcherInfos = watcherInfos;
        this.infoWidth = infoWidth;
        this.infoHeight = infoHeight;

        configPanel();

        makeList();

        layoutComponent();
    }

    private void configPanel() {
        setLayout(null);
        setBackground(new Color(0, 0, 0, 120));
        setPreferredSize(
                new Dimension(GUIConfigs.watchersInfoListWidth, watcherInfos.size() * disY + 10));
        setOpaque(false);
        setVisible(true);
    }

    private void  makeList(){
        watcherButtons = new ArrayList<>();

        for (WatcherInfo watcher : watcherInfos) {
            WatcherButton infoButton = new WatcherButton(watcher, infoWidth, infoHeight);

            infoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    ClientMapper.kickWatcherRequest(watcher.getUsername());
                }
            });

            watcherButtons.add(infoButton);
        }

        disY += infoHeight;
    }

    private void layoutComponent() {
        for (int i = 0; i < watcherInfos.size(); i++) {
            WatcherButton button = this.watcherButtons.get(i);

            button.setBounds(startX, startY + i * disY,
                    infoWidth, infoHeight);

            add(button);
        }
    }

    public void updateWatchers(ArrayList<WatcherInfo> watchers) {
        this.watcherInfos = watchers;

        makeList();

        layoutComponent();

        revalidate();
        repaint();
    }
}
