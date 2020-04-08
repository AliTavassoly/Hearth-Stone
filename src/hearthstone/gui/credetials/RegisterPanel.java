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

        // first row
        grid.gridy = 0;
        grid.gridx = 0;
        grid.insets = new Insets(10, 0, 10, 0);
        add(registerButton, grid);

        //second row
        grid.gridy = 1;
        grid.gridx = 0;
        grid.insets = new Insets(10, 0, 10, 0);
        add(backButton, grid);
    }
}
