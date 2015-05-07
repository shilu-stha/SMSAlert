package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import data.Contract.MessageEntry;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/20/15
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SMSAlert.db";

    public static final int DATABASE_VERSION = 1;

    final String SQL_CREATE_MESSAGE_TABLE = "CREATE TABLE " + Contract.MessageEntry.TABLE_NAME + " (" +
            Contract.MessageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.MessageEntry.MESSAGE_BODY + " TEXT NOT NULL, " +
            Contract.MessageEntry.DATE_TIME + " INTEGER NOT NULL, " +
            Contract.MessageEntry.CONTACTS_NUMBER + " TEXT NOT NULL, " +
            Contract.MessageEntry.CONTACTS_NAME + " TEXT ," +
            Contract.MessageEntry.CONTACTS_ID + " INTEGER NOT NULL, " +
            " UNIQUE (" + Contract.MessageEntry.CONTACTS_ID + ", " +
            Contract.MessageEntry.CONTACTS_NUMBER + ") ON CONFLICT REPLACE " +
            " );";

    final String SQL_CREATE_MESSAGE_DETAIL = "CREATE TABLE " + Contract.MessageDetailEntry.TABLE_NAME + " (" +
            Contract.MessageDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.MessageDetailEntry.MESSAGES_TABLE_ID + " INTEGER NOT NULL, " +
            Contract.MessageDetailEntry.MESSAGE_BODY + " TEXT NOT NULL, " +
            Contract.MessageDetailEntry.MESSAGE_TYPE + " TEXT NOT NULL, " +
            Contract.MessageDetailEntry.DATE_TIME + " INTEGER NOT NULL, " +
            " FOREIGN KEY (" + Contract.MessageDetailEntry.MESSAGES_TABLE_ID + ") REFERENCES " +
            Contract.MessageEntry.TABLE_NAME + " (" + Contract.MessageEntry._ID + ") " +
            " );";

    final String SQL_DROP_MESSAGE_TABLE = "DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME;
    final String SQL_DROP_MESSAGE_DETAIL_TABLE = "DROP TABLE IF EXISTS " + Contract.MessageDetailEntry.TABLE_NAME;

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
