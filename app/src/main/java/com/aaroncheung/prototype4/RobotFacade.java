package com.aaroncheung.prototype4;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;

import com.felhr.usbserial.UsbSerialDevice;

public class RobotFacade extends ContextWrapper {

    public final String TAG = "debug_main3";


    Context context;
    ArduinoCommunicator arduinoCommunicator;

    public RobotFacade(Context context, UsbManager usbManager) {
        super(context);
        this.context = context;

        arduinoCommunicator = new ArduinoCommunicator(context, usbManager);

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

    private void sendArduino(){

    }


}
