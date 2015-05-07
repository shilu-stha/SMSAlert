package common;

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
    public static final String PREVIOUS_ID = "PreviousId";
    public static final String FIRST_IN = "FirstIn";
    public static final String PREFERENCE = "Counters";
    public static final String BROADCAST_RECEIVER_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String BROADCAST_MESSAGE_OBJECT = "pdus";
    public static final String SMS_CONTENT_PROVIDER_URI = "content://sms";
    public static final String CONTENT_AUTHORITY = "com.shilu.leapfrog.smsalert";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String WELCOME_TEXT = "Welcome to SMS Alert!";
    public final static String DATEFORMAT = "E d-MMM-yy";
    public final static String TIMEFORMAT = "h:mma";
    public static final String PATH_MESSAGE = "messages";
    public static final String PATH_MESSAGE_DETAILS = "message_details";
}