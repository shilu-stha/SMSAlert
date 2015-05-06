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
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage message[] = null;
            String smsFrom = null;
            String msgBody = " ";
            Long timeStamp = null;
            String fullMessage;
            if (bundle != null) {
                try {
                    Object[] pdusObject = (Object[]) bundle.get("pdus");
                    message = new SmsMessage[pdusObject.length];
                    for (int i = 0; i < message.length; i++) {
                        message[i] = SmsMessage.createFromPdu((byte[]) pdusObject[i]);
                        smsFrom = message[i].getOriginatingAddress();
                        timeStamp = message[i].getTimestampMillis();
                        msgBody = msgBody + message[i].getMessageBody();
                    }
                    fullMessage = "SMS Received. \n" + msgBody/*+"\n From "+smsFrom*/;
                    ContentResolver contentResolver = context.getContentResolver();

                    String displayName = "";
                    String contactId = "";
                    String phoneNumber = smsFrom;

                    try {

                        ContactLookUp lookUp = new ContactLookUp(context);
                        String[] value = lookUp.phoneLookUp(smsFrom);
                        displayName = value[0];
                        contactId = value[1];
                        phoneNumber = value[2];

                    } catch (Exception e) {
                        Log.e("ContactCursorException ", e.getMessage());
                    } finally {

                        InsertData.insertMessage(context, timeStamp, "RECEIVED", msgBody, contactId, displayName, phoneNumber);
//                        insertMessage(timeStamp, msgBody, contactId, displayName, phoneNumber);
                        Intent intentService = new Intent(context, TextToSpeechService.class);
                        intentService.putExtra("SMSAlert_Message", fullMessage);
                        intentService.putExtra("SMSAlert_Sender", smsFrom);
                        context.startService(intentService);

                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }

}
