package com.aaroncheung.prototype4;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    public final static String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    public final static String TAG = "debug_main654";
    private final static int MAIN_FACE_REQUEST_CODE = 123;
    public static final String KEY_MESSAGE = "msg";
    private boolean permissionGranted = false;
    //Button faceButton;
    ImageView faceView;

    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data;
            Log.d(TAG, "onReceivedData 1");
            try {
                data = new String(arg0, "UTF-8");
                Log.d(TAG, "************ " + data);
                //data = data.concat("\n");
                //tvAppend(textView, data);
                //tvAppend(textView, "test1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }
    };
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "broadcast 1");
            if (intent.getAction() != null && intent.getAction().equals(ACTION_USB_PERMISSION)) {
                Log.d(TAG, "broadcast 2");
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);

                if (granted) {
                    Log.d(TAG, "broadcast 3");
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        Log.d(TAG, "broadcast 4");
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            permissionGranted = true;
                            Log.d(TAG, "broadcast 5");
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);
                            //tvAppend(textView,"Serial Connection Opened!\n");
                            Toast.makeText(MainActivity.this,
                                    "Serial Connection Opened", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Log.d(TAG, "broadcast 6");
                onClickStart();
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Log.d(TAG, "broadcast 7");
                onClickStop();

            }
            Log.d(TAG, "broadcast 8");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate was created ");
        usbManager = (UsbManager) getSystemService(USB_SERVICE);
        //faceButton = findViewById(R.id.faceButton);
        faceView = findViewById(R.id.multiFace);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);
    }


    public void onClickStart() {
        Log.d(TAG, "start 1");
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2341)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }

        Log.d(TAG, "start 2");
    }

    public void onClickSend() {
        Log.d(TAG, "onClickSend was clicked");
        String string = "working";
        Log.d(TAG, "onClickSend string: " + string);
        serialPort.write(string.getBytes());
    }

    public void sender(String s){
        Log.d(TAG, "sender was clicked: " + s);
        serialPort.write(s.getBytes());
        Log.d(TAG, "sender was clicked end");
    }

    public void onClickStop() {
        serialPort.close();
        //tvAppend(textView,"\nSerial Connection Closed! \n");
        Toast.makeText(MainActivity.this,
                "Serial Connection Closed", Toast.LENGTH_SHORT).show();
    }


    public void faceClick(View view){

//        Intent i = new Intent(this, MainFace.class);
//        startActivityForResult(i, MAIN_FACE_REQUEST_CODE);
//        RobotFacade robot = RobotFacade.getInstance(this);
//        robot.onClickSend("testing");
        Log.d(TAG, "face click 1");
        onClickStart();
        if(permissionGranted){
            sender("hi");
        }

        Log.d(TAG, "face click 2");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == MAIN_FACE_REQUEST_CODE && resultCode == RESULT_OK){
            String myMessage = data.getStringExtra(KEY_MESSAGE);
            Log.d(TAG, "Message: " + myMessage);
        }
    }
}

