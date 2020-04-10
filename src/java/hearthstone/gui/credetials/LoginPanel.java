package hearthstone.gui.credetials;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.ImageButton;
import hearthstone.gui.game.GameFrame;
import hearthstone.util.HearthStoneException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class LoginPanel extends JPanel {
    private ImageButton backButton, loginButton, closeButton, minimizeButton;
    private JTextField userField;
    private JPasswordField passField;

    private final String userText = "Username : ";
    private final String passText = "Password : ";
    private String error = "no";

    private Color textColor;

    private final int startTextY = DefaultSizes.credentialFrameHeight / 2 - 10 - 45 + 30 + 1;
    private final int startFieldY = DefaultSizes.credentialFrameHeight / 2 - 22 - 50 + 30 + 1;
    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = DefaultSizes.credentialFrameHeight - DefaultSizes.iconHeight - 20;
    private final int iconsDis = 70;
    private final int textFieldDis = 30;
    private final int constAddX = 20;
    private final int stringFieldDis = 3;
    private final int loginButtonY = 300;

    public LoginPanel() {
        configPanel();

        loginButton = new ImageButton("login.png", "login_active.png",
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

        userField = new JTextField(10); userField.setBorder(null);

        passField = new JPasswordField(10); passField.setBorder(null);

        configText();

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().getContentPane().setVisible(false);
                CredentialsFrame.getInstance().setContentPane(new LogisterPanel());
            }
        });

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

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    hearthstone.HearthStone.login(userField.getText(), new String(passField.getPassword()));
                    CredentialsFrame.getInstance().setVisible(false);
                    GameFrame.getInstance().setVisible(true);
                } catch (HearthStoneException e){
                    error = e.getMessage();
                    repaint();
                } catch (Exception ex){
                    System.out.println(ex.getMessage());
                    ex.getStackTrace();
                }
            }
        });

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/logister_background.jpg"));
        } catch (Exception e) {
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);

        drawString(userText,
                DefaultSizes.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 0, 20, Font.PLAIN, textColor, g);
        drawString(passText,
                DefaultSizes.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 1 * 30, 20, Font.PLAIN, textColor, g);
        if (!error.equals("no")) {
            drawString(error,
                    DefaultSizes.credentialFrameWidth / 2,
                    startTextY + 2 * 30, 15, Font.PLAIN, Color.RED, g);
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

        int isNotError = 1;
        if(color == Color.RED){
            isNotError = 2;
            graphics2D.setColor(new Color(0, 0, 0, 150));
            graphics2D.fillRect(x - width / isNotError + 2 - 3, y - 12, width + 6, 15);
        }
        graphics2D.setColor(color);
        graphics2D.drawString(text, x - width / isNotError + 2, y);
    }

    private void layoutComponent() {
        backButton.setBounds(iconX, iconX, DefaultSizes.iconWidth, DefaultSizes.iconHeight);
        add(backButton);

        userField.setBounds(DefaultSizes.credentialFrameWidth / 2 + constAddX - 12,
                startFieldY + 0 * textFieldDis,
                100, 20);
        add(userField);

        passField.setBounds(DefaultSizes.credentialFrameWidth / 2 + constAddX - 12,
                startFieldY + 1 * textFieldDis,
                100, 20);
        add(passField);

        loginButton.setBounds(DefaultSizes.credentialFrameWidth / 2 - DefaultSizes.logisterButtonWidth / 2,
                loginButtonY,
                DefaultSizes.logisterButtonWidth,
                DefaultSizes.logisterButtonHeight);
        add(loginButton);

        //
        backButton.setBounds(iconX, startIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(backButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);
    }
}
