package hearthstone.gui.controls;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.game.GameFrame;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class ErrorDialog extends JDialog {
    private JLabel message;
    private ImageButton okButton;

    private int width, height;
    private String text;

    public ErrorDialog(JFrame frame, String text, int width, int height){
        super(frame);
        this.width = width;
        this.height = height;
        this.text = text;

        playError();

        configDialog();

        makeButtons();

        makeLabels();

        layoutComponent();

        setVisible(true);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/dialog_background.png"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
    }

    public void configDialog(){
        setSize(new Dimension(width, height));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        setModal(true);
        setResizable(false);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        ImagePanel backgroundPanel = new ImagePanel("dialog_background.png", width, height);
        backgroundPanel.setOpaque(false);
        setContentPane(backgroundPanel);

        Image cursorImage = null;
        try{
            cursorImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/cursor.png"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point(0, 0), "customCursor");
        setCursor(customCursor);
    }

    public void makeLabels(){
        message = new JLabel(text);
        message.setForeground(new Color(69, 28, 28));
        message.setFont(GameFrame.getInstance().getCustomFont(0, 20));
    }

    public void makeButtons(){
        okButton = new ImageButton("OK", "buttons/red_background.png", 0,
                Color.white, Color.yellow,
                15, 0,
                DefaultSizes.smallButtonWidth, DefaultSizes.smallButtonHeight);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        });
    }

    public void playError() {
        try {
            File file = new File(this.getClass().getResource(
                    "/sounds/error.wav").getFile());
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void layoutComponent(){
        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();

        // first row
        grid.gridy = 0;
        grid.gridx = 0;
        add(message, grid);

        // second row
        grid.gridy = 1;
        grid.gridx = 0;
        grid.insets = new Insets(30, 0, 0, 0);
        add(okButton, grid);
    }
}