package com.aaroncheung.prototype4;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.aaroncheung.prototype4.Helper.BatteryService;
import com.aaroncheung.prototype4.Networking.HttpRequest;
import com.aaroncheung.prototype4.Helper.UserInformationSingleton;
import com.aaroncheung.prototype4.Robot.RobotFacade;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    public final static String TAG = "debug_123";
    private RobotFacade robotFacade;
    HttpRequest httpRequest;

    private EditText emailLoginEditText;
    private EditText passwordEditText;
    private Intent BatteryServiceIntent;



    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This makes it fullscreen mode!!!!
        //--------------------------------------------------

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //--------------------------------------------------

        emailLoginEditText = findViewById(R.id.emailLoginEditText);
        passwordEditText = findViewById(R.id.passwordLoginEditText);
        Log.d(TAG, "Login onCreate");
        httpRequest = new HttpRequest(this);


        Log.d(TAG, "onCreate was created ");
        //faceView = findViewById(R.id.multiFace);
        UsbManager usbManager = (UsbManager) this.getSystemService(this.USB_SERVICE);

        usbManager = (UsbManager) getSystemService(USB_SERVICE);
        Log.d(TAG, "1");
        robotFacade = RobotFacade.getInstance();

        robotFacade.init(this, usbManager);

        //STARTING SERVICE TIMER
        BatteryServiceIntent = new Intent(getApplicationContext(), BatteryService.class);
        startService(BatteryServiceIntent);

    }



    public void loginButtonClick(View view){
        httpRequest.sendLoginGetRequest(emailLoginEditText.getText().toString());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    loginCheck();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }

    public void loginCheck() throws JSONException {
        JSONObject jsonObject = httpRequest.getMyJSONObject();
        if(jsonObject == null){
            Toast.makeText(this, "Connect to wifi or wait 5 seconds and hit login again",
                    Toast.LENGTH_LONG).show();
        }
        else{
            JSONObject jsonObjectInfo = (JSONObject) jsonObject.get("info");

            //CHECKING IF ACCOUNT EXISTS
            if(jsonObject != null){
                String databasePassword = jsonObjectInfo.get("password").toString();
                password = passwordEditText.getText().toString();

                //CHECKING IF PASSWORDS MATCH
                if(password.matches(databasePassword)){
                    Toast.makeText(this, "Login Successful",
                            Toast.LENGTH_LONG).show();


                    //INITIALIZING SINGLETON INFORMATION
                    UserInformationSingleton userInfo = UserInformationSingleton.getInstance();
                    userInfo.setEmail(jsonObjectInfo.get("email").toString());

                    startActivity(new Intent(LoginActivity.this, EmotionActivity.class));
                }
                else{
                    Toast.makeText(this, "Wrong Password",
                            Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, "Account Does Not Exist",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}

