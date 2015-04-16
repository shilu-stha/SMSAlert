package com.shilulf.smsalert.smsalert;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech textToSpeech;
    private String message = "Welcome!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textToSpeech = new TextToSpeech(MainActivity.this,this);
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak("Hello There!!", TextToSpeech.QUEUE_FLUSH, null);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInit(int status) {
        Log.d("MAIN", "INIT"+status);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            message = extras.getString("SMSAlert_Message");
        }
        if(status== TextToSpeech.SUCCESS){
            textToSpeech.setLanguage(Locale.ENGLISH);
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
                textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        if(textToSpeech.isSpeaking()){
            Toast.makeText(getApplicationContext(),
                    "true",
                    Toast.LENGTH_LONG).show();
        }
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
}
