package com.shilu.leapfrog.smsalert;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import adapter.MessagesListAdapter;
import data.DBHelper;


public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener{

    private static TextToSpeech textToSpeech;
    private String message = "Welcome to SMS Alert!";
    private ListView messagesListview;
     private TextView welcomeMessage;
    private TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messagesListview = (ListView) findViewById(R.id.messagesListview);
        welcomeMessage = (TextView) findViewById(R.id.welcomeText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);

        textToSpeech = new TextToSpeech(MainActivity.this,this);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor messagesCursor =  db.rawQuery("select * from messages", null);

        if(messagesCursor==null){
            messagesListview.setVisibility(View.INVISIBLE);
        }
        else{
            descriptionText.setVisibility(View.INVISIBLE);
            messagesListview.setVisibility(View.VISIBLE);
            MessagesListAdapter adapter = new MessagesListAdapter(getApplicationContext(),messagesCursor);
            messagesListview.setAdapter(adapter);
//          adapter.changeCursor(newCursor);
        }

        messagesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String message = cursor.getString(cursor.getColumnIndex("message_body"));
                speakUp(message);
            }
        });

    }

    public void speakUp(String message){
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
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
        if(status== TextToSpeech.SUCCESS){

            int result =textToSpeech.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA ||result == TextToSpeech.LANG_NOT_SUPPORTED) {

                Toast.makeText(getApplicationContext(),"Language is not available.",Toast.LENGTH_LONG).show();
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                Log.v("MAIN", "Language is not available.");
            }
            else {
                textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


}
