package com.aaroncheung.prototype4.Helper;

import android.app.Service;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;

import com.aaroncheung.prototype4.Robot.RobotFacade;
import com.aaroncheung.prototype4.Robot.SpeechRecognition;

import org.json.JSONException;

public class BatteryService extends Service {

    private UserInformationSingleton userInformationSingleton;
    private SpeechRecognition speechRecognition;

    //BATTERY
    private BatteryManager batteryManager;
    private int batteryLevel;


    public BatteryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        speechRecognition = new SpeechRecognition();

        sendRobotHeadBattery();
        getRobotBattery();
        sendRobotBattery();
        return super.onStartCommand(intent, flags, startId);
    }

    //----------------------------------------------
    //
    // Send robot head battery
    //
    //----------------------------------------------
    public void sendRobotHeadBattery(){
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {

                try {
                    batteryManager = (BatteryManager)getSystemService(BATTERY_SERVICE);
                    batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    speechRecognition.attemptSend("head-battery " + String.valueOf(batteryLevel));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                handler.postDelayed(this, 60);


            }
        };
        handler.post(run);
    }

    //----------------------------------------------
    //
    // Get robot battery
    //
    //----------------------------------------------
    public void getRobotBattery(){
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                    RobotFacade robotFacade = RobotFacade.getInstance();
                    robotFacade.getBattery();
                handler.postDelayed(this, 60);
            }
        };
        handler.post(run);
    }

    //----------------------------------------------
    //
    // Send robot battery
    //
    //----------------------------------------------
    public void sendRobotBattery(){
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    speechRecognition.attemptSend("body-battery " + String.valueOf(userInformationSingleton.getBattery()));
                    speechRecognition.attemptSend("min-low-charge " + String.valueOf(userInformationSingleton.getMinInLowCharge()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 60);
            }
        };
        handler.post(run);
    }




}
