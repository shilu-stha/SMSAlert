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

    @Override
    public void onReceive(Context mContext, Intent intent) {
        this.mContext = mContext;

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            Bundle mBundle = intent.getExtras();
            SmsMessage mMessage[] = null;
            String mFrom = null;
            String mMsgBody = " ";
            Long mTimeStamp = null;
            String mFullMessage;

            if (mBundle != null) {
                try {
                    Object[] pdusObject = (Object[]) mBundle.get("pdus");
                    mMessage = new SmsMessage[pdusObject.length];

                    for (int i = 0; i < mMessage.length; i++) {
                        mMessage[i] = SmsMessage.createFromPdu((byte[]) pdusObject[i]);
                        mFrom = mMessage[i].getOriginatingAddress();
                        mTimeStamp = mMessage[i].getTimestampMillis();
                        mMsgBody = mMsgBody + mMessage[i].getMessageBody();
                    }
                    mFullMessage = "SMS Received. \n" + mMsgBody/*+"\n From "+mFrom*/;
                  
                    String mDisplayName = "";
                    String mContactId = "";
                    String mPhoneNumber = mFrom;

                    try {

                        ContactLookUp mLookUp = new ContactLookUp(mContext);
                        String[] value = mLookUp.phoneLookUp(mFrom);
                        mDisplayName = value[0];
                        mContactId = value[1];
                        mPhoneNumber = value[2];

                    } catch (Exception e) {
                        Log.e("ContactCursorException ", e.getMessage());
                    } finally {

                        InsertData.insertMessage(mContext, mTimeStamp, "RECEIVED", mMsgBody, mContactId, mDisplayName, mPhoneNumber);
//                        insertMessage(mTimeStamp, mMsgBody, mContactId, mDisplayName, mPhoneNumber);
                        Intent mIntentService = new Intent(mContext, TextToSpeechService.class);
                        mIntentService.putExtra("SMSAlert_Message", mFullMessage);
                        mIntentService.putExtra("SMSAlert_Sender", mFrom);
                        mContext.startService(mIntentService);

                    }
                } catch (Exception e) {
                    Log.e("Exception caught", e.getMessage());
                }
            }
        }
    }

}
