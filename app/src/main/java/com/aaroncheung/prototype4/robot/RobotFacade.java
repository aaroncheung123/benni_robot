package com.aaroncheung.prototype4.robot;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

public class RobotFacade{

    public String TAG = "debug_main4";

    Context context;
    Motors motors;
    Speech speech;

    private static RobotFacade instance = null;

    private RobotFacade() {
        // Exists only to defeat instantiation.
    }

    public static RobotFacade getInstance() {
        if(instance == null) {
            instance = new RobotFacade();
        }
        return instance;
    }

    public void init(Context context, UsbManager usbManager){
        Log.d(TAG, "2");
        this.context = context;
        motors = new Motors(context, usbManager);
        speech = new Speech(context);

        Log.d(TAG, "4");
        motors.init();
    }


    public void say(final String robotFacadeMessage){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "say1");
                speech.say(robotFacadeMessage);
            }
        }, 500);
    }

    public boolean getPermission(){
        return motors.getPermission();
    }

    public void forward(){
        motors.sendArduino("w");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                motors.sendArduino("z");
            }
        }, 7000);
    }
    public void backward(){
        motors.sendArduino("s");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                motors.sendArduino("z");
            }
        }, 7000);
    }
    public void left(){
        motors.sendArduino("a");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                motors.sendArduino("z");
            }
        }, 7000);
    }
    public void right(){
        motors.sendArduino("d");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                motors.sendArduino("z");
            }
        }, 7000);
    }
    public void stop(){
        motors.sendArduino("z");
    }


}
