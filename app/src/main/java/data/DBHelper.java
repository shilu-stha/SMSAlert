package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import data.MessageContract.MessageEntry;

/**
 * Created by shilushrestha on 4/20/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SMSAlert.db";

    public static final int DATABASE_VERSION = 1;

    final String SQL_CREATE_MESSAGE_TABLE = "CREATE TABLE " + MessageContract.MessageEntry.TABLE_NAME + " (" +
            MessageContract.MessageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MessageContract.MessageEntry.MESSAGE_BODY + " TEXT UNIQUE NOT NULL, " +
            MessageContract.MessageEntry.DATE_TIME + " INTEGER NOT NULL " +
            " );";

    final String SQL_DROP_MESSAGE_TABLE = "DROP TABLE IF EXISTS "+ MessageEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DROP_MESSAGE_TABLE);
        onCreate(db);
    }
}
