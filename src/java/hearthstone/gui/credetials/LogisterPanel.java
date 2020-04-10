package hearthstone.gui.credetials;

import hearthstone.gui.DefaultSizes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class LogisterPanel extends JPanel {
    private ImageButton registerButton, loginButton;

    public LogisterPanel() {
        configPanel();

        loginButton = new ImageButton("login.png", "loginClicked.png",
                DefaultSizes.logisterButtonWidth,     DefaultSizes.logisterButtonHeight);

        registerButton = new ImageButton("register.png", "registerClicked.png",
                DefaultSizes.logisterButtonWidth,  DefaultSizes.logisterButtonHeight);


        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().getContentPane().setVisible(false);
                CredentialsFrame.getInstance().setContentPane(new LoginPanel());
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().getContentPane().setVisible(false);
                CredentialsFrame.getInstance().setContentPane(new RegisterPanel());
            }
        });

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("logisterBG.png"));
        } catch (Exception e){
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void configPanel(){
        setVisible(true);
    }

    private void layoutComponent(){
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        // first row
        grid.gridy = 0;

        grid.gridx = 0;
        grid.gridwidth = 2;
        grid.insets = new Insets(0, 0, 200, 10);

        // second row
        grid.gridy++;

        grid.gridx = 0;
        grid.gridwidth = 1;
        grid.insets = new Insets(150, 10, 0, 10);
        add(loginButton, grid);

        // third row
        grid.gridy++;

        grid.gridx = 0;
        grid.gridwidth = 1;
        grid.insets = new Insets(10, 10, 0, 10);
        add(registerButton, grid);
    }
}
