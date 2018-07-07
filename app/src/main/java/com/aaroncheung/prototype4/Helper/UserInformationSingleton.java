package com.aaroncheung.prototype4.Helper;

import java.util.ArrayList;
import java.util.Map;

public class UserInformationSingleton {

    private String email;
    private String SERVER_URL = "https://guarded-savannah-87082.herokuapp.com/";
    private Integer RobotBattery;
    private Integer HeadBattery;
    private Integer minInLowCharge;
    private Boolean isChatting = false;
    private Map<String, Object> context;
    Boolean voiceControlled = false;

    private ArrayList<String> MovmentSayings;
    private ArrayList<String> ChatSayings;

    private ArrayList<String> FaceSayings;

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

    public void setSayings(){
        MovmentSayings = new ArrayList<String>();
        MovmentSayings.add("Weeeee!");
        MovmentSayings.add("This is fun!");
        MovmentSayings.add("I love driving around!");
        MovmentSayings.add("Faster, Faster, Faster!");
        MovmentSayings.add("Don't crash me! You'll break me!");
        MovmentSayings.add("You're pretty good at this!");
        MovmentSayings.add("Thanks for helping me move around!");

        ChatSayings = new ArrayList<String>();
        ChatSayings.add("Lets Chat! Push the Off button to start chatting and ask me a question");
        ChatSayings.add("Thanks for chatting with me, I get so bored! Push the Off button to start chatting with me");
        ChatSayings.add("I love talking! Push the button to start and stop talking with me!");
        ChatSayings.add("Oh boy! A friend to talk with! Push the button to start talking to me!");

        FaceSayings = new ArrayList<String>();
        FaceSayings.add("Hey! That tickled!");
        FaceSayings.add("You touched me!");
        FaceSayings.add("Hey! Thats my face!");
        FaceSayings.add("Don't make me touch your face!");
        FaceSayings.add("STOP IT!!");
        FaceSayings.add("Can you ask me next time before you touch me?");
        FaceSayings.add("Hahahaha!");
        FaceSayings.add("Ugh");
        FaceSayings.add("Pppttbbbb");

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

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public ArrayList<String> getMovmentSayings() {
        return MovmentSayings;
    }

    public ArrayList<String> getChatSayings() {
        return ChatSayings;
    }

    public ArrayList<String> getFaceSayings() {
        return FaceSayings;
    }

    public Boolean getVoiceControlled() {
        return voiceControlled;
    }

    public void setVoiceControlled(Boolean voiceControlled) {
        this.voiceControlled = voiceControlled;
    }


}
