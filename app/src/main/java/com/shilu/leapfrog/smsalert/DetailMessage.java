package com.shilu.leapfrog.smsalert;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

import adapter.MessageDetailsListAdapter;
import data.MessageDetailContract;


public class DetailMessage extends ActionBarActivity implements TextToSpeech.OnInitListener, LoaderManager.LoaderCallbacks<Cursor> {

    private MessageDetailsListAdapter mAdapter;
    private TextToSpeech mTextToSpeech;
    private String mMessageId;

    private Toolbar toolbar;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        toolbar = (Toolbar) findViewById(R.id.app_bar);

        Bundle mBundle = getIntent().getExtras();
        mMessageId = mBundle.getString("mMessageId");
        String mContactName = mBundle.getString("ContactsName");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mContactName);

        mTextToSpeech = new TextToSpeech(DetailMessage.this, this);
        listview = (ListView) findViewById(R.id.message_detail_listview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor mCursor = (Cursor) parent.getItemAtPosition(position);
                String mMessage = mCursor.getString(mCursor.getColumnIndex("message_body"));
                speakUp(mMessage);
            }
        });

        getSupportLoaderManager().initLoader(2, null, this);

    }

    public void speakUp(String mMessage) {
        mTextToSpeech.speak(mMessage, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTextToSpeech != null) {
            mTextToSpeech.shutdown();
        }
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
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int mResult = mTextToSpeech.setLanguage(Locale.ENGLISH);
            if (mResult == TextToSpeech.LANG_MISSING_DATA || mResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language is not available.", Toast.LENGTH_LONG).show();
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                Log.v("MAIN", "Language is not available.");
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri mUri = MessageDetailContract.MessageDetailEntry.CONTENT_URI;
        return new CursorLoader(this,mUri,null,
                MessageDetailContract.MessageDetailEntry.MESSAGES_TABLE_ID + " =? ",
                new String[]{mMessageId},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            mAdapter = new MessageDetailsListAdapter(getApplicationContext(),data,false);
            listview.setAdapter(mAdapter);
        }

        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);

    }
}
