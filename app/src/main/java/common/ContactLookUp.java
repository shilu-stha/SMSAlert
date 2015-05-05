package common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by shilushrestha on 5/4/15.
 */
public class ContactLookUp {

    Context context;
    private String displayName="";
    private String phoneNumber="";
    private String contactId="";

    public ContactLookUp(Context context) {
        this.context = context;
    }
    
    public String[] phoneLookUp(String smsFrom){
        Log.d("LOOKUP","smsfrom "+smsFrom);
        ContentResolver contentResolver = context.getContentResolver();
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
        Log.d("LOOKUP",displayName+" "+contactId+" "+phoneNumber);
        String[] value = {displayName,contactId,phoneNumber};
        return value;
    }
}
