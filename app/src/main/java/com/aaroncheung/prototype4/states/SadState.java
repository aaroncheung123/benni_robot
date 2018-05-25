package com.aaroncheung.prototype4.states;

import com.aaroncheung.prototype4.hardware.RobotFacade;

public class SadState implements State {

    @Override
    public void explain() {
        RobotFacade.getInstance().say("sad");
    }
}
