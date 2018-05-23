package com.aaroncheung.prototype4;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.aaroncheung.prototype4.Hardware.RobotFacade;
import com.aaroncheung.prototype4.States.HappyState;
import com.aaroncheung.prototype4.States.RobotState;
import com.aaroncheung.prototype4.States.SadState;

public class HappyStateActivity extends Activity {

    public final static String TAG = "debug_main4";
    private RobotState robotState;
    private RobotFacade robotFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This makes it fullscreen mode!!!!
        //--------------------------------------------------

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_happy_state);

        //--------------------------------------------------

        RobotState.getInstance().setState(new HappyState());
        robotState = RobotState.getInstance();
        robotFacade = RobotFacade.getInstance();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                begin();
            }
        }, 2000);

    }

    private void begin(){
        Log.d(TAG, "HAPPYSTATEACTIVITY1");
        robotState.explain();
        robotFacade.forward();
        Log.d(TAG, "HAPPYSTATEACTIVITY2");
    }
}
