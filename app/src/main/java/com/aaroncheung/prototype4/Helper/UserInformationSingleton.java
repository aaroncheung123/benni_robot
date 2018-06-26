package com.aaroncheung.prototype4.Helper;

public class UserInformationSingleton {

    private String email;
    private String SERVER_URL = "https://guarded-savannah-87082.herokuapp.com/";
    private Integer Battery = 100;
    private Integer minInLowCharge = 0;

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


    public void setBattery(String battery) {
        if (battery.matches("i")) {
            Battery = 100;
            minInLowCharge = 0;
        } else if (battery.matches("o")) {
            Battery = 75;
            minInLowCharge = 0;
        } else if (battery.matches("p")) {
            minInLowCharge++;
            Battery = 20 - minInLowCharge;
            if(Battery < 0)
                Battery = 0;
        }
    }

    public Integer getBattery() {
        return Battery;
    }

    public void incrementMinInLowCharge(Integer minInLowCharge) {
        minInLowCharge += minInLowCharge;
    }

    public Integer getMinInLowCharge() {
        return minInLowCharge;
    }
}
