package com.aaroncheung.prototype4.states;

import android.util.Log;

import com.aaroncheung.prototype4.robot.RobotFacade;

public class HappyState implements State {

    String TAG = "debug_123";

    @Override
    public void explain() {
        RobotFacade.getInstance().say("happy");
    }


    @Override
    public void moveForward() {
        Log.d(TAG, "Happy State Moving Forward");
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
