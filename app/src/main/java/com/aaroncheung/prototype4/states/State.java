package com.aaroncheung.prototype4.states;


public interface State {
    public void explain();
    public void moveForward();
    public void moveBackward();
    public void turnRight();
    public void turnLeft();
    public void stop();
}

