package common;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;


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
                    Log.d("SMSOBSERVER", "phoneNumber " + phoneNumber);
                    InsertData.insertMessage(mainActivity, date, "SENT", content, contactId, displayName, phoneNumber);
//                    insertMessage(date, content, contactId, displayName, phoneNumber);
                }
            }
            cur.close();
        }
    }

}
