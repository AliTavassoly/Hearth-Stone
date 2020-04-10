package hearthstone.gui.credetials;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.ImageButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class RegisterPanel extends JPanel {
    private ImageButton backButton, registerButton, closeButton, minimizeButton;
    private JTextField nameField, userField;
    private JPasswordField passField, repField;

    private final String nameText = "Name : ";
    private final String userText = "Username : ";
    private final String passText = "Password : ";
    private final String repText = "Repeat : ";

    private Color textColor;

    private final int startTextY = DefaultSizes.credentialFrameHeight / 2 - 10 - 44;
    private final int startFieldY = DefaultSizes.credentialFrameHeight / 2 - 22 - 49;
    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.credentialFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int textFiledDis = 30;
    private final int stringsDis = 30;
    private final int constAddX = 20;
    private final int stringFieldDis = 1;

    private String error = "no";

    public RegisterPanel() {
        configPanel();

        registerButton = new ImageButton("register.png", "register_active.png",
                DefaultSizes.logisterButtonWidth,
                DefaultSizes.logisterButtonHeight);

        backButton = new ImageButton("back.png", "back_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        closeButton = new ImageButton("close.png", "close_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new ImageButton("minimize.png", "minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        nameField = new JTextField(10); nameField.setBorder(null);

        userField = new JTextField(10); userField.setBorder(null);

        passField = new JPasswordField(10); passField.setBorder(null);

        repField = new JPasswordField(10); repField.setBorder(null);

        configText();

        minimizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().setState(Frame.ICONIFIED);
                CredentialsFrame.getInstance().setState(Frame.NORMAL);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

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
            image = ImageIO.read(new File("logister_background.jpg"));
        } catch (Exception e) { }
        g.drawImage(image, 0, 0, null);

        drawString(nameText,
                DefaultSizes.credentialFrameWidth / 2 - stringFieldDis,
                startTextY, 20, Font.PLAIN, textColor, g);
        drawString(userText,
                DefaultSizes.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + stringsDis, 20, Font.PLAIN, textColor, g);
        drawString(passText,
                DefaultSizes.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 2 * stringsDis, 20, Font.PLAIN, textColor, g);
        drawString(repText,
                DefaultSizes.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 3 * stringsDis,20, Font.PLAIN, textColor, g);
        if (!error.equals("no")) {
            drawString("username or password is not correct!",
                    DefaultSizes.credentialFrameWidth / 2 - stringFieldDis,
                    startTextY + 4 * stringsDis,15, Font.ITALIC, Color.RED, g);
        }
    }

    private void configText(){
        textColor = new Color(255, 255, 68);
    }

    private void configPanel() {
        setLayout(null);
        setVisible(true);
    }

    private void drawString(String text, int x, int y, int size, int style, Color color, Graphics graphic) {
        Graphics2D graphics2D = (Graphics2D) graphic;
        Font font = CredentialsFrame.getInstance().getCustomFont(style, size);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);
        int width = fontMetrics.stringWidth(text);
        graphics2D.setColor(color);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics2D.setFont(font);
        graphics2D.drawString(text, x - width, y);
    }

    private void layoutComponent() {
        nameField.setBounds(DefaultSizes.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 0 * textFiledDis, 100, 20);
        add(nameField);

        userField.setBounds(DefaultSizes.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 1 * textFiledDis, 100, 20);
        add(userField);

        passField.setBounds(DefaultSizes.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 2 * textFiledDis, 100, 20);
        add(passField);

        repField.setBounds(DefaultSizes.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 3 * textFiledDis, 100, 20);
        add(repField);

        registerButton.setBounds(DefaultSizes.credentialFrameWidth / 2 - DefaultSizes.logisterButtonWidth / 2,
                startFieldY + 5 * textFiledDis,
                DefaultSizes.logisterButtonWidth,
                DefaultSizes.logisterButtonHeight);
        add(registerButton);

        //
        backButton.setBounds(iconX, startIconY, DefaultSizes.iconWidth, DefaultSizes.iconHeight);
        add(backButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis, DefaultSizes.iconWidth, DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY, DefaultSizes.iconWidth, DefaultSizes.iconHeight);
        add(closeButton);
    }
}
