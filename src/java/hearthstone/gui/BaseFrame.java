package hearthstone.gui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseFrame extends JFrame {
    private Font hearthStoneFont;

    public BaseFrame (){
        try (InputStream in = new BufferedInputStream(new FileInputStream("text_font.ttf"))) {
            hearthStoneFont = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public Font getCustomFont(int style, int size){
        return hearthStoneFont.deriveFont(style, size);
    }
}
