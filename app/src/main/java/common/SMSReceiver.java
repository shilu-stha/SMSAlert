package common;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.shilu.leapfrog.smsalert.TextToSpeechService;


/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/15/15
 */
public class SMSReceiver extends BroadcastReceiver {
    Context mContext;
    private String TAG = SMSReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context mContext, Intent intent) {
        this.mContext = mContext;

        if (intent.getAction().equals(Constants.BROADCAST_RECEIVER_ACTION)) {

            Bundle mBundle = intent.getExtras();
            SmsMessage mMessage[] = null;
            String mFrom = "";
            String mMsgBody = "";
            Long mTimeStamp = null;
            String mFullMessage;

            if (mBundle != null) {
                try {
                    Object[] pdusObject = (Object[]) mBundle.get(Constants.BROADCAST_MESSAGE_OBJECT);
                    mMessage = new SmsMessage[pdusObject.length];

                    for (int i = 0; i < mMessage.length; i++) {
                        mMessage[i] = SmsMessage.createFromPdu((byte[]) pdusObject[i]);
                        mFrom = mMessage[i].getOriginatingAddress();
                        mTimeStamp = mMessage[i].getTimestampMillis();
                        mMsgBody = mMsgBody + mMessage[i].getMessageBody();
                    }
                    mFullMessage = "SMS Received. \n" + mMsgBody;
                  
                    String mDisplayName = "";
                    String mContactId = "";
                    String mPhoneNumber = mFrom;

                    try {

                        ContactLookUp mLookUp = new ContactLookUp(mContext);
                        String[] value = mLookUp.phoneLookUp(mFrom);
                        mDisplayName = value[0];
                        mContactId = value[1];
                        mPhoneNumber = value[2];

                    } catch (UnsupportedOperationException e) {
                        Log.e(TAG, e.getMessage());
                    } finally {

                        InsertData.insertMessage(mContext, mTimeStamp, Constants.RECEIVED, mMsgBody, mContactId, mDisplayName, mPhoneNumber);
                        startService(mFullMessage,mFrom);
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    /**
     * Start TextToSpeech service to read the message
     *
     * @param mFullMessage: Message to read
     * @param mFrom: Sms Sender's number
     */
    private void startService(String mFullMessage, String mFrom) {
        Intent mIntentService = new Intent(mContext, TextToSpeechService.class);
        mIntentService.putExtra(Constants.BROADCAST_MESSAGE, mFullMessage);
        mIntentService.putExtra(Constants.BROADCAST_NUMBER, mFrom);
        mContext.startService(mIntentService);
    }


}
