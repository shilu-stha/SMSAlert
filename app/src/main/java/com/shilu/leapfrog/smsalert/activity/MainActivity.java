package com.shilu.leapfrog.smsalert.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import com.shilu.leapfrog.smsalert.R;
import com.shilu.leapfrog.smsalert.adapter.MessagesListAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.shilu.leapfrog.smsalert.components.Constants;
import com.shilu.leapfrog.smsalert.services.SMSObserver;
import com.shilu.leapfrog.smsalert.data.Contract;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/15/15
 */
public class MainActivity extends ActionBarActivity implements LoaderCallbacks<Cursor>, TextToSpeech.OnInitListener,ListView.OnItemClickListener {

    private String TAG = MainActivity.class.getSimpleName();
    private static TextToSpeech sTextToSpeech;
    private MessagesListAdapter mAdapter;

    @InjectView(R.id.messages_listview)
    ListView listview;
    @InjectView(R.id.text_welcome)
    TextView welcomeMessage;
    @InjectView(R.id.text_description)
    TextView descriptionText;
    @InjectView(R.id.app_bar)
    Toolbar toolbar;
    @InjectView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);

        sTextToSpeech = new TextToSpeech(MainActivity.this, this);

        SMSObserver smsObserver = (new SMSObserver(new Handler(), this));
        ContentResolver contentResolver = this.getContentResolver();
        contentResolver.registerContentObserver(Uri.parse(Constants.SMS_CONTENT_PROVIDER_URI), true, smsObserver);

        getSupportLoaderManager().initLoader(1, null, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().restartLoader(1, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (sTextToSpeech != null) {
            sTextToSpeech.stop();
            sTextToSpeech.shutdown();
        }

        ButterKnife.reset(this);
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
            mProgressBar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(1, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Contract.MessageEntry.CONTENT_URI;

        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            listview.setVisibility(View.INVISIBLE);
        } else {
            descriptionText.setVisibility(View.INVISIBLE);
            welcomeMessage.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
            mAdapter = new MessagesListAdapter(getApplicationContext(), data, false);
            listview.setAdapter(mAdapter);
        }
        mAdapter.changeCursor(data);

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = sTextToSpeech.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                Toast.makeText(getApplicationContext(), Constants.ERROR_MESSAGE_LANGUAGE_UNAVAILABLE, Toast.LENGTH_LONG).show();
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                Log.v(TAG, Constants.ERROR_MESSAGE_LANGUAGE_UNAVAILABLE);
            } else {
                SharedPreferences mPref = getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE);
                boolean firstIn = mPref.getBoolean(Constants.FIRST_IN, true);

//              for welcome speech message
                if (firstIn) {
                    listview.setVisibility(View.INVISIBLE);
                    descriptionText.setVisibility(View.VISIBLE);
                    welcomeMessage.setVisibility(View.VISIBLE);

                    sTextToSpeech.speak(Constants.WELCOME_TEXT, TextToSpeech.QUEUE_FLUSH, null);
                    mPref.edit().putBoolean(Constants.FIRST_IN, false).commit();
                }
            }
        }
    }

    @Override
    @OnItemClick(R.id.messages_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mProgressBar.setVisibility(View.VISIBLE);

        Cursor mCursor = (Cursor) parent.getItemAtPosition(position);
        String mMessageId = mCursor.getString(mCursor.getColumnIndex(Contract.MessageEntry.CONTACTS_NUMBER));
        String mContactName = mCursor.getString(mCursor.getColumnIndex(Contract.MessageEntry.CONTACTS_NAME));

        if (TextUtils.isEmpty(mContactName)) {
            mContactName = mMessageId;
        }
        Intent mIntent = new Intent(getApplicationContext(),DetailMessageActivity.class);
        mIntent.putExtra(Constants.MESSAGE_ID, mMessageId);
        mIntent.putExtra(Constants.CONTACT_NAME, mContactName);
        startActivity(mIntent);

    }
}
