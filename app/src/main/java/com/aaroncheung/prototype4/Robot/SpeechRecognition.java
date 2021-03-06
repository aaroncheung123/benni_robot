package com.aaroncheung.prototype4.Robot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.aaroncheung.prototype4.Helper.UserInformationSingleton;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

public class SpeechRecognition extends Activity implements RecognitionListener {

    public final static String TAG = "speech_rec";
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    private UserInformationSingleton userInformationSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        //Connecting to Socket IO
        Log.d(TAG, "SOCKET INIT");
        userInformationSingleton = UserInformationSingleton.getInstance();
        socket.connect();
        email = userInformationSingleton.getEmail();
        socket.on(email, handleIncomingMessages);
    }


    //--------------------------------------------------

    //  SOCKET IO

    //--------------------------------------------------

    private String url = UserInformationSingleton.getInstance().getSERVER_URL();
    private String email = UserInformationSingleton.getInstance().getEmail();

    private Socket socket;
    {
        Log.d(TAG, "Connecting to socket" + email);
        try {
            socket = IO.socket(url);
        } catch (URISyntaxException e) {}
    }

    private Emitter.Listener handleIncomingMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            SpeechRecognition.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, args[0].toString());
                    processSocketIOCommands(args[0].toString());
                }
            });
        }
    };

    public void attemptSend(String message) throws JSONException {
        email = UserInformationSingleton.getInstance().getEmail();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        String finalMessage = email + ":" + message;
        Log.d(TAG, finalMessage);
        socket.emit("message", finalMessage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userInformationSingleton.setContext(null);
        socket.disconnect();
    }

    public void processSocketIOCommands(String command){}

    //--------------------------------------------------


    //--------------------------------------------------

    public void startListening(){
        Map<String, Object> context = userInformationSingleton.getContext();
//        if(context != null) {
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(this, "this is the context: " + context.toString(), duration);
//            toast.show();
//        }
        Log.d(TAG, "startListening");
        userInformationSingleton.setChatting(true);
        ActivityCompat.requestPermissions
                (this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_PERMISSION);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        userInformationSingleton.setChatting(false);
        super.onStop();
        if (speech != null) {
            speech.destroy();
            userInformationSingleton.setContext(null);
            Log.i(TAG, "destroy");
        }
    }


    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(TAG, "FAILED " + errorMessage);
        userInformationSingleton.setChatting(false);
        try {
            attemptSend("stop listening");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(TAG, "onReadyForSpeech");
    }



    @Override
    public void onResults(Bundle results) {
        Log.i(TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.i(TAG, matches.get(0));
        String message = matches.get(0);
        processSpeech(message);
    }

    public void processSpeech(String message){
        userInformationSingleton.setLastResponse(message);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i("rms", "onRmsChanged: " + rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


}
