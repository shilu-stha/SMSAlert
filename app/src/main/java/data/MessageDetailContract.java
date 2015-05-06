package data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/4/15
 */
public class MessageDetailContract {

    public static final String CONTENT_AUTHORITY = "com.shilu.leapfrog.smsalert";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MESSAGE_DETAILS = "message_details";

    public static final class MessageDetailEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGE_DETAILS).build();
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
