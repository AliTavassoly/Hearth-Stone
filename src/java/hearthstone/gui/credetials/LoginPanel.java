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

    private final String userText = "Username : ";
    private final String passText = "Password : ";
    private String error = "no";

    private Color textColor;

    private int startTextY = DefaultSizes.credentialFrameHeight / 2 - 10 - 45;
    private int startFieldY = DefaultSizes.credentialFrameHeight / 2 - 22 - 50;

    public LoginPanel() {
        configPanel();

        loginButton = new ImageButton("login.png", "loginClicked.png", DefaultSizes.logisterButtonWidth, DefaultSizes.logisterButtonHeight);

        backButton = new ImageButton("back.png", 80, 80);

        userField = new JTextField(10);
        userField.setBorder(null);

        passField = new JPasswordField(10);
        passField.setBorder(null);

        textColor = new Color(255, 255, 68);

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
                startTextY + 0, 20, Font.PLAIN, textColor, g);
        g.setColor(Color.WHITE);
        drawString(passText,
                DefaultSizes.credentialFrameWidth / 2 - 20,
                startTextY + 1 * 30, 20, Font.PLAIN, textColor, g);
        if (!error.equals("no")) {
            g.setColor(Color.BLACK);
            drawString("username or password is not correct!",
                    DefaultSizes.credentialFrameWidth / 2 - 20,
                    startTextY + 30, 15, Font.ITALIC, Color.RED, g);
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

        userField.setBounds(DefaultSizes.credentialFrameWidth / 2, startFieldY + 0 * 30, 100, 20);
        add(userField);

        passField.setBounds(DefaultSizes.credentialFrameWidth / 2, startFieldY + 1 * 30, 100, 20);
        add(passField);

        loginButton.setBounds(DefaultSizes.credentialFrameWidth / 2 - 80, startFieldY + 4 * 30, (int) loginButton.getPreferredSize().getWidth(), (int) loginButton.getPreferredSize().getHeight());
        add(loginButton);
    }
}
