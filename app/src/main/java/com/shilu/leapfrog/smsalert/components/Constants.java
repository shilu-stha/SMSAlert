package com.shilu.leapfrog.smsalert.components;

import android.net.Uri;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/6/15
 */
public class Constants {
    public static final String MESSAGE_ID = "MessageId";
    public static final String CONTACT_NAME = "ContactsName";

    public static final String SENT = "SENT";
    public static final String RECEIVED = "RECEIVED";

    public static final String BROADCAST_MESSAGE = "BroadcastMessage";
    public static final String BROADCAST_NUMBER = "BroadcastNumber";
    public static final String BROADCAST_RECEIVER_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String BROADCAST_MESSAGE_OBJECT = "pdus";

    public static final String PREVIOUS_ID = "PreviousId";
    public static final String FIRST_IN = "FirstIn";
    public static final String PREFERENCE = "Counters";


    public static final String SMS_CONTENT_PROVIDER_URI = "content://sms";
    public static final String SMS_CONTENT_PROVIDER_URI_SENT = Constants.SMS_CONTENT_PROVIDER_URI+"/sent";
    public static final String CONTENT_AUTHORITY = "com.shilu.leapfrog.smsalert";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String SMS_PROVIDER_COLUMN_BODY = "body";
    public static final String SMS_PROVIDER_COLUMN_ADDRESS = "address";
    public static final String SMS_PROVIDER_COLUMN_DATE = "date";

    public static final String WELCOME_TEXT = "Welcome to SMS Alert!";
    public static final String FULL_MESSAGE_TEXT = "SMS Received. \n";

    public final static String DATE_FORMAT = "E d-MMM-yy";
    public final static String TIME_FORMAT = "h:mma";

    public static final String PATH_MESSAGE = "messages";
    public static final String PATH_MESSAGE_DETAILS = "message_details";

    public static final String ERROR_MESSAGE_LANGUAGE_UNAVAILABLE = "Language is not available.";
    public static final String ERROR_SQL_INSERTION = "Failed to insert row into ";
    public static final String ERROR_UNKNOWN_EXCEPTION = "Unknown Exception ";
}