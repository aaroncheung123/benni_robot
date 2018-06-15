package com.aaroncheung.prototype4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aaroncheung.prototype4.robot.SpeechRecognition;
import com.aaroncheung.prototype4.states.RobotState;



public class HappyStateActivity extends SpeechRecognition {

    public final static String TAG = "debug_123";
    private RobotState robotState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This makes it fullscreen mode!!!!
        //--------------------------------------------------

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //--------------------------------------------------

        setContentView(R.layout.activity_happy_state);


        RobotState.getInstance().setState(new com.aaroncheung.prototype4.states.HappyState());
        robotState = RobotState.getInstance();

        begin();

    }

    @Override
    public void processSpeech(String message) {
        Log.d(TAG, "process speech: " + message);

        if(message.contains("move forward")){
            Log.d(TAG, "move forward");
            robotState.moveForward();
            startListening();
        }
        else if(message.contains("move back")){
            robotState.moveBackward();
            startListening();
        }
        else if(message.contains("turn right")){
            robotState.turnRight();
            startListening();
        }
        else if(message.contains("turn left")){
            robotState.turnLeft();
            startListening();
        }
        else if(message.contains("stop moving")){
            robotState.stop();
            startListening();
        }
        else if(message.contains("stop listening")){
            onStop();
        }
        else{
            startListening();
        }
    }

    @Override
    public void processSocketIOCommands(String command) {
        Log.d(TAG, "process move: " + command);
        if(command.matches("chat activity")){
            startActivity(new Intent(HappyStateActivity.this, ChatActivity.class));
        }
        else if(command.matches("forward")){
            Log.d(TAG, "move forward");
            robotState.moveForward();
        }
        else if(command.matches("backward")){
            robotState.moveBackward();
        }
        else if(command.matches("right")){
            robotState.turnRight();
        }
        else if(command.matches("left")){
            robotState.turnLeft();
        }
        else if(command.matches("stop")){
            robotState.stop();
        }
        else if(command.matches("listen")){
            startListening();
        }
        else if(command.matches("stop listening")){
            onStop();
        }

    }

    private void begin(){
        Log.d(TAG, "HAPPY ACTIVITY");
        //robotState.explain();
        //startListening();
    }

    public void happyStateFaceClick(View view) {
        //startListening();
    }


}
