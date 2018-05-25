package com.aaroncheung.prototype4.states;

public class RobotState implements State{

    private State RobotState;

    private static RobotState instance = null;
    private RobotState() {
        // Exists only to defeat instantiation.
        RobotState = new HappyState();
    }

    public static RobotState getInstance() {
        if(instance == null) {
            instance = new RobotState();
        }
        return instance;
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

    @Override
    public void moveForward() {
        RobotState.moveForward();
    }

    @Override
    public void moveBackward() {
        RobotState.moveBackward();
    }

    @Override
    public void turnRight() {
        RobotState.turnRight();
    }

    @Override
    public void turnLeft() {
        RobotState.turnLeft();

    }


}

