package common;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.shilu.leapfrog.smsalert.MainActivity;

import data.MessageContract;
import data.MessageDetailContract;


/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/24/15
 */
public class SMSObserver extends ContentObserver {

    private Context mainActivity;

    public SMSObserver(Handler handler, Context mainActivity) {
        super(handler);
        this.mainActivity = mainActivity;

    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        SharedPreferences pref = mainActivity.getSharedPreferences("Counters", Context.MODE_PRIVATE);
        long previousId = pref.getLong("PreviousId", 0);

        Uri uriSMSURI = Uri.parse("content://sms/sent");
        Cursor cur = mainActivity.getContentResolver().query(uriSMSURI, null, null, null, null);
        cur.moveToNext();

        /*
        to stop duplicate sms from being saved into db
        * */
        long id = cur.getLong(cur.getColumnIndex("_id"));

        if (previousId != id) {
            pref.edit().putLong("PreviousId", id).commit();
            String content = cur.getString(cur.getColumnIndex("body"));
            String smsNumber = cur.getString(cur.getColumnIndex("address"));
            long date = cur.getLong(cur.getColumnIndex("date"));
            if (smsNumber == null || smsNumber.length() <= 0) {
                smsNumber = "Unknown";
            } else {
                String displayName = "";
                String contactId = "";
                String phoneNumber = smsNumber;
                try {
                    ContactLookUp lookUp = new ContactLookUp(mainActivity);
                    String[] value = lookUp.phoneLookUp(smsNumber);
                    displayName = value[0];
                    contactId = value[1];
                    phoneNumber = value[2];
                } catch (Exception e) {

                } finally {
                    Log.d("SMSOBSERVER", "phoneNumber "+phoneNumber);
                    insertMessage(date, content, contactId, displayName, phoneNumber);
                }
            }
            Log.d("SMSOBSERVER", content);
            Log.d("SMSOBSERVER", "smsNumber "+smsNumber);
            cur.close();
        }
    }

    private void insertMessage(Long timeStamp, String msgBody, String contactId, String contactName, String contactNumber) {
        ContentValues values = new ContentValues();
        values.put(MessageContract.MessageEntry.DATE_TIME, timeStamp);
        values.put(MessageContract.MessageEntry.MESSAGE_BODY, msgBody);
        values.put(MessageContract.MessageEntry.CONTACTS_ID, contactId);
        values.put(MessageContract.MessageEntry.CONTACTS_NAME, contactName);
        values.put(MessageContract.MessageEntry.CONTACTS_NUMBER, contactNumber);

        ContentValues detailValues = new ContentValues();
        detailValues.put(MessageDetailContract.MessageDetailEntry.DATE_TIME, timeStamp);
        detailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGE_BODY, msgBody);
        detailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGE_TYPE, "SENT");
        detailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGES_TABLE_ID, contactId);
        mainActivity.getContentResolver().insert(MessageDetailContract.MessageDetailEntry.CONTENT_URI, detailValues);
    }

}
