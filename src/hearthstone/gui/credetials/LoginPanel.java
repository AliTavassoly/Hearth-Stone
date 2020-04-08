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

    public LoginPanel(){
        configPanel();

        loginButton = new JButton("login");
        backButton = new JButton("back");

        loginButton.setMargin(new Insets(2, 3 + 35, 2, 3 + 35));
        backButton.setMargin(new Insets(2, 4 + 35, 2, 4 + 35));

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

        // first row
        grid.gridy = 0;
        grid.gridx = 0;
        grid.insets = new Insets(10, 0, 10, 0);
        add(loginButton, grid);

        //second row
        grid.gridy++;
        grid.gridx = 0;
        grid.insets = new Insets(10, 0, 10, 0);
        add(backButton, grid);
    }
}
