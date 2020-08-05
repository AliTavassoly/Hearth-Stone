package hearthstone.client.gui;

import hearthstone.client.configs.GUIConfigs;
import hearthstone.client.gui.controls.dialogs.ErrorDialog;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.util.FontType;
import hearthstone.util.getresource.FontResource;
import hearthstone.util.getresource.ImageResource;

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
                GUIConfigs.errorWidth, GUIConfigs.errorHeight);
    }

    public void  setCursor(){
        Image cursorImage = null;
        try {
            cursorImage = ImageResource.getInstance().getImage("/images/normal_cursor.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point(0, 0), "customCursor");
        setCursor(customCursor);
    }
}
