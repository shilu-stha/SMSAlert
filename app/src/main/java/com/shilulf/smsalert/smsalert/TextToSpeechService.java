package com.shilulf.smsalert.smsalert;

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

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        Log.d("SERVICE","onCreate");
        textToSpeech = new TextToSpeech(context,this);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle bundle = intent.getExtras();
        String msg = bundle.getString("SMSAlert_Message");
        Log.d("SERVICE", msg);
        speakUp(msg);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE", "OnStartCommand");
        Bundle bundle = intent.getExtras();
        String msg = bundle.getString("SMSAlert_Message");
        Log.d("SERVICE", msg);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void speakUp(String str){
        textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH,null);
    }
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            Log.d("TAG", "textToSpeech Destroyed");
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if(status== TextToSpeech.SUCCESS) {
            int result =textToSpeech.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                Log.v("MAIN", "Language is not available.");
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sucessfull intialization of Text-To-Speech engine ",
                        Toast.LENGTH_LONG).show();
                textToSpeech.speak("hello there!!", TextToSpeech.QUEUE_FLUSH, null);
            }
            if(textToSpeech.isSpeaking()){
                Toast.makeText(getApplicationContext(),
                        "true",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
