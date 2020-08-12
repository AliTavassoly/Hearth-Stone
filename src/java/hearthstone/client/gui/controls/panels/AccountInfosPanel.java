package hearthstone.client.gui.controls.panels;

import hearthstone.client.gui.controls.buttons.AccountInfoButton;
import hearthstone.client.gui.controls.buttons.DeckButton;
import hearthstone.models.AccountInfo;
import hearthstone.models.Deck;
import hearthstone.shared.GUIConfigs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AccountInfosPanel extends JPanel{
    private ArrayList<AccountInfo> accountInfos;
    private ArrayList<JPanel> panels;
    private ArrayList<AccountInfoButton> infoButtons;
    private int infoWidth, infoHeight;

    private int startX = 10;
    private int startY = 10;
    private int disY = 10;

    public AccountInfosPanel(ArrayList<AccountInfo> accountInfos, ArrayList<JPanel> panels,
                      int infoWidth, int infoHeight) {
        this.accountInfos = accountInfos;
        this.panels = panels;
        this.infoWidth = infoWidth;
        this.infoHeight = infoHeight;

        infoButtons = new ArrayList<>();

        for (AccountInfo accountInfo : this.accountInfos) {
            AccountInfoButton infoButton = new AccountInfoButton(accountInfo, infoWidth, infoHeight);
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
                new Dimension(GUIConfigs.statusListWidth, accountInfos.size() * disY + 10));
        setOpaque(false);
        setVisible(true);
    }

    private void layoutComponent() {
        for (int i = 0; i < accountInfos.size(); i++) {
            AccountInfoButton button = infoButtons.get(i);
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
