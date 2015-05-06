package common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/4/15
 */
public class ContactLookUp {

    Context mContext;

    public ContactLookUp(Context context) {
        this.mContext = context;
    }

    /**
     *
     * @param smsFrom
     * @return String[] containing displayname, contactId, phoneNumber of the contacts as saved from ContactProvider
     * on the basis of number sent in params.
     * Uses PhoneLookup Content Provider to filter out the details
     */
    public String[] phoneLookUp(String smsFrom) {
        String mDisplayName = "";
        String mPhoneNumber = smsFrom;
        String mContactId = "";

        ContentResolver mContentResolver = mContext.getContentResolver();
        Uri mUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(smsFrom));

        Cursor mContactsCursor = mContentResolver.query(mUri, new String[]{
                        ContactsContract.PhoneLookup.DISPLAY_NAME,
                        ContactsContract.PhoneLookup._ID,
                        ContactsContract.PhoneLookup.NUMBER},
                null,
                null,
                null);

        if (mContactsCursor != null) {
            if (mContactsCursor.moveToFirst()) {
                mDisplayName = mContactsCursor.getString(mContactsCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                mContactId = mContactsCursor.getString(mContactsCursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
                mPhoneNumber = mContactsCursor.getString(mContactsCursor.getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
            }
            mContactsCursor.close();
        }
        String[] mValue = {mDisplayName, mContactId, mPhoneNumber};

        return mValue;
    }
}
