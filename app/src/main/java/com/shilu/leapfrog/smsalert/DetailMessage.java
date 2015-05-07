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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Locale;

import adapter.MessageDetailsListAdapter;
import common.Constants;
import data.Contract;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/24/15
 */
public class DetailMessage extends ActionBarActivity implements TextToSpeech.OnInitListener, LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = DetailMessage.class.getSimpleName();
    private MessageDetailsListAdapter mAdapter;
    private TextToSpeech mTextToSpeech;
    private String mMessageId;

    private Toolbar toolbar;
    private ListView listview;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);

        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);

        Bundle mBundle = getIntent().getExtras();
        mMessageId = mBundle.getString(Constants.MESSAGE_ID);
        String mContactName = mBundle.getString(Constants.CONTACT_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mContactName);

        mTextToSpeech = new TextToSpeech(DetailMessage.this, this);
        listview = (ListView) findViewById(R.id.message_detail_listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mProgressBar.setVisibility(View.VISIBLE);
                Cursor mCursor = (Cursor) parent.getItemAtPosition(position);
                String mMessage = mCursor.getString(mCursor.getColumnIndex(Contract.MessageEntry.MESSAGE_BODY));
                speakUp(mMessage);
            }
        });

        getSupportLoaderManager().initLoader(2, null, this);

    }

    public void speakUp(String mMessage) {
        mTextToSpeech.speak(mMessage, TextToSpeech.QUEUE_FLUSH, null);
        mProgressBar.setVisibility(View.GONE);
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
            mProgressBar.setVisibility(View.VISIBLE);
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
                Log.v(TAG, "Language is not available.");
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri mUri = Contract.MessageDetailEntry.CONTENT_URI;

        return new CursorLoader(this, mUri, null,
                Contract.MessageDetailEntry.MESSAGES_TABLE_ID + " =? ",
                new String[]{mMessageId}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {
            mAdapter = new MessageDetailsListAdapter(getApplicationContext(), data, false);
            listview.setAdapter(mAdapter);
        }

        mAdapter.changeCursor(data);

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }
}
