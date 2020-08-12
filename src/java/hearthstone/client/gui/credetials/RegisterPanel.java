package hearthstone.client.gui.credetials;

import hearthstone.client.network.ClientMapper;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.fields.PasswordField;
import hearthstone.client.gui.controls.fields.TextField;
import hearthstone.server.network.ServerMapper;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class RegisterPanel extends JPanel {
    private static RegisterPanel instance;

    private ImageButton backButton, registerButton, closeButton, minimizeButton;
    private TextField nameField, userField;
    private PasswordField passField, repField;

    private BufferedImage backgroundImage;

    private final String nameText = "Name : ";
    private final String userText = "Username : ";
    private final String passText = "Password : ";
    private final String repText = "Repeat : ";

    private Color textColor;

    private final int startTextY = GUIConfigs.credentialFrameHeight / 2 - 10 - 44;
    private final int startFieldY = GUIConfigs.credentialFrameHeight / 2 - 22 - 49;
    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.credentialFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int textFiledDis = 30;
    private final int stringsDis = 30;
    private final int constAddX = 20;
    private final int stringFieldDis = 1;
    private final int registerButtonY = 300;

    public static String error = "no";

    private RegisterPanel() {
        configPanel();

        makeFields();

        configText();

        makeIcons();

        makeButtons();

        layoutComponent();
    }

    public static RegisterPanel makeInstance() {
        return instance = new RegisterPanel();
    }

    public static RegisterPanel getInstance() {
        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage("/images/logister_background.jpg");
        } catch (Exception e) {
        }
        g.drawImage(backgroundImage, 0, 0, null);

        drawString(nameText,
                GUIConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY, 20, Font.PLAIN, textColor, g);
        drawString(userText,
                GUIConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + stringsDis, 20, Font.PLAIN, textColor, g);
        drawString(passText,
                GUIConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 2 * stringsDis, 20, Font.PLAIN, textColor, g);
        drawString(repText,
                GUIConfigs.credentialFrameWidth / 2 - stringFieldDis,
                startTextY + 3 * stringsDis, 20, Font.PLAIN, textColor, g);
        if (!error.equals("no")) {
            if (error.equals("Username is invalid(at least 4 character, only contains 1-9, '-', '_' and letters!)")) {
                drawString("Username is invalid",
                        GUIConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis, 15, Font.PLAIN, Color.RED, g);
                drawString("at least 4 character, only contains 1-9, '-', '_' and letters!",
                        GUIConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis + 14, 15, Font.PLAIN, Color.RED, g);

            } else if (error.equals("Password is invalid(at least 4 character and contains at least a capital letter!)")) {
                drawString("Password is invalid",
                        GUIConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis, 15, Font.PLAIN, Color.RED, g);
                drawString("at least 4 character and contains at least a capital letter!",
                        GUIConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis + 14, 15, Font.PLAIN, Color.RED, g);
            } else {
                drawString(error,
                        GUIConfigs.credentialFrameWidth / 2,
                        startTextY + 4 * stringsDis, 15, Font.PLAIN, Color.RED, g);
            }
        }
    }

    private void makeFields() {
        nameField = new TextField(10);
        nameField.addKeyListener(new MyAction());

        userField = new TextField(10);
        userField.addKeyListener(new MyAction());

        passField = new PasswordField(10);
        passField.addKeyListener(new MyAction());

        repField = new PasswordField(10);
        repField.addKeyListener(new MyAction());
    }

    private void configPanel() {
        setSize(new Dimension(GUIConfigs.credentialFrameWidth, GUIConfigs.credentialFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void configText() {
        textColor = new Color(255, 255, 68);
    }

    private void makeIcons() {
        backButton = new ImageButton("icons/back.png", "icons/back_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        closeButton = new ImageButton("icons/close.png", "icons/close_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton = new ImageButton("icons/minimize.png", "icons/minimize_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

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

    private void makeButtons() {
        registerButton = new ImageButton("register", "buttons/blue_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (RegisterPanel.this.checkValid())
                    ClientMapper.registerRequest(nameField.getText(), userField.getText(),
                            new String(passField.getPassword()));
            }
        });
    }

    private boolean checkValid() {
        String pass1 = new String(passField.getPassword());
        String pass2 = new String(repField.getPassword());

        if (nameField.getText().length() == 0) {
            showError("name should contain at least one character!");
            return false;
        }
        if (userField.getText().length() == 0) {
            showError("username should contain at least one character!");
            return false;
        }
        if (pass1.length() == 0) {
            showError("passwords should contain at least one character!");
            return false;
        }

        if (!pass1.equals(pass2)) {
            showError("passwords does not match!");
            return false;
        }
        return true;
    }

    private void drawString(String text, int x, int y, int size, int style, Color color, Graphics graphic) {
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
        nameField.setBounds(GUIConfigs.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 0 * textFiledDis, 100, 20);
        add(nameField);

        userField.setBounds(GUIConfigs.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 1 * textFiledDis, 100, 20);
        add(userField);

        passField.setBounds(GUIConfigs.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 2 * textFiledDis, 100, 20);
        add(passField);

        repField.setBounds(GUIConfigs.credentialFrameWidth / 2 + constAddX - 12, startFieldY + 3 * textFiledDis, 100, 20);
        add(repField);

        registerButton.setBounds(GUIConfigs.credentialFrameWidth / 2 - GUIConfigs.medButtonWidth / 2,
                registerButtonY,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);
        add(registerButton);

        //
        backButton.setBounds(iconX, startIconY, GUIConfigs.iconWidth, GUIConfigs.iconHeight);
        add(backButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis, GUIConfigs.iconWidth, GUIConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY, GUIConfigs.iconWidth, GUIConfigs.iconHeight);
        add(closeButton);
    }

    public void showError(String error) {
        this.error = error;
        repaint();
    }

    class MyAction implements KeyListener {
        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyReleased(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                registerButton.doClick();
            }
        }
    }
}
