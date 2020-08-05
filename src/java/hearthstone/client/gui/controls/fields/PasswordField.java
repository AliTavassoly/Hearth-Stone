package hearthstone.client.gui.controls.fields;

import javax.swing.*;
import java.awt.*;

public class PasswordField extends JPasswordField {
    public PasswordField(int columns){
        super(columns);
        configPasswordField();
    }

    private void configPasswordField(){
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 150));
        setForeground(new Color(255, 255, 68));
        setBorder(null);
    }

    protected void paintComponent(Graphics g) {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}