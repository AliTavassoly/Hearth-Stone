package hearthstone.gui.game.play;

import java.awt.*;

public class Animation {
    private int x, y;
    private Component component;

    public Animation(int x, int y, Component component){
        this.x = x;
        this.y = y;
        this.component = component;
    }

    public synchronized void setComponent(Component component){
        this.component = component;
    }
    public synchronized Component getComponent(){
        return component;
    }

    public synchronized void setX(int x){
        this.x = x;
    }
    public synchronized void setY(int y){
        this.y = y;
    }

    public synchronized int getX(){
        return x;
    }
    public synchronized int getY(){
        return y;
    }
}
