package com.aaroncheung.prototype4;

import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

public class RobotFacade extends ContextWrapper {

    public final String TAG = "debug_main3";


    Context context;
    ArduinoCommunicator arduinoCommunicator;
    Speech speech;

    public RobotFacade(Context context, UsbManager usbManager) {
        super(context);
        this.context = context;

        arduinoCommunicator = new ArduinoCommunicator(context, usbManager);
        speech = new Speech(context);
    }

    public boolean start(){
        if(arduinoCommunicator.onClickStart()){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "robot facade start");
                    arduinoCommunicator.sendArduino("x");
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

    private void sendArduino(){

    }


}
