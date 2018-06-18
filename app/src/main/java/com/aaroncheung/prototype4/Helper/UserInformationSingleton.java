package com.aaroncheung.prototype4.Helper;

public class UserInformationSingleton {

    private String email;
    private String SERVER_URL = "https://guarded-savannah-87082.herokuapp.com/";

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



}
