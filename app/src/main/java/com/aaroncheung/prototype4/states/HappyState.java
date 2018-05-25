package com.aaroncheung.prototype4.states;

import com.aaroncheung.prototype4.robot.RobotFacade;

public class HappyState implements State {

    @Override
    public void explain() {
        RobotFacade.getInstance().say("happy");
    }
}
