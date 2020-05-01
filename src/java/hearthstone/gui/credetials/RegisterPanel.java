package hearthstone.gui.credetials;

import hearthstone.data.DataBase;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.fields.PasswordField;
import hearthstone.gui.controls.fields.TextField;
import hearthstone.gui.game.GameFrame;
import hearthstone.util.HearthStoneException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class RegisterPanel extends JPanel {
    private ImageButton backButton, registerButton, closeButton, minimizeButton;
    private TextField nameField, userField;
    private PasswordField passField, repField;

    private final String nameText = "Name : ";
    private final String userText = "Username : ";
    private final String passText = "Password : ";
    private final String repText = "Repeat : ";

    private Color textColor;

    private final int startTextY = SizeConfigs.credentialFrameHeight / 2 - 10 - 44;
    private final int startFieldY = SizeConfigs.credentialFrameHeight / 2 - 22 - 49;
    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = SizeConfigs.credentialFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int textFiledDis = 30;
    private final int stringsDis = 30;
    private final int constAddX = 20;
    private final int stringFieldDis = 1;
    private final int registerButtonY = 300;

    private String error = "no";

    public RegisterPanel() {
        configPanel();

        makeFields();

        configText();

        makeIcons();

        makeButtons();

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
        }
        g.drawImage(image, 0, 0, null);

        drawString(nameText,
                SizeConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY, 20, Font.PLAIN, textColor, g);
        drawString(userText,
                SizeConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + stringsDis, 20, Font.PLAIN, textColor, g);
        drawString(passText,
                SizeConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 2 * stringsDis, 20, Font.PLAIN, textColor, g);
        drawString(repText,
                SizeConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 3 * stringsDis, 20, Font.PLAIN, textColor, g);
        if (!error.equals("no")) {
            if(error.equals("Username is invalid(at least 4 character, only contains 1-9, '-', '_' and letters!)")){
                drawString("Username is invalid",
                        SizeConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis, 15, Font.PLAIN, Color.RED, g);
                drawString("at least 4 character, only contains 1-9, '-', '_' and letters!",
                        SizeConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis + 14, 15, Font.PLAIN, Color.RED, g);

            } else if (error.equals("Password is invalid(at least 4 character and contains at least a capital letter!)")){
                drawString("Password is invalid",
                        SizeConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis, 15, Font.PLAIN, Color.RED, g);
                drawString("at least 4 character and contains at least a capital letter!",
                        SizeConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis + 14, 15, Font.PLAIN, Color.RED, g);
            } else {
                drawString(error,
                        SizeConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis, 15, Font.PLAIN, Color.RED, g);
            }
        }
    }

    private void makeFields(){
        nameField = new TextField(10);

        userField = new TextField(10);

        passField = new PasswordField(10);

        repField = new PasswordField(10);
    }

    private void configPanel() {
        setLayout(null);
        setVisible(true);
    }

    private void configText() {
        textColor = new Color(255, 255, 68);
    }

    private void makeIcons(){
        backButton = new ImageButton("icons/back.png", "icons/back_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new ImageButton("icons/close.png", "icons/close_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new ImageButton("icons/minimize.png", "icons/minimize_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

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
                CredentialsFrame.getInstance().switchPanelTo(CredentialsFrame.getInstance(), new LogisterPanel());
            }
        });
    }

    private void makeButtons(){
        registerButton = new ImageButton("register", "buttons/blue_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    hearthstone.HearthStone.register(nameField.getText(), userField.getText(),
                            new String(passField.getPassword()), new String(repField.getPassword()));
                    CredentialsFrame.getInstance().getContentPane().setVisible(false);
                    CredentialsFrame.getInstance().setContentPane(new LogisterPanel());
                    CredentialsFrame.getInstance().setVisible(false);
                    GameFrame.getNewInstance().setVisible(true);
                    DataBase.save();
                } catch (HearthStoneException e) {
                    try {
                        hearthstone.util.Logger.saveLog("ERROR",
                                e.getClass().getName() + ": " + e.getMessage()
                                        + "\nStack Trace: " + e.getStackTrace());
                    } catch (Exception f) { }
                    error = e.getMessage();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
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
        if (color == Color.RED) {
            isNotError = 2;
            graphics2D.setColor(new Color(0, 0, 0, 150));
            graphics2D.fillRect(x - width / isNotError + 2 - 3, y - 11, width + 6, 14);
        }
        graphics2D.setColor(color);
        graphics2D.drawString(text, x - width / isNotError + 2, y);
    }

    private void layoutComponent() {
        nameField.setBounds(SizeConfigs.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 0 * textFiledDis, 100, 20);
        add(nameField);

        userField.setBounds(SizeConfigs.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 1 * textFiledDis, 100, 20);
        add(userField);

        passField.setBounds(SizeConfigs.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 2 * textFiledDis, 100, 20);
        add(passField);

        repField.setBounds(SizeConfigs.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 3 * textFiledDis, 100, 20);
        add(repField);

        registerButton.setBounds(SizeConfigs.credentialFrameWidth / 2 - SizeConfigs.medButtonWidth / 2,
                registerButtonY,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);
        add(registerButton);

        //
        backButton.setBounds(iconX, startIconY, SizeConfigs.iconWidth, SizeConfigs.iconHeight);
        add(backButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis, SizeConfigs.iconWidth, SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY, SizeConfigs.iconWidth, SizeConfigs.iconHeight);
        add(closeButton);
    }
}
