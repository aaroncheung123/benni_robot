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
import android.widget.ImageView;
import android.widget.Toast;

import com.aaroncheung.prototype4.Networking.HttpRequest;
import com.aaroncheung.prototype4.Networking.UserInformationSingleton;
import com.aaroncheung.prototype4.robot.RobotFacade;
import com.aaroncheung.prototype4.states.RobotState;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    public final static String TAG = "debug_123";
    private UsbManager usbManager;
    private RobotFacade robotFacade;
    private RobotState robotState;

    HttpRequest httpRequest;

    private EditText emailLoginEditText;
    private EditText passwordEditText;

    private String email;
    private String password;

    ImageView faceView;

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
        robotState = RobotState.getInstance();

        usbManager = (UsbManager) getSystemService(USB_SERVICE);
        Log.d(TAG, "1");
        robotFacade = RobotFacade.getInstance();

        robotFacade.init(this, usbManager);
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
            Toast.makeText(this, "Please Connect to Wifi",
                    Toast.LENGTH_LONG).show();
        }
        else{
            JSONObject jsonObjectInfo = (JSONObject) jsonObject.get("info");
            JSONObject jsonObjectProgress = (JSONObject) jsonObject.get("progressNumbers");
        }


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

                startActivity(new Intent(LoginActivity.this, HappyStateActivity.class));
                //startActivity(new Intent(LoginActivity.this, ChatActivity.class));
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

