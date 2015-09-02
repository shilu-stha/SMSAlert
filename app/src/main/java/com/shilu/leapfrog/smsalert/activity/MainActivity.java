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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shilu.leapfrog.smsalert.R;
import com.shilu.leapfrog.smsalert.components.ClickListener;
import com.shilu.leapfrog.smsalert.components.Constants;
import com.shilu.leapfrog.smsalert.components.Messages;
import com.shilu.leapfrog.smsalert.data.Contract;
import com.shilu.leapfrog.smsalert.services.SMSObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.MessagesRecyclerAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/15/15
 */
public class MainActivity extends ActionBarActivity implements LoaderCallbacks<Cursor>, TextToSpeech.OnInitListener, ClickListener {
    private String TAG = MainActivity.class.getSimpleName();
    private static TextToSpeech sTextToSpeech;
    private MessagesRecyclerAdapter mAdapter;

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.text_welcome)
    TextView welcomeMessage;
    @InjectView(R.id.text_description)
    TextView descriptionText;
    @InjectView(R.id.app_bar)
    Toolbar toolbar;
    @InjectView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private List<Messages> mMessageEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

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
        if (data != null) {
            descriptionText.setVisibility(View.INVISIBLE);
            welcomeMessage.setVisibility(View.INVISIBLE);

            mMessageEntryList = updateMessagesList(data);

            mAdapter = new MessagesRecyclerAdapter(mMessageEntryList);
            mAdapter.setClickListener(this);

            recyclerView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.notifyDataSetChanged();
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
                    descriptionText.setVisibility(View.VISIBLE);
                    welcomeMessage.setVisibility(View.VISIBLE);

                    sTextToSpeech.speak(Constants.WELCOME_TEXT, TextToSpeech.QUEUE_FLUSH, null);
                    mPref.edit().putBoolean(Constants.FIRST_IN, false).commit();
                }
            }
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        mProgressBar.setVisibility(View.VISIBLE);

        Messages mMessages = mMessageEntryList.get(position);

        String mMessageId = mMessages.CONTACTS_NUMBER;
        String mContactName = mMessages.CONTACTS_NAME;

        if (TextUtils.isEmpty(mContactName)) {
            mContactName = mMessageId;
        }
        Intent mIntent = new Intent(getApplicationContext(),DetailMessageActivity.class);
        mIntent.putExtra(Constants.MESSAGE_ID, mMessageId);
        mIntent.putExtra(Constants.CONTACT_NAME, mContactName);
        startActivity(mIntent);
    }

    List<Messages> updateMessagesList(Cursor cursor) {
        List<Messages> mList = new ArrayList<Messages>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Messages messageEntry = new Messages();

            messageEntry.MESSAGE_BODY = cursor.getString(cursor.getColumnIndex(Contract.MessageEntry.MESSAGE_BODY));
            messageEntry.CONTACTS_NUMBER = cursor.getString(cursor.getColumnIndex(Contract.MessageEntry.CONTACTS_NUMBER));
            messageEntry.CONTACTS_NAME = cursor.getString(cursor.getColumnIndex(Contract.MessageEntry.CONTACTS_NAME));
            messageEntry.CONTACTS_ID = cursor.getString(cursor.getColumnIndex(Contract.MessageEntry.CONTACTS_ID));
            messageEntry.DATE_TIME = cursor.getString(cursor.getColumnIndex(Contract.MessageEntry.DATE_TIME));

            mList.add(messageEntry);
            cursor.moveToNext();
        }

        return mList;
    }
}
