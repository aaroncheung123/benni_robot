package com.aaroncheung.prototype4.Networking;

public class UserInformationSingleton {

    private String email;

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




}
