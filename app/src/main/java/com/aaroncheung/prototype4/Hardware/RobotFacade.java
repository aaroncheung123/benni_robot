package com.aaroncheung.prototype4.Hardware;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

public class RobotFacade {

    public final String TAG = "debug_main5";

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
        Log.d(TAG, "init1");
        this.context = context;
        motors = new Motors(context, usbManager);
        speech = new Speech(context);

        Log.d(TAG, "init2");
        motors.init();


//        final boolean notGranted = true;
//        while(notGranted){
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(motors.getPermission()){
//                        notGranted = false;
//                    }
//                }
//            }, 2000);
//        }

        Log.d(TAG, "robot facade start");
        motors.sendArduino("p");


//        if(motors.init()){
//            Log.d(TAG, "robot facade start");
//            motors.sendArduino("p");
//            //start();
//        }
//        else{
//            Log.d(TAG, "Robot Facade Init Fail");
//        }
    }

    public boolean start(){
        Log.d(TAG, "starting1");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "robot facade start");
                motors.sendArduino("start");
            }
        }, 2000);
        return true;
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

    public void forward(){
        motors.sendArduino("w");
    }
    public void backward(){
        motors.sendArduino("s");
    }
    public void left(){
        motors.sendArduino("a");
    }
    public void right(){
        motors.sendArduino("d");
    }
    public void stop(){
        motors.sendArduino("z");
    }


}
