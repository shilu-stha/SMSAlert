package com.shilu.leapfrog.smsalert;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
import common.SMSObserver;
import data.DBHelper;
import data.MessageContract;
import data.MessageDetailContract;


public class MainActivity extends ActionBarActivity /*implements TextToSpeech.OnInitListener */{

    private static TextToSpeech textToSpeech;
    private String message = "Welcome to SMS Alert!";
    private ListView messagesListview;
    private TextView welcomeMessage;
    private TextView descriptionText;
    private Toolbar toolbar;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private MessagesListAdapter adapter;
    private Cursor messagesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        messagesListview = (ListView) findViewById(R.id.messagesListview);
        welcomeMessage = (TextView) findViewById(R.id.welcomeText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);

//        textToSpeech = new TextToSpeech(MainActivity.this, this);

        SMSObserver smsObserver = (new SMSObserver(new Handler(),this));
        ContentResolver contentResolver = this.getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms"), true, smsObserver);

        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

//        Cursor messagesCursor = db.rawQuery("select * from messages", null);
        messagesCursor = db.query(MessageContract.MessageEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        Log.d("MAINACTIVITY", "" + messagesCursor.toString());

        if (messagesCursor == null) {
            messagesListview.setVisibility(View.INVISIBLE);
        } else {
            descriptionText.setVisibility(View.INVISIBLE);
            welcomeMessage.setVisibility(View.INVISIBLE);
            messagesListview.setVisibility(View.VISIBLE);
            adapter = new MessagesListAdapter(getApplicationContext(), messagesCursor);
            messagesListview.setAdapter(adapter);
        }

        messagesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String messageId = cursor.getString(cursor.getColumnIndex("contacts_id"));
                String contactName = cursor.getString(cursor.getColumnIndex("contacts_name"));
                startActivity(new Intent(getApplicationContext(), DetailMessage.class).putExtra("MessageId", messageId).putExtra("ContactsName",contactName));
                cursor.close();
            }
        });

    }

    public void speakUp(String message) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        db.close();
//        messagesCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.refresh) {
            Cursor newCursor = db.query(MessageContract.MessageEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            adapter.changeCursor(newCursor);
        }
        return super.onOptionsItemSelected(item);
    }


   /* @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.ENGLISH);
            Log.v("MAIN", "onInit");

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                Toast.makeText(getApplicationContext(), "Language is not available.", Toast.LENGTH_LONG).show();
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                Log.v("MAIN", "Language is not available.");
            } else {
                textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }*/

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
        db.close();
        messagesCursor.close();
    }


}
