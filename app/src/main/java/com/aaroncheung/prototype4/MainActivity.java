package com.aaroncheung.prototype4;

import android.app.Activity;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {
    public final static String TAG = "debug_main4";
    private UsbManager usbManager;
    private RobotFacade robotFacade;
    private RobotState robotState;

    ImageView faceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This makes it fullscreen mode!!!!
        //--------------------------------------------------

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //--------------------------------------------------

        Log.d(TAG, "onCreate was created ");
        faceView = findViewById(R.id.multiFace);
        UsbManager usbManager = (UsbManager) this.getSystemService(this.USB_SERVICE);
    }


    public void faceClick(View view){
        usbManager = (UsbManager) getSystemService(USB_SERVICE);
        robotFacade = RobotFacade.getInstance();
        robotFacade.init(this, usbManager);

        robotState = RobotState.getInstance();
        robotState.explain();


//        robotFacade.say("Hello my name is Nelly");
//        Log.d(TAG, "face click 1");
//        if(robotFacade.start()){
//            Log.d(TAG, "Startup Succeeded");
//        }
//        else{
//            Log.d(TAG, "Startup Failed");
//        }
    }
}

