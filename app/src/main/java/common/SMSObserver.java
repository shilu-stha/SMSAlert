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

    private Context mContext;

    public SMSObserver(Handler handler, Context mContext) {
        super(handler);
        this.mContext = mContext;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        SharedPreferences mPref = mContext.getSharedPreferences("Counters", Context.MODE_PRIVATE);
        long mPreviousId = mPref.getLong("PreviousId", 0);

        Uri mSmsUri = Uri.parse("content://sms/sent");
        Cursor mCur = mContext.getContentResolver().query(mSmsUri, null, null, null, null);
        mCur.moveToNext();

        /**
         * get _id of the message to stop duplicate messages from being saved
         */
        long mId = mCur.getLong(mCur.getColumnIndex("_id"));

        if (mPreviousId != mId) {
            mPref.edit().putLong("PreviousId", mId).commit();

            String mContent = mCur.getString(mCur.getColumnIndex("body"));
            String mNumber = mCur.getString(mCur.getColumnIndex("address"));
            long mDate = mCur.getLong(mCur.getColumnIndex("date"));

            if (mNumber == null || mNumber.length() <= 0) {
                mNumber = "Unknown";
            } else {

                String mDisplayName = "";
                String mContactId = "";
                String mPhoneNumber = mNumber;

                try {
                    ContactLookUp lookUp = new ContactLookUp(mContext);
                    String[] value = lookUp.phoneLookUp(mNumber);
                    mDisplayName = value[0];
                    mContactId = value[1];
                    mPhoneNumber = value[2];
                } catch (Exception e) {
                        throw new UnsupportedOperationException("UnSupported Operation: "+e.getMessage());
                } finally {
                    InsertData.insertMessage(mContext, mDate, "SENT", mContent, mContactId, mDisplayName, mPhoneNumber);
//                    insertMessage(mDate, content, mContactId, mDisplayName, mPhoneNumber);
                }
            }
            mCur.close();
        }
    }

}
