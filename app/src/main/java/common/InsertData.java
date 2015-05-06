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

    /**
     *
     * @param context
     * @param timeStamp time in milliseconds
     * @param smsType type: SENT/RECEIVED
     * @param msgBody
     * @param contactId
     * @param contactName
     * @param contactNumber
     * insert data into messages and message_details tables
     */
    static void insertMessage(Context context, Long timeStamp, String smsType, String msgBody, String contactId, String contactName, String contactNumber) {
        ContentValues mValues = new ContentValues();
        mValues.put(MessageContract.MessageEntry.DATE_TIME, timeStamp);
        mValues.put(MessageContract.MessageEntry.MESSAGE_BODY, msgBody);
        mValues.put(MessageContract.MessageEntry.CONTACTS_ID, contactId);
        mValues.put(MessageContract.MessageEntry.CONTACTS_NAME, contactName);
        mValues.put(MessageContract.MessageEntry.CONTACTS_NUMBER, contactNumber);
        context.getContentResolver().insert(MessageContract.MessageEntry.CONTENT_URI, mValues);

        ContentValues mDetailValues = new ContentValues();
        mDetailValues.put(MessageDetailContract.MessageDetailEntry.DATE_TIME, timeStamp);
        mDetailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGE_BODY, msgBody);
        mDetailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGE_TYPE, smsType);
        mDetailValues.put(MessageDetailContract.MessageDetailEntry.MESSAGES_TABLE_ID, contactNumber);
        context.getContentResolver().insert(MessageDetailContract.MessageDetailEntry.CONTENT_URI, mDetailValues);
    }
}
