package hearthstone.gui.controls.dialogs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class MyDialog extends JDialog {
    private int width, height;
    private JFrame frame;

    public MyDialog(JFrame frame, int width, int height){
        super(frame);
        this.frame = frame;
        this.width = width;
        this.height = height;

        configDialog();
    }

    private void configDialog(){
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

        Image cursorImage = null;
        try{
            cursorImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/cursor.png"));
        } catch (Exception e){
            e.printStackTrace();
        }
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point(0, 0), "customCursor");
        setCursor(customCursor);
    }
}
