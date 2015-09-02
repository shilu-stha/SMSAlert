package com.shilu.leapfrog.smsalert.activity;

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

import com.shilu.leapfrog.smsalert.R;
import com.shilu.leapfrog.smsalert.adapter.MessageDetailsListAdapter;
import adapter.MessageDetailRecyclerAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.shilu.leapfrog.smsalert.components.Constants;
import com.shilu.leapfrog.smsalert.data.Contract;

/**
 * Shows detail of the SMS
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/24/15
 */
public class DetailMessageActivity extends ActionBarActivity implements TextToSpeech.OnInitListener, LoaderManager.LoaderCallbacks<Cursor>, ListView.OnItemClickListener {

    private String TAG = DetailMessageActivity.class.getSimpleName();
    private MessageDetailsListAdapter mAdapter;
    private TextToSpeech mTextToSpeech;
    private String mMessageId;

    @InjectView(R.id.app_bar)
    Toolbar toolbar;
    @InjectView(R.id.detail_listview)
    ListView listview;
    @InjectView(R.id.detail_progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        ButterKnife.inject(this);

        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);

        Bundle mBundle = getIntent().getExtras();
        mMessageId = mBundle.getString(Constants.MESSAGE_ID);
        String mContactName = mBundle.getString(Constants.CONTACT_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mContactName);

        mTextToSpeech = new TextToSpeech(DetailMessageActivity.this, this);

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

        ButterKnife.reset(this);
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
                Toast.makeText(getApplicationContext(), Constants.ERROR_MESSAGE_LANGUAGE_UNAVAILABLE, Toast.LENGTH_LONG).show();
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                Log.v(TAG, Constants.ERROR_MESSAGE_LANGUAGE_UNAVAILABLE);
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
//            mList = updateMessageDetailsList(data);
//            mAdapter = new MessageDetailRecyclerAdapter(mList);
////            mAdapter.setClickListener(this);
////            recyclerView.setAdapter(mAdapter);
        }

        mAdapter.changeCursor(data);

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    @OnItemClick(R.id.detail_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mProgressBar.setVisibility(View.VISIBLE);
        Cursor mCursor = (Cursor) parent.getItemAtPosition(position);
        String mMessage = mCursor.getString(mCursor.getColumnIndex(Contract.MessageEntry.MESSAGE_BODY));
        speakUp(mMessage);
    }
}
