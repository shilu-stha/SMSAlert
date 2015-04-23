package common;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import com.shilu.leapfrog.smsalert.TextToSpeechService;

import data.MessageContract;


/**
 * Created by shilushrestha on 4/15/15.
 */
public class SMSReceiver extends BroadcastReceiver{
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage message[] = null;
            String smsFrom = null;
            String msgBody = " ";
            Long timeStamp = null;
            String fullMessage;
            if(bundle!=null){
                try{
                    Object[] pdusObject = (Object[]) bundle.get("pdus");
                    message = new SmsMessage[pdusObject.length];
                    for(int i=0; i<message.length; i++){
                        message[i] = SmsMessage.createFromPdu((byte[]) pdusObject[i]);
                        smsFrom = message[i].getOriginatingAddress();
                        timeStamp = message[i].getTimestampMillis();
                        msgBody = msgBody+ message[i].getMessageBody();
                    }
                    fullMessage = "SMS Received. \n"+msgBody/*+"\n From "+smsFrom*/;
                    ContentResolver contentResolver = context.getContentResolver();

                    String displayName = "";
                    String contactId = "";
                    String phoneNumber = smsFrom;

                    try{
                        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(smsFrom));
                        Cursor contactsCursor = contentResolver.query(uri, new String[]{
                                        ContactsContract.PhoneLookup.DISPLAY_NAME,
                                        ContactsContract.PhoneLookup._ID,
                                        ContactsContract.PhoneLookup.NUMBER},
                                null,
                                null,
                                null);
                        if (contactsCursor != null) {
                            if (contactsCursor.moveToFirst()) {
                                displayName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                                contactId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
                                phoneNumber = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
                            }
                            contactsCursor.close();
                        }
                    }
                    catch (Exception e){
                        Log.e("Exception ",e.getMessage());
                    }
                    finally {
                        insertMessage(timeStamp, msgBody, contactId, displayName, phoneNumber);
                        Intent intentService = new Intent(context, TextToSpeechService.class);
                        intentService.putExtra("SMSAlert_Message", fullMessage);
                        intentService.putExtra("SMSAlert_Sender", smsFrom);
                        context.startService(intentService);
                    }
                }
                catch(Exception e){
                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }

    private void insertMessage(Long timeStamp, String msgBody,String contactId,String contactName,String contactNumber) {
            ContentValues values = new ContentValues();
            values.put(MessageContract.MessageEntry.DATE_TIME,timeStamp);
            values.put(MessageContract.MessageEntry.MESSAGE_BODY,msgBody);
            values.put(MessageContract.MessageEntry.CONTACTS_ID,contactId);
            values.put(MessageContract.MessageEntry.CONTACTS_NAME,contactName);
            values.put(MessageContract.MessageEntry.CONTACTS_NUMBER,contactNumber);
            context.getContentResolver().insert(MessageContract.MessageEntry.CONTENT_URI, values);
    }
}
