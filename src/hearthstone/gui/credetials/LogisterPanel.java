package hearthstone.gui.credetials;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class LogisterPanel extends JPanel {
    private JButton loginButton, registerButton;

    public LogisterPanel(){
        configPanel();

        loginButton = new JButton("login");
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setMargin(new Insets(2, 3 + 35, 2, 3 + 35));

        registerButton = new JButton("register");
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setMargin(new Insets(2, 3 + 25, 2, 3 + 25));

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
            image = ImageIO.read(new File("logisterBG.jpg"));
        } catch (Exception e){
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void configPanel(){
        setVisible(true);
        setBackground(Color.ORANGE);
    }

    /*private void drawMessage(Graphics graphics){
        Graphics2D graphics2D = (Graphics2D)graphics;
        String message = "register or login?";
        Font font = new Font("Helvetica", Font.BOLD, 40);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);
        int width = fontMetrics.stringWidth(message);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics2D.setFont(font);
        graphics2D.drawString(message, (DefaultSizes.credentialFrameWidth - width) / 2, 150);
    }*/

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
        grid.insets = new Insets(350, 0, 0, 10);
        add(loginButton, grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.insets = new Insets(350, 10, 0, 0);
        add(registerButton, grid);
    }
}
