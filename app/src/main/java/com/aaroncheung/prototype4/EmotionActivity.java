package com.aaroncheung.prototype4;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aaroncheung.prototype4.Robot.RobotFacade;
import com.aaroncheung.prototype4.Robot.SpeechRecognition;
import com.aaroncheung.prototype4.States.RobotState;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import org.json.JSONException;



public class EmotionActivity extends SpeechRecognition {

    public final static String TAG = "debug_123";
    private RobotState robotState;
    private ConversationService myConversationService = null;
    private ImageView displayFace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //--------------------------------------------------
        //
        // This makes it fullscreen mode!!!!
        //
        //--------------------------------------------------

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //************************


        //************************

        setContentView(R.layout.activity_happy_state);
        displayFace = findViewById(R.id.displayFace);

        //IBM
        myConversationService =
                new ConversationService(
                        "2018-06-14",
                        getString(R.string.username),
                        getString(R.string.password)
                );


        RobotState.getInstance().setState(new com.aaroncheung.prototype4.States.HappyState());
        robotState = RobotState.getInstance();
    }

    //--------------------------------------------------
    //
    // Speech recognition processes the speech
    //
    //--------------------------------------------------
    @Override
    public void processSpeech(String command) {
        Log.d(TAG, "process speech: " + command);

        processMovement(command);
        if(processListenCommand(command) == false){
            IBMProcessSpeech(command);
        }

    }

    //************************


    //************************


    //--------------------------------------------------
    //
    // Socket IO processes the speech
    //
    //--------------------------------------------------
    @Override
    public void processSocketIOCommands(String command) {
        Log.d(TAG, "process command: " + command);
        processListenCommand(command);
        processMathGameCommand(command);
        processEmotions(command);
        processMovement(command);
    }

    //--------------------------------------------------
    //
    // Start or Stop listening
    //
    //--------------------------------------------------
    public boolean processListenCommand(String command){
        if(command.matches("listen")){
            startListening();
            return true;
        }
        else if(command.matches("stop listening")){
            RobotFacade.getInstance().say("Ok I will stop listening");
            onPause();
            return true;
        }
        return false;
    }

    public void processMathGameCommand(String command){
        if(command.matches("Math1")){
            RobotFacade.getInstance().say("Hi, can you help me with my math homework");
        }
        else if(command.matches("CorrectMath")){
            RobotFacade.getInstance().say("Good Job! You got it Right!");
        }
    }

    //--------------------------------------------------
    //
    // Changes emotions based on emotional state
    //
    //--------------------------------------------------
    public void processEmotions(String emotion){
        if(emotion.matches("Happy")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace.setImageResource(R.drawable.happyface);
            RobotFacade.getInstance().say("I am happy");
        }
        else if(emotion.matches("Bored")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace.setImageResource(R.drawable.neutral);
            RobotFacade.getInstance().say("I am bored");
        }
        else if(emotion.matches("Sad")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace.setImageResource(R.drawable.sadface);
            RobotFacade.getInstance().say("I am sad");
        }
        else if(emotion.matches("Mad")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace.setImageResource(R.drawable.angry);
            RobotFacade.getInstance().say("I am mad");
        }
        else if(emotion.matches("Broken")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace.setImageResource(R.drawable.tired);
            RobotFacade.getInstance().say("I am broken");
        }
    }

    //--------------------------------------------------
    //
    // Drive controls
    //
    //--------------------------------------------------
    public void processMovement(String movement){
        if(movement.matches("forward")){
            robotState.moveForward();
        }
        else if(movement.matches("backward")){
            robotState.moveBackward();
        }
        else if(movement.matches("right")){
            robotState.turnRight();
        }
        else if(movement.matches("left")){
            robotState.turnLeft();
        }
        else if(movement.matches("stop")){
            robotState.stop();
        }
    }


    //--------------------------------------------------
    //
    // IBM ASSISTANT CODE
    //
    //--------------------------------------------------

    public void IBMProcessSpeech(String message){

        Log.d(TAG, "IBM Process Speech" + message);
        MessageRequest request = new MessageRequest.Builder()
                .inputText(message)
                .build();


        myConversationService
                .message(getString(R.string.workspace), request)
                .enqueue(new ServiceCallback<MessageResponse>() {
                    @Override
                    public void onResponse(MessageResponse response) {
                        // More code here
                        final String outputText = response.getText().get(0);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, outputText);
                                TextToSpeech mTTs = RobotFacade.getInstance().say(outputText);

                                //Sending a +1 for chatProgress score
                                try {
                                    attemptSend("add chatProgress");
                                    attemptSend("listening");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                while(mTTs.isSpeaking()){
                                    continue;
                                }
                                startListening();
//                                final Handler handler = new Handler();
//                                        handler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//
//                                            }
//                                        }, 4500);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {}
                });
    }

    //************************


    //************************


    public void happyStateFaceClick(View view) {
        RobotFacade.getInstance().say("Hey that tickeled");
    }


}
