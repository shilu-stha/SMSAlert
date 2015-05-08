package services;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;

import components.Constants;
import components.ContactLookUp;
import components.InsertData;


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

        SharedPreferences mPref = mContext.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE);
        long mPreviousId = mPref.getLong(Constants.PREVIOUS_ID, 0);

        Uri mSmsUri = Uri.parse(Constants.SMS_CONTENT_PROVIDER_URI_SENT);
        Cursor mCur = mContext.getContentResolver().query(mSmsUri, null, null, null, null);
        mCur.moveToNext();

        // get _id of the message to stop duplicate messages from being saved
        long mId = mCur.getLong(mCur.getColumnIndex(BaseColumns._ID));

        if (mPreviousId != mId) {
            mPref.edit().putLong(Constants.PREVIOUS_ID, mId).commit();

            String mContent = mCur.getString(mCur.getColumnIndex(Constants.SMS_PROVIDER_COLUMN_BODY));
            String mNumber = mCur.getString(mCur.getColumnIndex(Constants.SMS_PROVIDER_COLUMN_ADDRESS));
            long mDate = mCur.getLong(mCur.getColumnIndex(Constants.SMS_PROVIDER_COLUMN_DATE));

            if (mNumber != null || mNumber.length() > 0) {

                String mDisplayName = "";
                String mContactId = "";
                String mPhoneNumber = mNumber;

                try {
                    ContactLookUp lookUp = new ContactLookUp(mContext);
                    String[] value = lookUp.phoneLookUp(mNumber);
                    mDisplayName = value[0];
                    mContactId = value[1];
                    mPhoneNumber = value[2];
                } catch (UnsupportedOperationException e) {
                        throw new UnsupportedOperationException(e.getMessage());
                } finally {
                    InsertData.insertMessage(mContext, mDate, Constants.SENT, mContent, mContactId, mDisplayName, mPhoneNumber);
                }
            }
            mCur.close();
        }
    }

}
