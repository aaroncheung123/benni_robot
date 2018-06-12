package com.aaroncheung.prototype4.states;

import com.aaroncheung.prototype4.robot.RobotFacade;

public class SadState implements State {

    @Override
    public void explain() {
        RobotFacade.getInstance().say("sad");
    }

    @Override
    public void moveForward() {
        RobotFacade.getInstance().say("Moving Forward");
        RobotFacade.getInstance().forward();
    }

    @Override
    public void moveBackward() {
        RobotFacade.getInstance().say("Moving Backward");
        RobotFacade.getInstance().backward();
    }

    @Override
    public void turnRight() {
        RobotFacade.getInstance().say("Turning Right");
        RobotFacade.getInstance().right();
    }

    @Override
    public void turnLeft() {
        RobotFacade.getInstance().say("Turning Left");
        RobotFacade.getInstance().left();
    }

    @Override
    public void stop(){
        RobotFacade.getInstance().say("Stopping");
        RobotFacade.getInstance().stop();
    }
}
