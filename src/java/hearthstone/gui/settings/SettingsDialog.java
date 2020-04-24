package hearthstone.gui.settings;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsDialog extends JDialog {
    private ImageButton volumeUp, volumeDown;
    private JLabel volumeUpLabel, volumeDownLabel;

    private ImageButton saveButton, cancelButton;
    private JPanel panel;

    private int width, height;

    public SettingsDialog(JFrame frame, int width, int height) {
        super(frame);
        this.width = width;
        this.height = height;

        configDialog();

        makeButtons();

        makeLabels();

        makeVolumesButtons();

        layoutComponent();
    }

    public void configDialog() {
        setSize(new Dimension(width, height));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        setResizable(false);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        setVisible(true);

        panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 20));
        panel.setPreferredSize(new Dimension(width, height));
        setContentPane(panel);
    }

    public void makeButtons() {
        saveButton = new ImageButton("ok", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

        cancelButton = new ImageButton("cancel", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        });
    }

    public void makeVolumesButtons(){
        volumeDown = new ImageButton("/icons/volume_down.png",
                "/icons/volume_down_active.png",
                DefaultSizes.iconWidth, DefaultSizes.iconHeight);

        volumeUp = new ImageButton("/icons/volume_up.png",
                "/icons/volume_up_active.png",
                DefaultSizes.iconWidth, DefaultSizes.iconHeight);
    }

    public void makeLabels(){
        volumeDownLabel = new JLabel("Volume Down");
        volumeDownLabel.setFont(GameFrame.getInstance().getCustomFont(0, 20));

        volumeDownLabel = new JLabel("Volume Up");
        volumeDownLabel.setFont(GameFrame.getInstance().getCustomFont(0, 20));
    }

    public void layoutComponent() {
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        // first row
        grid.gridy = 0;

        grid.gridx = 0;
        panel.add(volumeUp, grid);

        grid.gridx = 1;
        panel.add(volumeDownLabel, grid);

        // second row
        grid.gridy = 1;

        grid.gridx = 0;
        panel.add(volumeUp, grid);

        grid.gridx = 1;
        panel.add(volumeDown, grid);

        // third row
        grid.gridy = 2;

        grid.gridx = 0;
        panel.add(volumeUp, grid);

        grid.gridx = 1;
        panel.add(volumeDown, grid);
    }
}