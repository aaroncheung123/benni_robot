package com.aaroncheung.prototype4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aaroncheung.prototype4.robot.RobotFacade;
import com.aaroncheung.prototype4.robot.SpeechRecognition;
import com.aaroncheung.prototype4.states.RobotState;


public class HappyStateActivity extends SpeechRecognition {

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

        RobotState.getInstance().setState(new com.aaroncheung.prototype4.states.HappyState());
        robotState = RobotState.getInstance();
        robotFacade = RobotFacade.getInstance();

        begin();

    }

    @Override
    public void processSpeech(String message) {
        Log.d(TAG, "process speech: " + message);
        if(message.contains("move forward")){
            Log.d(TAG, "move forward");
            RobotState.getInstance().moveForward();
            startListening();
        }
        else if(message.contains("move back")){
            RobotState.getInstance().moveBackward();
            startListening();
        }
        else if(message.contains("turn right")){
            RobotState.getInstance().turnRight();
            startListening();
        }
        else if(message.contains("turn left")){
            RobotState.getInstance().turnLeft();
            startListening();
        }
    }


    private void begin(){
        robotState.explain();
        startListening();
    }

    public void happyStateFaceClick(View view) {
        startListening();
    }


}
