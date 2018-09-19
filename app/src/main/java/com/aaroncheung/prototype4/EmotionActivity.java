package com.aaroncheung.prototype4;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.aaroncheung.prototype4.Helper.UserInformationSingleton;
import com.aaroncheung.prototype4.Robot.OpenCVActivity;
import com.aaroncheung.prototype4.Robot.RobotFacade;
import com.aaroncheung.prototype4.Robot.SpeechRecognition;
import com.aaroncheung.prototype4.States.RobotState;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


public class EmotionActivity extends SpeechRecognition {

    public final static String TAG = "debug_123";
    private RobotState robotState;
    private ConversationService myConversationService = null;
    private ImageView displayFace;
    private UserInformationSingleton userInformationSingleton;
    Random rand = new Random();


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

        userInformationSingleton = UserInformationSingleton.getInstance();

        //************************


        //************************

        setContentView(R.layout.activity_happy_state);
        displayFace = findViewById(R.id.displayFace);

        processEmotions("Happy");

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

        if(userInformationSingleton.getVoiceControlled())
            voiceDrive(command);
        else {
//            RobotFacade.getInstance().say("im no longer voice controlled");
            if (!processMovement(command)) {
                Log.d(TAG, "process speech: stage1");

                if (processListenCommand(command) == false) {
                    Log.d(TAG, "process speech: stage2");

                    if(userInformationSingleton.getIBMChatting()){
                        Log.d(TAG, "process speech: stage3");

                        IBMProcessSpeech(command);
                    }
                    else{
                        userInformationSingleton.setLastResponse(command);
                        try{
                            attemptSend(userInformationSingleton.getLastResponse());
                        }catch(JSONException ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
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
        if(processListenCommand(command))
            return;
        else if(processMathGameCommand(command))
            return;
        else if(processEmotions(command))
            return;
        else if(processMovement(command))
            return;
        else{
            processSpeaking(command);
        }
    }

    //--------------------------------------------------
    //
    // Start or Stop listening
    //
    //--------------------------------------------------
    public boolean processListenCommand(String command){
        if(command.matches("listen")){
            userInformationSingleton.setIBMChatting(true);
            startListening();
            return true;
        }
        else if(command.matches("stop listening")){
            RobotFacade.getInstance().say("Ok");
            onPause();
            return true;
        }
        else if(command.matches("start chat")){
            ArrayList<String> chatSayings = userInformationSingleton.getChatSayings();
            RobotFacade.getInstance().say(chatSayings.get(rand.nextInt(chatSayings.size() - 1)));
            return true;
        }
        return false;
    }

    public boolean processMathGameCommand(String command){
        if(command.matches("Math1")){
            RobotFacade.getInstance().say("Hi, can you help me with my math homework");
            return true;
        }
        else if(command.matches("CorrectMath")){
            RobotFacade.getInstance().say("Good Job! You got it Right!");
            return true;
        }
        return false;
    }

    //--------------------------------------------------
    //
    // Changes emotions based on emotional state
    //
    //--------------------------------------------------
    public boolean processEmotions(String emotion){
        if(emotion.matches("Happy")){
            Log.d(TAG, "process Emotions: " + emotion);

            displayFace = (ImageView)findViewById(R.id.displayFace);
            displayFace.setBackgroundResource(R.drawable.happy_animation);

            // Get the background, which has been compiled to an AnimationDrawable object.
            AnimationDrawable frameAnimation = (AnimationDrawable) displayFace.getBackground();
            frameAnimation.start();
            RobotFacade.getInstance().say("I am so happy");
            displayFace.setBackgroundResource(R.drawable.happy_blink);

            // Get the background, which has been compiled to an AnimationDrawable object.
            frameAnimation = (AnimationDrawable) displayFace.getBackground();
            frameAnimation.start();
            return true;
        }
        else if(emotion.matches("Bored")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace = (ImageView)findViewById(R.id.displayFace);
            displayFace.setBackgroundResource(R.drawable.bored_animations);
            // Get the background, which has been compiled to an AnimationDrawable object.
            AnimationDrawable frameAnimation = (AnimationDrawable) displayFace.getBackground();
            frameAnimation.start();
            RobotFacade.getInstance().say("Ugh I'm bored because no one is playing with me");
            displayFace.setBackgroundResource(R.drawable.blink_bored);
            // Get the background, which has been compiled to an AnimationDrawable object.
            frameAnimation = (AnimationDrawable) displayFace.getBackground();
            frameAnimation.start();
            return true;
        }
        else if(emotion.matches("Sad")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace = (ImageView)findViewById(R.id.displayFace);
            displayFace.setBackgroundResource(R.drawable.sad_animation);
            // Get the background, which has been compiled to an AnimationDrawable object.
            AnimationDrawable frameAnimation = (AnimationDrawable) displayFace.getBackground();
            frameAnimation.start();
            RobotFacade.getInstance().say("I'm so sad that no one wants to talk to me or play with me");

            displayFace.setBackgroundResource(R.drawable.sad_blink);
            // Get the background, which has been compiled to an AnimationDrawable object.
            frameAnimation = (AnimationDrawable) displayFace.getBackground();
            frameAnimation.start();
            return true;
        }
        else if(emotion.matches("Mad")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace.setImageResource(R.drawable.angry);
            RobotFacade.getInstance().say("I am mad, why won't anyone play with me or talk to me");
            return true;
        }
        else if(emotion.matches("Broken")){
            Log.d(TAG, "process Emotions: " + emotion);
            displayFace.setImageResource(R.drawable.tired);
            RobotFacade.getInstance().say("I am broken now");
            return true;
        }
        else if(emotion.matches("talking")){
            Log.d(TAG, "process Emotions: ");
            displayFace = (ImageView)findViewById(R.id.displayFace);
            displayFace.setBackgroundResource(R.drawable.talking_animations);
            // Get the background, which has been compiled to an AnimationDrawable object.
            AnimationDrawable frameAnimation = (AnimationDrawable) displayFace.getBackground();
            frameAnimation.start();
            return true;
        }
        return false;
    }

    //--------------------------------------------------
    //
    // Drive controls
    //
    //--------------------------------------------------
    public boolean processMovement(String movement){
        ArrayList<String> MovementSayings = userInformationSingleton.getMovmentSayings();
        Log.d(TAG, "process Movement: " + movement);
        if(movement.toLowerCase().contains("forward")){
            robotState.moveForward();
            return true;
        }
        else if(movement.toLowerCase().contains("backward") || movement.toLowerCase().contains("back")){
            robotState.moveBackward();
            return true;
        }
        else if(movement.toLowerCase().contains("right") ){
            robotState.turnRight();
            return true;
        }
        else if(movement.toLowerCase().contains("left") ){
            robotState.turnLeft();
            return true;
        }
        else if(movement.toLowerCase().matches("stop") ){
            robotState.stop();
            return true;
        }
        else if(movement.matches("start drive listening")){
            userInformationSingleton.setVoiceControlled(true);
            TextToSpeech mTTs = RobotFacade.getInstance().say("Now you can tell me how to drive! Tell me to move forward, backward, left or right!");
            while (mTTs.isSpeaking()){
                continue;
            }
            startListening();
            return true;
        }
        else if(movement.matches("stop drive listening")){
            userInformationSingleton.setVoiceControlled(false);
            onPause();
            RobotFacade.getInstance().say("Thanks for helping me drive around!");
            return true;
        }
        return false;
    }

    public Boolean voiceDrive(String movement) {
        if (movement.toLowerCase().contains("forward")) {
            robotState.moveForward();
            RobotFacade.getInstance().say("Forward");

            try {
                Thread.sleep(2000);
                robotState.stop();
                startListening();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else if (movement.toLowerCase().contains("backward") || movement.toLowerCase().contains("back")) {
            robotState.moveBackward();
            RobotFacade.getInstance().say("Backward");

            try {
                Thread.sleep(2000);
                robotState.stop();
                startListening();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        } else if (movement.toLowerCase().contains("right")) {
            robotState.turnRight();
            RobotFacade.getInstance().say("Right");

            try {
                Thread.sleep(2000);
                robotState.stop();
                startListening();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        } else if (movement.toLowerCase().contains("left")) {
            RobotFacade.getInstance().say("Left");
            robotState.turnLeft();
            try {
                Thread.sleep(2000);
                robotState.stop();
                startListening();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        else if(movement.toLowerCase().matches("stop") ){
            userInformationSingleton.setVoiceControlled(false);
            RobotFacade.getInstance().say("Thanks for helping me drive around!");
            return true;
        }
        return false;
    }

    //--------------------------------------------------
    //
    // IBM ASSISTANT CODE
    //
    //--------------------------------------------------

    public void IBMProcessSpeech(String message){
        userInformationSingleton.setChatting(true);
        MessageRequest request;
        Log.d(TAG, "IBM Process Speech" + message);
        Map<String, Object> context = userInformationSingleton.getContext();
        if(context == null) {
            request = new MessageRequest.Builder()
                    .inputText(message)
                    .build();
        }
        else {
            request = new MessageRequest.Builder()
                    .inputText(message)
                    .context(context)
                    .build();
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(this, "this is the context: " + context.toString(), duration);
//            toast.show();
        }



        myConversationService
                .message(getString(R.string.workspace), request)
                .enqueue(new ServiceCallback<MessageResponse>() {
                    @Override
                    public void onResponse(final MessageResponse response) {
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
                                userInformationSingleton.setContext(response.getContext());
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
                    public void onFailure(Exception e) {
                    }
                });
    }

    public void processSpeaking(String command) {
        userInformationSingleton.setIBMChatting(false);
        TextToSpeech ttS = RobotFacade.getInstance().say(command);
        boolean speaking = true;
        while(speaking){
            if(!ttS.isSpeaking()){
                startListening();
                speaking = false;
            }
        }

    }


        //************************


    //************************

    private int faceTouches = 0;
    public void happyStateFaceClick(View view) {
        //startActivity(new Intent(EmotionActivity.this, OpenCVActivity.class));
//        if(!userInformationSingleton.getChatting()){
//            IBMProcessSpeech("click on face");
//            processEmotions("talking");
//        }
//        else{
//            faceTouches++;
//            if(faceTouches == 4){
//                ArrayList<String> faceSayings = userInformationSingleton.getFaceSayings();
//                RobotFacade.getInstance().say(faceSayings.get(rand.nextInt(faceSayings.size() - 1)));
//            }
//            else if(faceTouches == 5){
//                ArrayList<String> faceSayings = userInformationSingleton.getFaceSayings();
//                RobotFacade.getInstance().say(faceSayings.get(rand.nextInt(faceSayings.size() - 1)));
//            }
//            else if(faceTouches == 7){
//                IBMProcessSpeech("click on face");
//                faceTouches = 0;
//            }
//        }
//
    }


}
