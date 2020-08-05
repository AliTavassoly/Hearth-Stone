package hearthstone.client.gui.util;

import java.awt.*;

public class Animation {
    private int destinationX, destinationY;
    private int destinationWidth, destinationHeight;
    private Component component;

    private int removeDelayAfterArrived;

    public Animation(int destinationX, int destinationY, Component component){
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.component = component;
    }

    public Animation(int destinationX, int destinationY, int removeDelayAfterArrived, Component component){
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.removeDelayAfterArrived = removeDelayAfterArrived;
        this.component = component;
    }

    public Animation(int destinationX, int destinationY, int destinationWidth, int destinationHeight, Component component){
        this.destinationX = destinationX;
        this.destinationY = destinationY;

        this.destinationWidth = destinationWidth;
        this.destinationHeight = destinationHeight;

        this.component = component;
    }

    public Animation(int destinationX, int destinationY, int destinationWidth, int destinationHeight, int removeDelayAfterArrived, Component component){
        this.destinationX = destinationX;
        this.destinationY = destinationY;

        this.destinationWidth = destinationWidth;
        this.destinationHeight = destinationHeight;

        this.removeDelayAfterArrived = removeDelayAfterArrived;

        this.component = component;
    }

    public synchronized void setComponent(Component component){
        this.component = component;
    }
    public synchronized Component getComponent(){
        return component;
    }

    public synchronized void setDestinationX(int destinationX){
        this.destinationX = destinationX;
    }
    public synchronized void setDestinationY(int destinationY){
        this.destinationY = destinationY;
    }

    public synchronized int getDestinationX(){
        return destinationX;
    }
    public synchronized int getDestinationY(){
        return destinationY;
    }

    public synchronized int getDestinationWidth(){
        return destinationWidth;
    }
    public synchronized int getDestinationHeight(){
        return destinationHeight;
    }

    public synchronized int getRemoveDelayAfterArrived() {
        return removeDelayAfterArrived;
    }
    public synchronized void setRemoveDelayAfterArrived(int removeDelayAfterArrived) {
        this.removeDelayAfterArrived = removeDelayAfterArrived;
    }
}
