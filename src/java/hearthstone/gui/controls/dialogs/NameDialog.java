package hearthstone.gui.controls.dialogs;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.buttons.ImageButton;
import hearthstone.gui.controls.fields.TextField;
import hearthstone.gui.controls.panels.ImagePanel;
import hearthstone.gui.game.GameFrame;
import hearthstone.util.FontType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NameDialog extends HSDialog {
    private JLabel message;
    private TextField nameField;
    private ImageButton okButton, cancelButton;
    private String clicked;

    private String text;
    private int width, height;

    public NameDialog(JFrame frame, String text, int width, int height){
        super(frame, width, height);
        this.text = text;
        this.width = width;
        this.height = height;

        configDialog();

        makeButtons();

        makeFields();

        makeLabels();

        layoutComponent();
    }

    private void configDialog() {
        ImagePanel backgroundPanel = new ImagePanel("dialog_background.png",
                width, height);
        backgroundPanel.setOpaque(false);
        setContentPane(backgroundPanel);
    }

    private void makeLabels(){
        message = new JLabel(text);
        message.setForeground(new Color(69, 28, 28));
        message.setFont(GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, 20));
    }

    private void makeFields(){
        nameField = new TextField(10);
    }

    private void makeButtons(){
        okButton = new ImageButton("ok", "buttons/green_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                SizeConfigs.smallButtonWidth, SizeConfigs.smallButtonHeight);

        cancelButton = new ImageButton("cancel", "buttons/red_background.png", 0,
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

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nameField.setText(null);
                setVisible(false);
                dispose();
            }
        });
    }

    private void layoutComponent(){
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        // first row
        grid.gridy = 0;

        grid.gridx = 0;
        add(message, grid);

        grid.gridx = 1;
        add(nameField, grid);

        // second row
        grid.gridy = 1;

        grid.gridx = 0;
        grid.insets = new Insets(30, 0, 0, 0);
        add(okButton, grid);

        grid.gridx = 1;
        grid.insets = new Insets(30, 0, 0, 0);
        add(cancelButton, grid);
    }

    public String getValue() {
        setVisible(true);
        return nameField.getText();
    }
}