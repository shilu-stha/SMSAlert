package com.shilu.leapfrog.smsalert;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import adapter.MessagesListAdapter;
import common.SMSObserver;
import data.MessageContract;


public class MainActivity extends ActionBarActivity implements LoaderCallbacks<Cursor>/*,TextToSpeech.OnInitListener*/ {

    private static TextToSpeech textToSpeech;
    private String message = "Welcome to SMS Alert!";
    private ListView messagesListview;
    private TextView welcomeMessage;
    private TextView descriptionText;
    private Toolbar toolbar;
    private MessagesListAdapter adapter;

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

        SMSObserver smsObserver = (new SMSObserver(new Handler(), this));
        ContentResolver contentResolver = this.getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms"), true, smsObserver);

//        DBHelper dbHelper = new DBHelper(getApplicationContext());
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        deleteDatabase(DBHelper.DATABASE_NAME);

        messagesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String messageId = cursor.getString(cursor.getColumnIndex("contacts_number"));
                String contactName = cursor.getString(cursor.getColumnIndex("contacts_name"));
                if(contactName.equals(null)||contactName.equals("")){
                    contactName = messageId;
                }
                startActivity(new Intent(getApplicationContext(), DetailMessage.class).putExtra("MessageId", messageId).putExtra("ContactsName", contactName));

            }
        });

        getSupportLoaderManager().initLoader(1, null, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(1, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
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
            getSupportLoaderManager().restartLoader(1, null, this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MessageContract.MessageEntry.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            messagesListview.setVisibility(View.INVISIBLE);
        } else {
            descriptionText.setVisibility(View.INVISIBLE);
            welcomeMessage.setVisibility(View.INVISIBLE);
            messagesListview.setVisibility(View.VISIBLE);
            adapter = new MessagesListAdapter(getApplicationContext(), null);
            messagesListview.setAdapter(adapter);
        }
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
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

}
