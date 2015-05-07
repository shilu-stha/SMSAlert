package data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import common.Constants;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 4/20/15
 */

public class MessageProvider extends ContentProvider {
    private DBHelper mDBHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int MESSAGE = 1;
    public static final int MESSAGEDETAIL = 2;
    public static final int ALTERCOLUMN = 11;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor mCursor = null;
        switch (sUriMatcher.match(uri)) {
            case MESSAGE:
                mCursor = mDBHelper.getReadableDatabase().query(
                        Contract.MessageEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MESSAGEDETAIL:
                mCursor = mDBHelper.getReadableDatabase().query(
                        Contract.MessageDetailEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown Exception " + uri);
        }
        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();
        int mMatcher = sUriMatcher.match(uri);
        Uri mReturnUri = null;
        long mID;
        switch (mMatcher) {
            case MESSAGE:
                mID = mDb.insert(Contract.MessageEntry.TABLE_NAME, null, values);
                if (mID > 0) {
                    mReturnUri = Contract.MessageEntry.buildMessageUri(mID);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case MESSAGEDETAIL:
                mID = mDb.insert(Contract.MessageDetailEntry.TABLE_NAME, null, values);
                if (mID > 0) {
                    mReturnUri = Contract.MessageDetailEntry.buildMessageDetailsUri(mID);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown Exception " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return mReturnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();
        int mMatcher = sUriMatcher.match(uri);
        int mRowDeleted = 0;

        if (null == selection) {
            selection = "1";
        }
        switch (mMatcher) {
            case MESSAGE:
                mRowDeleted = mDb.delete(Contract.MessageEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MESSAGEDETAIL:
                mRowDeleted = mDb.delete(Contract.MessageDetailEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Exception " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return mRowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();
        int mMatcher = sUriMatcher.match(uri);
        int mRowsUpdated = 0;

        if (null == selection) {
            selection = "1";
        }
        switch (mMatcher) {
            case MESSAGE:
                mRowsUpdated = mDb.update(Contract.MessageEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case MESSAGEDETAIL:
                mRowsUpdated = mDb.update(Contract.MessageDetailEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case ALTERCOLUMN:
                mRowsUpdated = mDb.update(Contract.MessageEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Exception " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return mRowsUpdated;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String mAuthority = Constants.CONTENT_AUTHORITY;
        mUriMatcher.addURI(mAuthority, Constants.PATH_MESSAGE, MESSAGE);
        mUriMatcher.addURI(mAuthority, Constants.PATH_MESSAGE_DETAILS, MESSAGEDETAIL);

        return mUriMatcher;

    }
}
