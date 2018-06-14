package com.aaroncheung.prototype4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aaroncheung.prototype4.robot.RobotFacade;
import com.aaroncheung.prototype4.robot.SpeechRecognition;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

public class ChatActivity extends SpeechRecognition {

    public final static String TAG = "debug_123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //--------------------------------------------------
        //This makes it fullscreen mode!!!!
        //--------------------------------------------------

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //************************

        setContentView(R.layout.activity_chat);

    }

    public void chatFaceClick(View view){
        startListening();
    }

    @Override
    public void processSpeech(String message) {

        //--------------------------------------------------
        //IBM ASSISTANT CODE
        //--------------------------------------------------
        Log.d(TAG, message);

        final ConversationService myConversationService =
                new ConversationService(
                        "2018-06-14",
                        getString(R.string.username),
                        getString(R.string.password)
                );


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
                                RobotFacade.getInstance().say(outputText);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {}
                });


        //****
    }
}
