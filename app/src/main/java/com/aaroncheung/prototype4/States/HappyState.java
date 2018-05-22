package com.aaroncheung.prototype4.States;

import com.aaroncheung.prototype4.RobotFacade;

public class HappyState implements State {

    @Override
    public void explain() {
        RobotFacade.getInstance().say("I am in the happy state");
        System.out.println("Robot is in happy state");
    }
}
