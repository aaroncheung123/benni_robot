package com.aaroncheung.prototype4.Helper;

public class UserInformationSingleton {

    private String email;
    private String SERVER_URL = "https://guarded-savannah-87082.herokuapp.com/";
    private Integer RobotBattery;
    private Integer HeadBattery;
    private Integer minInLowCharge;
    private Boolean isChatting = false;


    private static UserInformationSingleton instance = null;
    private UserInformationSingleton() {
        // Exists only to defeat instantiation.
    }

    public static UserInformationSingleton getInstance() {
        if(instance == null) {
            instance = new UserInformationSingleton();
        }
        return instance;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public String getSERVER_URL(){
        return SERVER_URL;
    }


    public void setRobotBattery(String battery) {
        if (battery.matches("i")) {
            RobotBattery= 95;
            minInLowCharge = 0;
        } else if (battery.matches("o")) {
            RobotBattery = 75;
            minInLowCharge = 0;
        } else if (battery.matches("p")) {
            minInLowCharge++;
            RobotBattery = 20 - minInLowCharge;
            if(RobotBattery < 0)
                RobotBattery = 0;
        }
    }

    public Integer getRobotBattery() {
        return RobotBattery;
    }

    public void setMinInLowCharge(Integer minInLowCharge) {
        this.minInLowCharge = minInLowCharge;
    }

    public Integer getHeadBattery() {
        return HeadBattery;
    }

    public void setHeadBattery(Integer headBattery) {
        HeadBattery = headBattery;
    }


    public void setChatting(Boolean chatting) {
        isChatting = chatting;
    }

    public Boolean getChatting() {
        return isChatting;
    }

}
