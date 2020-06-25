package hearthstone.gui.controls.dialogs;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.ImagePanel;
import hearthstone.gui.game.GameFrame;
import hearthstone.util.FontType;
import hearthstone.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorDialog extends MyDialog {
    private JLabel message;
    private ImageButton okButton;

    private int width, height;
    private String text;

    public ErrorDialog(JFrame frame, String text, int width, int height){
        super(frame, width, height);
        this.width = width;
        this.height = height;
        this.text = text;

        playError();

        configDialog();

        makeButtons();

        makeLabels();

        layoutComponent();

        setVisible(true);
    }

    private void configDialog(){
        ImagePanel backgroundPanel = new ImagePanel("dialog_background.png", width, height);
        backgroundPanel.setOpaque(false);
        setContentPane(backgroundPanel);
    }

    private void makeLabels(){
        message = new JLabel(text);
        message.setForeground(new Color(69, 28, 28));
        message.setFont(GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, 20));
    }

    private void makeButtons(){
        okButton = new ImageButton("OK", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        });
    }

    private void playError() {
        try {
            SoundPlayer soundPlayer = new SoundPlayer("/sounds/error.wav");
            soundPlayer.playOnce();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void layoutComponent(){
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        // first row
        grid.gridy = 0;
        grid.gridx = 0;
        add(message, grid);

        // second row
        grid.gridy = 1;
        grid.gridx = 0;
        grid.insets = new Insets(30, 0, 0, 0);
        add(okButton, grid);
    }
}