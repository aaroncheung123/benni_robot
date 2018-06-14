package com.aaroncheung.prototype4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final TextView conversation = findViewById(R.id.conversation);
        final EditText userInput = findViewById(R.id.user_input);

        final ConversationService myConversationService =
                new ConversationService(
                        "2018-06-14",
                        getString(R.string.username),
                        getString(R.string.password)
                );



        userInput.setOnEditorActionListener(new TextView
                .OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int action, KeyEvent keyEvent) {
                if(action == EditorInfo.IME_ACTION_DONE) {
                    // More code here
                    final String inputText = userInput.getText().toString();

                    conversation.append(
                            Html.fromHtml("<p><b>You:</b> " + inputText + "</p>")
                    );

                    // Optionally, clear edittext
                    userInput.setText("");

                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(inputText)
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
                                            conversation.append(
                                                    Html.fromHtml("<p><b>Bot:</b> " +
                                                            outputText + "</p>")
                                            );
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Exception e) {}
                            });
                }
                return false;
            }
        });










    }








}
