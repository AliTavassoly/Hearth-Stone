package hearthstone.gui.game;

import com.sun.tools.javac.Main;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.credetials.LogisterPanel;

import javax.swing.*;

public class GameFrame extends JFrame {
    private MainPanel mainPanel;
    private static GameFrame gameFrame;

    private GameFrame(){
        mainPanel = new MainPanel();

        configFrame();
    }

    public static GameFrame getInstance(){
        if(gameFrame == null){
            return gameFrame = new GameFrame();
        } else {
            return gameFrame;
        }
    }

    private void configFrame(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(DefaultSizes.gameFrameWidth, DefaultSizes.gameFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }
}
