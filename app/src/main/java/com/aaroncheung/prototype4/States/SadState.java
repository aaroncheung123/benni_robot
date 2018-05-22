package com.aaroncheung.prototype4.States;

import com.aaroncheung.prototype4.Hardware.RobotFacade;

public class SadState implements State {

    @Override
    public void explain() {
        RobotFacade.getInstance().say("sad");
    }
}
