package com.aaroncheung.prototype4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.aaroncheung.prototype4.States.HappyState;
import com.aaroncheung.prototype4.States.RobotState;
import com.aaroncheung.prototype4.States.SadState;

public class HappyStateActivity extends Activity {

    private RobotState robotState;

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
        begin();
    }

    private void begin(){
        robotState.explain();
    }
}
