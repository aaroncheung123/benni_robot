package com.aaroncheung.prototype4.Helper;

import android.app.Service;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.aaroncheung.prototype4.Robot.RobotFacade;
import com.aaroncheung.prototype4.Robot.SpeechRecognition;

import org.json.JSONException;

public class BatteryService extends Service {
    public final String TAG = "debug_bat_service";
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
        userInformationSingleton = UserInformationSingleton.getInstance();

        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        userInformationSingleton.setHeadBattery(batteryLevel);
        userInformationSingleton.setRobotBattery("i");
        userInformationSingleton.setMinInLowCharge(0);

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
        if(!userInformationSingleton.getChatting()) {
            Log.d(TAG, "Sending phone battery");

            final Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {

                    try {
                        batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
                        batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                        Log.d(TAG, String.valueOf(batteryLevel));
                        speechRecognition.attemptSend("headBattery-" + String.valueOf(batteryLevel));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    handler.postDelayed(this, 3600);
                }
            };
            handler.post(run);
        }
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
                sendRobotBattery();
                handler.postDelayed(this, 3600);
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
        if(!userInformationSingleton.getChatting()) {

            final Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        speechRecognition.attemptSend("bodyBattery-" + String.valueOf(userInformationSingleton.getRobotBattery()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handler.postDelayed(this, 3600);
                }
            };
            handler.post(run);
        }
    }


}
