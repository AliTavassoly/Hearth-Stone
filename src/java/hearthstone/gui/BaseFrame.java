package hearthstone.gui;

import hearthstone.gui.controls.dialogs.ErrorDialog;
import hearthstone.gui.game.GameFrame;
import hearthstone.util.FontType;
import hearthstone.util.getresource.FontResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {
    public BaseFrame() {
        configFrame();
    }

    private void configFrame(){
        setCursor();
    }

    public Font getCustomFont(FontType fontType, int style, int size) {
        Font font = null;
        switch (fontType){
            case TEXT:
            case NUMBER:
                font = FontResource.getInstance().getFont("/fonts/text_font.ttf");
                break;
        }
        return font.deriveFont(style, size);
    }

    public void switchPanelTo(JFrame frame, JPanel panel) {
        frame.getContentPane().setVisible(false);
        frame.setContentPane(panel);
    }

    public static void error(String text) {
        ErrorDialog errorDialog = new ErrorDialog(GameFrame.getInstance(), text,
                SizeConfigs.errorWidth, SizeConfigs.errorHeight);
    }

    public void  setCursor(){
        Image cursorImage = null;
        try {
            cursorImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/normal_cursor.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point(0, 0), "customCursor");
        setCursor(customCursor);
    }
}
