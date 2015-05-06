package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import data.MessageContract.MessageEntry;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/20/15
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SMSAlert.db";

    public static final int DATABASE_VERSION = 1;

    final String SQL_CREATE_MESSAGE_TABLE = "CREATE TABLE " + MessageContract.MessageEntry.TABLE_NAME + " (" +
            MessageContract.MessageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MessageContract.MessageEntry.MESSAGE_BODY + " TEXT NOT NULL, " +
            MessageContract.MessageEntry.DATE_TIME + " INTEGER NOT NULL, " +
            MessageContract.MessageEntry.CONTACTS_NUMBER + " TEXT NOT NULL, " +
            MessageContract.MessageEntry.CONTACTS_NAME + " TEXT ," +
            MessageContract.MessageEntry.CONTACTS_ID + " INTEGER NOT NULL, " +
            " UNIQUE (" + MessageContract.MessageEntry.CONTACTS_ID + ", " +
            MessageContract.MessageEntry.CONTACTS_NUMBER + ") ON CONFLICT REPLACE " +
            " );";

    final String SQL_CREATE_MESSAGE_DETAIL = "CREATE TABLE " + MessageDetailContract.MessageDetailEntry.TABLE_NAME + " (" +
            MessageDetailContract.MessageDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MessageDetailContract.MessageDetailEntry.MESSAGES_TABLE_ID + " INTEGER NOT NULL, " +
            MessageDetailContract.MessageDetailEntry.MESSAGE_BODY + " TEXT NOT NULL, " +
            MessageDetailContract.MessageDetailEntry.MESSAGE_TYPE + " TEXT NOT NULL, " +
            MessageDetailContract.MessageDetailEntry.DATE_TIME + " INTEGER NOT NULL, " +
            " FOREIGN KEY (" + MessageDetailContract.MessageDetailEntry.MESSAGES_TABLE_ID + ") REFERENCES " +
            MessageContract.MessageEntry.TABLE_NAME + " (" + MessageContract.MessageEntry._ID + ") " +
            " );";

    final String SQL_DROP_MESSAGE_TABLE = "DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME;
    final String SQL_DROP_MESSAGE_DETAIL_TABLE = "DROP TABLE IF EXISTS " + MessageDetailContract.MessageDetailEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MESSAGE_TABLE);
        db.execSQL(SQL_CREATE_MESSAGE_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_MESSAGE_TABLE);
        db.execSQL(SQL_DROP_MESSAGE_DETAIL_TABLE);
        onCreate(db);
    }
}
