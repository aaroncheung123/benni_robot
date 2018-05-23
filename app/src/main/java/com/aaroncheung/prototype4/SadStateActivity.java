package com.aaroncheung.prototype4;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aaroncheung.prototype4.Hardware.RobotFacade;
import com.aaroncheung.prototype4.States.HappyState;
import com.aaroncheung.prototype4.States.RobotState;
import com.aaroncheung.prototype4.States.SadState;

public class SadStateActivity extends Activity {

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
        setContentView(R.layout.activity_sad_state);

        //--------------------------------------------------

        RobotState.getInstance().setState(new SadState());
        robotState = RobotState.getInstance();
        robotFacade = RobotFacade.getInstance();

        begin();

    }

    private void begin(){
        Log.d(TAG, "SAD_STATE_ACTIVITY 1");
        robotState.explain();
        robotFacade.backward();
        Log.d(TAG, "SAD_STATE_ACTIVITY 2");
    }

    public void sadStateFaceClick(View view){
        Intent myIntent = new Intent(this, HappyStateActivity.class);
        this.startActivity(myIntent);
    }
}
