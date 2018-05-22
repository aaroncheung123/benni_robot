package com.aaroncheung.prototype4;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aaroncheung.prototype4.Hardware.RobotFacade;
import com.aaroncheung.prototype4.States.HappyState;
import com.aaroncheung.prototype4.States.RobotState;

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
        robotFacade.start();
        Log.d(TAG, "fc1");

        Intent myIntent = new Intent(this, HappyStateActivity.class);
        this.startActivity(myIntent);
    }
}

