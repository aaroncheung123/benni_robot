package com.aaroncheung.prototype4;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {
    public final static String TAG = "debug_main3";
    private boolean permissionGranted = false;
    private ArduinoCommunicator arduinoCommunicator;
    private UsbManager usbManager;

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
        arduinoCommunicator = new ArduinoCommunicator(this, usbManager);

        Log.d(TAG, "face click 1");

        if(arduinoCommunicator.onClickStart()){

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "face click 2");
                    arduinoCommunicator.onClickSend("n");
                }
            }, 2000);
        }
    }
}

