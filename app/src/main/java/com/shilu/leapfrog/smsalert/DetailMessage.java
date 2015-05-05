package com.shilu.leapfrog.smsalert;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import adapter.MessageDetailsListAdapter;
import adapter.MessagesListAdapter;
import data.DBHelper;
import data.MessageContract;
import data.MessageDetailContract;


public class DetailMessage extends ActionBarActivity implements TextToSpeech.OnInitListener{

    private Toolbar toolbar;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor messageDetail;
    private ListView listview;
    private MessageDetailsListAdapter adapter;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String messageId = bundle.getString("MessageId");
        String contactsName = bundle.getString("ContactsName");

        getSupportActionBar().setTitle(contactsName);

        textToSpeech = new TextToSpeech(DetailMessage.this, this);

        listview = (ListView) findViewById(R.id.message_detail_listview);
//        TextView receivedText = (TextView) findViewById(R.id.receivedText);
//        TextView sentText = (TextView) findViewById(R.id.sendText);

        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        messageDetail = db.query(MessageDetailContract.MessageDetailEntry.TABLE_NAME,
                null,
                MessageDetailContract.MessageDetailEntry.MESSAGES_TABLE_ID + " =? ",
                new String[]{messageId},
                null,
                null,
                null);
        messageDetail.moveToFirst();
        Log.d("DETAILMESSAGE", messageId);

        if (messageDetail != null) {
            adapter = new MessageDetailsListAdapter(getApplicationContext(), messageDetail);
            listview.setAdapter(adapter);
        }

//        messageDetail.moveToFirst();
//        while (messageDetail.isAfterLast()==false){
//
//            String type = messageDetail.getString(messageDetail.getColumnIndexOrThrow(MessageDetailContract.MessageDetailEntry.MESSAGE_TYPE));
//            Log.d("DETAILMESSAGE", "" + messageDetail.getString(messageDetail.getColumnIndexOrThrow(MessageDetailContract.MessageDetailEntry.MESSAGE_BODY)));
//            if(type.equals("SENT")){
//                sentText.setText(messageDetail.getString(messageDetail.getColumnIndexOrThrow(MessageDetailContract.MessageDetailEntry.MESSAGE_BODY)));
//            }
//            else{
//                receivedText.setText(messageDetail.getString(messageDetail.getColumnIndexOrThrow(MessageDetailContract.MessageDetailEntry.MESSAGE_BODY)));
//            }
//            messageDetail.moveToNext();
//        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String message = cursor.getString(cursor.getColumnIndex("message_body"));
                speakUp(message);
//                cursor.close();
             }
        });

    }

    public void speakUp(String message) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        db.close();
        messageDetail.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id== R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                Toast.makeText(getApplicationContext(), "Language is not available.", Toast.LENGTH_LONG).show();
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                Log.v("MAIN", "Language is not available.");
            }
        }
    }
}
