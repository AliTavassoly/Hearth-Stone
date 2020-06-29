package hearthstone.gui.credetials;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.fields.PasswordField;
import hearthstone.gui.controls.fields.TextField;
import hearthstone.gui.game.GameFrame;
import hearthstone.util.FontType;
import hearthstone.util.HearthStoneException;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class LoginPanel extends JPanel {
    private ImageButton backButton, loginButton, closeButton, minimizeButton;
    private TextField userField;
    private PasswordField passField;

    private final String userText = "Username : ";
    private final String passText = "Password : ";
    private String error = "no";

    private BufferedImage backgroundImage;

    private Color textColor;

    private final int startTextY = SizeConfigs.credentialFrameHeight / 2 - 10 - 45 + 30 + 1;
    private final int startFieldY = SizeConfigs.credentialFrameHeight / 2 - 22 - 50 + 30 + 1;
    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = SizeConfigs.credentialFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int textFieldDis = 30;
    private final int constAddX = 20;
    private final int stringFieldDis = 3;
    private final int loginButtonY = 300;

    public LoginPanel() {
        configPanel();

        configText();

        makeIcons();

        makeButtons();

        makeFields();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage("/images/logister_background.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, null);

        drawString(userText,
                SizeConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 0, 20, Font.PLAIN, textColor, g);
        drawString(passText,
                SizeConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 1 * 30, 20, Font.PLAIN, textColor, g);
        if (!error.equals("no")) {
            drawString(error,
                    SizeConfigs.credentialFrameWidth / 2,
                    startTextY + 2 * 30, 15, Font.PLAIN, Color.RED, g);
        }
    }

    private void configPanel() {
        setSize(new Dimension(SizeConfigs.credentialFrameWidth, SizeConfigs.credentialFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void configText() {
        textColor = new Color(255, 255, 68);
    }

    private void makeButtons() {
        loginButton = new ImageButton("login", "buttons/green_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    hearthstone.HearthStone.login(userField.getText(), new String(passField.getPassword()));
                    CredentialsFrame.getInstance().setVisible(false);
                    GameFrame.getNewInstance().setVisible(true);

                } catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage() +
                                        "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) {
                    }
                    error = e.getMessage();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void makeIcons() {
        backButton = new ImageButton("icons/back.png", "icons/back_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new ImageButton("icons/close.png", "icons/close_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new ImageButton("icons/minimize.png", "icons/minimize_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().switchPanelTo(CredentialsFrame.getInstance(), new LogisterPanel());
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
    }

    private void makeFields() {
        userField = new TextField(10);
        userField.setBorder(null);

        passField = new PasswordField(10);
        passField.setBorder(null);
    }

    private void drawString(String text, int x, int y, int size, int style,
                            Color color, Graphics graphic) {
        Graphics2D graphics2D = (Graphics2D) graphic;
        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT, style, size);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(font);
        int width = fontMetrics.stringWidth(text);

        graphics2D.setColor(color);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics2D.setFont(font);

        int isNotError = 1;
        if (color == Color.RED) {
            isNotError = 2;
            graphics2D.setColor(new Color(0, 0, 0, 150));
            graphics2D.fillRect(x - width / isNotError + 2 - 3, y - 11, width + 6, 14);
        }
        graphics2D.setColor(color);
        graphics2D.drawString(text, x - width / isNotError + 2, y);
    }

    private void layoutComponent() {
        backButton.setBounds(iconX, iconX, SizeConfigs.iconWidth, SizeConfigs.iconHeight);
        add(backButton);

        userField.setBounds(SizeConfigs.credentialFrameWidth / 2 + constAddX - 12,
                startFieldY + 0 * textFieldDis,
                100, 20);
        add(userField);

        passField.setBounds(SizeConfigs.credentialFrameWidth / 2 + constAddX - 12,
                startFieldY + 1 * textFieldDis,
                100, 20);
        add(passField);

        loginButton.setBounds(SizeConfigs.credentialFrameWidth / 2 - SizeConfigs.medButtonWidth / 2,
                loginButtonY,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);
        add(loginButton);

        //
        backButton.setBounds(iconX, startIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(backButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(closeButton);
    }
}
