package data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by shilushrestha on 4/20/15.
 */
public class MessageProvider extends ContentProvider{
    private DBHelper dbHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder messageByContactsInfo;

    public static final int MESSAGE = 1;
    public static final int ALTERCOLUMN = 11;
    public static final int CONTACTS = 2;
    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    static{
        messageByContactsInfo = new SQLiteQueryBuilder();

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case MESSAGE:
                cursor = dbHelper.getReadableDatabase().query(
                        MessageContract.MessageEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int matcher = uriMatcher.match(uri);
        Uri returnUri = null;

        switch (matcher){
            case MESSAGE:
                long _id = db.insert(MessageContract.MessageEntry.TABLE_NAME,null,values);
                if(_id>0){
                    returnUri = MessageContract.MessageEntry.buildMessageUri(_id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Exception "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int matcher = uriMatcher.match(uri);
        int rowsDeleted = 0;

        if(null==selection){ selection = "1";}
        switch (matcher){
            case MESSAGE:
                rowsDeleted= db.delete(MessageContract.MessageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Exception "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int matcher = uriMatcher.match(uri);
        int rowsUpdated = 0;

        if(null==selection){ selection = "1";}
        switch (matcher){
            case MESSAGE:
                rowsUpdated= db.update(MessageContract.MessageEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case ALTERCOLUMN:
                rowsUpdated= db.update(MessageContract.MessageEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Exception "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MessageContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority,MessageContract.PATH_MESSAGE,MESSAGE);

        return uriMatcher;

    }
}
