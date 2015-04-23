package com.shilu.leapfrog.smsalert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by shilushrestha on 4/16/15.
 */
public class TextToSpeechService extends Service implements TextToSpeech.OnInitListener{
    TextToSpeech textToSpeech;
    private Context context;
    String receivedText = null;
    String sender = null;
    boolean ttsReady = false;
    boolean msgReady = false;
    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
         textToSpeech = new TextToSpeech(this,this);
     }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        receivedText = bundle.getString("SMSAlert_Message");
        sender = bundle.getString("SMSAlert_Sender");
        if(receivedText!=null){
            msgReady = true;
        }
        speakUp();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

     @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    public void speakUp(){
        if(msgReady && ttsReady){
            textToSpeech.speak(receivedText, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int status) {
        Log.d("SERVICE", "onInit called");
        if(status== TextToSpeech.SUCCESS) {
            int result =textToSpeech.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language is not available.", Toast.LENGTH_LONG).show();
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                Log.v("MAIN", "Language is not available.");
            } else {
                ttsReady = true;
                speakUp();
            }
        }
    }
}
