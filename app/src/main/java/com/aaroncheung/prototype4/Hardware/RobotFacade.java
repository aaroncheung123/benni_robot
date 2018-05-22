package com.aaroncheung.prototype4.Hardware;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

public class RobotFacade {

    public final String TAG = "debug_main4";

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
        this.context = context;
        motors = new Motors(context, usbManager);
        speech = new Speech(context);
        start();
    }

    public boolean start(){
        if(motors.onClickStart()){
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
        else{
            return false;
        }
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
