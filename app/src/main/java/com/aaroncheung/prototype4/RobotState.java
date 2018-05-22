package com.aaroncheung.prototype4;
import com.aaroncheung.prototype4.States.HappyState;
import com.aaroncheung.prototype4.States.State;

public class RobotState implements State{

    private State RobotState;

    public RobotState(){
        RobotState = new HappyState();
    }

    public void setState(State state) {
        RobotState = state;
    }

    public State getState() {
        return RobotState;
    }

    @Override
    public void explain() {
        RobotState.explain();
    }

}

