package com.shilu.leapfrog.smsalert.components;

import android.content.ContentValues;
import android.content.Context;

import com.shilu.leapfrog.smsalert.data.Contract;

/**
 * Insert sms data to table
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/5/15
 */
public class InsertData {

    /**
     * Insert com.shilu.leapfrog.smsalert.data into messages and message_details tables
     *
     * @param context
     * @param timeStamp time in <code><i>milliseconds</i></code>
     * @param smsType type: <code><i>SENT/RECEIVED</i></code>
     * @param msgBody
     * @param contactId
     * @param contactName
     * @param contactNumber
     *
     */
    public static void insertMessage(Context context, Long timeStamp, String smsType, String msgBody, String contactId, String contactName, String contactNumber) {
        ContentValues mValues = new ContentValues();
        mValues.put(Contract.MessageEntry.DATE_TIME, timeStamp);
        mValues.put(Contract.MessageEntry.MESSAGE_BODY, msgBody);
        mValues.put(Contract.MessageEntry.CONTACTS_ID, contactId);
        mValues.put(Contract.MessageEntry.CONTACTS_NAME, contactName);
        mValues.put(Contract.MessageEntry.CONTACTS_NUMBER, contactNumber);
        context.getContentResolver().insert(Contract.MessageEntry.CONTENT_URI, mValues);

        ContentValues mDetailValues = new ContentValues();
        mDetailValues.put(Contract.MessageDetailEntry.DATE_TIME, timeStamp);
        mDetailValues.put(Contract.MessageDetailEntry.MESSAGE_BODY, msgBody);
        mDetailValues.put(Contract.MessageDetailEntry.MESSAGE_TYPE, smsType);
        mDetailValues.put(Contract.MessageDetailEntry.MESSAGES_TABLE_ID, contactNumber);
        context.getContentResolver().insert(Contract.MessageDetailEntry.CONTENT_URI, mDetailValues);
    }
}
