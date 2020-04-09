package hearthstone.gui.credetials;

import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.credetials.LogisterPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class RegisterPanel extends JPanel {
    private JButton backButton, registerButton;
    private JTextField nameField, userField, passField, passRepField;
    private JLabel nameLabel, userLabel, passLabel, passRepLabel;

    public RegisterPanel(){
        configPanel();

        registerButton = new JButton("register");
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setMargin(new Insets(2, 3 + 30, 2, 3 + 30));

        backButton = new JButton("back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setMargin(new Insets(2, 15 + 30, 2, 15 + 30));

        nameField = new JTextField(10);
        nameField.setBorder(null);

        userField = new JTextField(10);
        userField.setBorder(null);

        passField = new JTextField(10);
        passField.setBorder(null);

        passRepField = new JTextField(10);
        passRepField.setBorder(null);

        nameLabel = new JLabel("name : ");
        nameLabel.setForeground(Color.WHITE);

        userLabel = new JLabel("username : ");
        userLabel.setForeground(Color.WHITE);

        passLabel = new JLabel("password : ");
        passLabel.setForeground(Color.WHITE);

        passRepLabel = new JLabel("repeat password : ");
        passRepLabel.setForeground(Color.WHITE);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().getContentPane().setVisible(false);
                CredentialsFrame.getInstance().setContentPane(new LogisterPanel());
            }
        });

        /*registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().getContentPane().setVisible(false);
                CredentialsFrame.getInstance().setContentPane(new RegisterPanel());
            }
        });*/

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("logisterBG.jpg"));
        } catch (Exception e){
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void configPanel(){
        setVisible(true);
        setBackground(Color.BLUE);
    }

    private void layoutComponent(){
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        // zero row
        grid.gridy = 0;
        grid.gridx = 0;

        // next row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 1;
        grid.insets = new Insets(180, 0, 5, 0);
        add(nameLabel, grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.insets = new Insets(180, 0, 5, 0);
        add(nameField, grid);

        // next row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 1;
        grid.insets = new Insets(5, 0, 5, 0);
        add(userLabel, grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.insets = new Insets(5, 0, 5, 0);
        add(userField, grid);

        // next row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 1;
        grid.insets = new Insets(5, 0, 5, 0);
        add(passLabel, grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.insets = new Insets(5, 0, 5, 0);
        add(passField, grid);

        // next row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 1;
        grid.insets = new Insets(5, 0, 5, 0);
        add(passRepLabel, grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.insets = new Insets(5, 0, 5, 0);
        add(passRepField, grid);

        // next row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 2;
        grid.insets = new Insets(70, 0, 10, 0);
        add(registerButton, grid);

        //next row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 2;
        grid.insets = new Insets(10, 0, 10, 0);
        add(backButton, grid);
    }
}
