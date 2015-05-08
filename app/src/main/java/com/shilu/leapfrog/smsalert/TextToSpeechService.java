package com.shilu.leapfrog.smsalert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import components.Constants;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/16/15
 */
public class TextToSpeechService extends Service implements TextToSpeech.OnInitListener {

    TextToSpeech mTextToSpeech;
    private Context mContext;
    String mReceivedText = null;
    String mSender = null;
    boolean mTtsReady = false;
    boolean mMsgReady = false;

    private String TAG = TextToSpeechService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        mTextToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle mBundle = intent.getExtras();
        mReceivedText = mBundle.getString(Constants.BROADCAST_MESSAGE);
        mSender = mBundle.getString(Constants.BROADCAST_NUMBER);
        if (mReceivedText != null) {
            mMsgReady = true;
        }
        speakUp();

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        super.onDestroy();
    }

    /**
     *  Method to call TextToSpeech Service to read out the text.
     */
    public void speakUp() {
        if (mMsgReady && mTtsReady) {
            mTextToSpeech.speak(mReceivedText, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(),Constants.ERROR_MESSAGE_LANGUAGE_UNAVAILABLE, Toast.LENGTH_LONG).show();
                Intent mInstallIntent = new Intent();
                mInstallIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(mInstallIntent);
                Log.v(TAG, Constants.ERROR_MESSAGE_LANGUAGE_UNAVAILABLE);
            } else {
                mTtsReady = true;
                speakUp();
            }
        }
    }
}
