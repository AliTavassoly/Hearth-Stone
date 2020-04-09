package hearthstone.gui.credetials;

import hearthstone.gui.DefaultSizes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class LoginPanel extends JPanel {
    private ImageButton backButton, loginButton;
    private JTextField userField;
    private JPasswordField passField;
    private final String userText = "username : ";
    private final String passText = "password : ";
    private String error = "no";

    public LoginPanel() {
        configPanel();

        loginButton = new ImageButton("login.png", 120, 80);

        backButton = new ImageButton("back.png", 80, 80);

        userField = new JTextField(10);
        userField.setBorder(null);

        passField = new JPasswordField(10);
        passField.setBorder(null);

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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("logisterBG.png"));
        } catch (Exception e) {
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);

        g.setColor(Color.WHITE);
        drawString(userText,
                DefaultSizes.credentialFrameWidth / 2 - 20,
                DefaultSizes.credentialFrameHeight / 2 + 16, 20, Font.PLAIN, Color.WHITE, g);
        g.setColor(Color.WHITE);
        drawString(passText,
                DefaultSizes.credentialFrameWidth / 2 - 20,
                DefaultSizes.credentialFrameHeight / 2 + 48, 20, Font.PLAIN, Color.WHITE, g);
        if (!error.equals("no")) {
            g.setColor(Color.BLACK);
            drawString("username or password is not correct!",
                    DefaultSizes.credentialFrameWidth / 2 - 20,
                    DefaultSizes.credentialFrameHeight / 2 + 80, 15, Font.ITALIC, Color.RED, g);
        }
    }

    private void configPanel() {
        setLayout(null);
        setVisible(true);
    }

    private void drawString(String text, int x, int y, int size, int style, Color color, Graphics graphic) {
        Graphics2D graphics2D = (Graphics2D) graphic;
        Font font = new Font("Helvetica", style, size);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);
        int width = fontMetrics.stringWidth(text);
        graphics2D.setColor(color);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics2D.setFont(font);
        graphics2D.drawString(text, x - width, y);
    }

    private void layoutComponent() {
        backButton.setBounds(20, 20, DefaultSizes.backButtonWidth, DefaultSizes.backButtonHeight);
        add(backButton);

        userField.setBounds(DefaultSizes.credentialFrameWidth / 2, DefaultSizes.credentialFrameHeight / 2, 100, 20);
        add(userField);

        passField.setBounds(DefaultSizes.credentialFrameWidth / 2, DefaultSizes.credentialFrameHeight / 2 + 30, 100, 20);
        add(passField);

        loginButton.setBounds(DefaultSizes.credentialFrameWidth / 2 - 80, DefaultSizes.credentialFrameHeight / 2 + 100, (int) loginButton.getPreferredSize().getWidth(), (int) loginButton.getPreferredSize().getHeight());
        add(loginButton);
    }
}
