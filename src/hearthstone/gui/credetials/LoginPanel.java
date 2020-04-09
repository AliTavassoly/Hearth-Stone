package hearthstone.gui.credetials;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class LoginPanel extends JPanel {
    private JButton backButton, loginButton;
    private JTextField userField, passField;
    private JLabel userLabel, passLabel;

    public LoginPanel(){
        configPanel();

        loginButton = new JButton("login");
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setMargin(new Insets(2, 3 + 35, 2, 3 + 35));

        backButton = new JButton("back");
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setMargin(new Insets(2, 4 + 35, 2, 4 + 35));

        userField = new JTextField(10);
        userField.setBorder(null);

        passField = new JTextField(10);
        passField.setBorder(null);

        userLabel = new JLabel("username : ");
        userLabel.setForeground(Color.WHITE);

        passLabel = new JLabel("password : ");
        passLabel.setForeground(Color.WHITE);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().getContentPane().setVisible(false);
                CredentialsFrame.getInstance().setContentPane(new LogisterPanel());
            }
        });

        /*loginButton.addActionListener(new ActionListener() {
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
        setBackground(Color.BLACK);
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
        grid.insets = new Insets(250, 0, 5, 0);
        add(userLabel, grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.insets = new Insets(250, 0, 5, 0);
        add(userField, grid);

        // next row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 1;
        grid.insets = new Insets(5, 0, 0, 0);
        add(passLabel, grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.insets = new Insets(5, 0, 0, 0);
        add(passField, grid);

        // next row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 2;
        grid.insets = new Insets(70, 0, 10, 0);
        add(loginButton, grid);

        //fourth row
        grid.gridy++;
        grid.gridx = 0;
        grid.gridwidth = 2;
        grid.insets = new Insets(10, 0, 10, 0);
        add(backButton, grid);
    }
}
