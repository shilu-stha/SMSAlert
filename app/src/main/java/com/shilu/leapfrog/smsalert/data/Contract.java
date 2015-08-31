package com.shilu.leapfrog.smsalert.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.shilu.leapfrog.smsalert.components.Constants;


/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/21/15
 */
public class Contract {

    public static final class MessageEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Constants.BASE_CONTENT_URI.buildUpon().appendPath(Constants.PATH_MESSAGE).build();
        public static final String TABLE_NAME = "messages";
        public static final String DATE_TIME = "date_time";
        public static final String MESSAGE_BODY = "message_body";
        public static final String CONTACTS_ID = "contacts_id";
        public static final String CONTACTS_NAME = "contacts_name";
        public static final String CONTACTS_NUMBER = "contacts_number";

        public static final Uri buildMessageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MessageDetailEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Constants.BASE_CONTENT_URI.buildUpon().appendPath(Constants.PATH_MESSAGE_DETAILS).build();
        public static final String TABLE_NAME = "message_details";
        public static final String DATE_TIME = "date_time";
        public static final String MESSAGE_BODY = "message_body";
        public static final String MESSAGE_TYPE = "message_type";
        public static final String MESSAGES_TABLE_ID = "message_table_id";

        public static final Uri buildMessageDetailsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
