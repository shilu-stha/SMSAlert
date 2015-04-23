package data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by shilushrestha on 4/21/15.
 */
public class MessageContract {

    public static final String CONTENT_AUTHORITY = "com.shilu.leapfrog.smsalert";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_MESSAGE = "messages";

    public static final class MessageEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGE).build();
        public static final String TABLE_NAME = "messages";
        public static final String DATE_TIME = "date_time";
        public static final String MESSAGE_BODY = "message_body";
        public static final String CONTACTS_ID   = "contacts_id";
        public static final String CONTACTS_NAME   = "contacts_name";
        public static final String CONTACTS_NUMBER   = "contacts_number";

        public static final Uri buildMessageUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

}
