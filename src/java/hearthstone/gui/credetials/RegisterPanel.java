package hearthstone.gui.credetials;

import hearthstone.gui.DefaultSizes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class RegisterPanel extends JPanel {
    private ImageButton backButton, registerButton;
    private JTextField nameField, userField;
    private JPasswordField passField, repField;
    private final String nameText = "Name : ";
    private final String userText = "Username : ";
    private final String passText = "Password : ";
    private final String repText = "Repeat : ";
    private Color textColor;
    private String error = "no";
    private int startTextY = DefaultSizes.credentialFrameHeight / 2 - 10 - 44;
    private int startFieldY = DefaultSizes.credentialFrameHeight / 2 - 22 - 49;

    public RegisterPanel() {
        configPanel();

        registerButton = new ImageButton("register.png", "registerClicked.png", DefaultSizes.logisterButtonWidth, DefaultSizes.logisterButtonHeight);

        backButton = new ImageButton("back.png", 80, 80);

        nameField = new JTextField(10);
        nameField.setBorder(null);

        userField = new JTextField(10);
        userField.setBorder(null);

        passField = new JPasswordField(10);
        passField.setBorder(null);

        repField = new JPasswordField(10);
        repField.setBorder(null);

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

        drawString(nameText,
                DefaultSizes.credentialFrameWidth / 2 - 30,//130
                startTextY, 20, Font.PLAIN, textColor, g);
        drawString(userText,
                DefaultSizes.credentialFrameWidth / 2 - 30,//150
                startTextY + 30, 20, Font.PLAIN, textColor, g);
        drawString(passText,
                DefaultSizes.credentialFrameWidth / 2 - 30,//150
                startTextY + 2 * 30, 20, Font.PLAIN, textColor, g);
        drawString(repText,
                DefaultSizes.credentialFrameWidth / 2 - 30,//190
                startTextY + 3 * 30,20, Font.PLAIN, textColor, g);
        if (!error.equals("no")) {
            drawString("username or password is not correct!",
                    DefaultSizes.credentialFrameWidth / 2 - 30,
                    startTextY + 4 * 30,15, Font.ITALIC, Color.RED, g);
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

        nameField.setBounds(DefaultSizes.credentialFrameWidth / 2, startFieldY + 0 * 30, 100, 20);
        add(nameField);

        userField.setBounds(DefaultSizes.credentialFrameWidth / 2, startFieldY + 1 * 30, 100, 20);
        add(userField);

        passField.setBounds(DefaultSizes.credentialFrameWidth / 2, startFieldY + 2 * 30, 100, 20);
        add(passField);

        repField.setBounds(DefaultSizes.credentialFrameWidth / 2, startFieldY + 3 * 30, 100, 20);
        add(repField);

        registerButton.setBounds(DefaultSizes.credentialFrameWidth / 2 - 80, DefaultSizes.credentialFrameHeight / 2 + 100, (int) registerButton.getPreferredSize().getWidth(), (int) registerButton.getPreferredSize().getHeight());
        add(registerButton);
    }
}
