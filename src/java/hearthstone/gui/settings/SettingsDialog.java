package hearthstone.gui.settings;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsDialog extends JDialog {
    private ImageButton volumeUp, volumeDown;
    private JLabel volumeUpLabel, volumeDownLabel;
    private ImagePanel backgroundPanel;

    private ImageButton saveButton, cancelButton;

    private int volumeChange;

    private int width, height;

    public SettingsDialog(JFrame frame, int width, int height) {
        super(frame);
        this.width = width;
        this.height = height;

        volumeChange = 0;

        configDialog();

        makeButtons();

        makeLabels();

        makeVolumesButtons();

        layoutComponent();

        setVisible(true);
    }

    public void configDialog() {
        setSize(new Dimension(width, height));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        setModal(true);
        setResizable(false);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        backgroundPanel = new ImagePanel("dialog_background.png", width, height);
        backgroundPanel.setOpaque(false);
        setContentPane(backgroundPanel);

        Image cursorImage = null;
        try{
            cursorImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/cursor.png"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point(0, 0), "customCursor");
        setCursor(customCursor);
    }

    public void makeButtons() {
        saveButton = new ImageButton("save", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                SizeConfigs.medButtonWidth, SizeConfigs.medButtonHeight);

        cancelButton = new ImageButton("cancel", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                SizeConfigs.medButtonWidth, SizeConfigs.medButtonHeight);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                CredentialsFrame.getInstance().volumeChange(volumeChange);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                CredentialsFrame.getInstance().volumeChange(-volumeChange);
                dispose();
            }
        });
    }

    public void makeVolumesButtons(){
        volumeDown = new ImageButton("/icons/volume_down.png",
                "/icons/volume_down_active.png",
                SizeConfigs.iconHeight, SizeConfigs.iconWidth);
        volumeDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                volumeChange -= 3;
                CredentialsFrame.getInstance().volumeChange(-3);
            }
        });

        volumeUp = new ImageButton("/icons/volume_up.png",
                "/icons/volume_up_active.png",
                SizeConfigs.iconHeight, SizeConfigs.iconWidth);
        volumeUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                volumeChange += 3;
                CredentialsFrame.getInstance().volumeChange(+3);
            }
        });
    }

    public void makeLabels(){
        volumeDownLabel = new JLabel("Volume Down");
        volumeDownLabel.setFont(GameFrame.getInstance().getCustomFont(0, 20));
        volumeDownLabel.setForeground(new Color(69, 28, 28));

        volumeUpLabel = new JLabel("Volume Up");
        volumeUpLabel.setFont(GameFrame.getInstance().getCustomFont(0, 20));
        volumeUpLabel.setForeground(new Color(69, 28, 28));
    }

    public void layoutComponent() {
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        // first row
        grid.gridy = 0;

        grid.gridx = 0;
        add(volumeUpLabel, grid);

        grid.gridx = 1;
        add(volumeUp, grid);

        // second row
        grid.gridy = 1;

        grid.gridx = 0;
        grid.insets = new Insets(30, 0, 0, 0);
        add(volumeDownLabel, grid);

        grid.gridx = 1;
        grid.insets = new Insets(30, 0, 0, 0);
        add(volumeDown, grid);

        // third row
        grid.gridy = 2;

        grid.gridx = 0;
        grid.insets = new Insets(30, 0, 0, 0);
        add(saveButton, grid);

        grid.gridx = 1;
        grid.insets = new Insets(30, 0, 0, 0);
        add(cancelButton, grid);
    }
}