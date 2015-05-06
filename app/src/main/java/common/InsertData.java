package common;

import android.content.ContentValues;
import android.content.Context;

import data.MessageContract;
import data.MessageDetailContract;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/5/15
 */
public class InsertData {

    static void insertMessage(Context context, Long timeStamp, String smsType, String msgBody, String contactId, String contactName, String contactNumber) {
        ContentValues values = new ContentValues();
        values.put(MessageContract.MessageEntry.DATE_TIME, timeStamp);
        values.put(MessageContract.MessageEntry.MESSAGE_BODY, msgBody);
        values.put(MessageContract.MessageEntry.CONTACTS_ID, contactId);
        values.put(MessageContract.MessageEntry.CONTACTS_NAME, contactName);
        values.put(MessageContract.MessageEntry.CONTACTS_NUMBER, contactNumber);
        context.getContentResolver().insert(MessageContract.MessageEntry.CONTENT_URI, values);

        ContentValues detailValues = new ContentValues();
        detailValues.put(MessageDetailContract.MessageDetailEntry.DATE_TIME, timeStamp);
        detailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGE_BODY, msgBody);
        detailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGE_TYPE, smsType);
        detailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGES_TABLE_ID, contactNumber);
        context.getContentResolver().insert(MessageDetailContract.MessageDetailEntry.CONTENT_URI, detailValues);
    }
}
