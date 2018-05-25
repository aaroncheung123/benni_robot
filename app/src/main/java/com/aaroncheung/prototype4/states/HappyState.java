package com.aaroncheung.prototype4.states;

import com.aaroncheung.prototype4.robot.RobotFacade;

public class HappyState implements State {

    @Override
    public void explain() {
        RobotFacade.getInstance().say("happy");
    }

    @Override
    public void moveForward() {
        RobotFacade.getInstance().forward();
    }

    @Override
    public void moveBackward() {
        RobotFacade.getInstance().backward();
    }

    @Override
    public void turnRight() {
        RobotFacade.getInstance().right();
    }

    @Override
    public void turnLeft() {
        RobotFacade.getInstance().left();
    }


}
