package com.twitter.yamba;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class TweetProvider extends ContentProvider {
    private static final String TAG = TweetProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(TweetContract.AUTHORITY, TweetContract.TABLE, TweetContract.TWEET_DIR);
        uriMatcher.addURI(TweetContract.AUTHORITY, TweetContract.TABLE + "/#", TweetContract.TWEET_ITEM);
    }

    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    // select user, message from tweet where user='bob' order by created_at
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TweetContract.TABLE);

        switch (uriMatcher.match(uri)) {
            case TweetContract.TWEET_ITEM:
                long id = ContentUris.parseId(uri);
                qb.appendWhere(TweetContract.Column.ID+"="+id);
                break;
            case TweetContract.TWEET_DIR:

                break;
            default:
                throw new IllegalArgumentException("Illegal uri: "+uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        // assert
        if (uriMatcher.match(uri) != TweetContract.TWEET_DIR) {
            throw new IllegalArgumentException("Illegal URI: " + uri);
        }


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insertWithOnConflict(TweetContract.TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        Uri ret = (rowId==-1)? null : ContentUris.withAppendedId(uri, contentValues.getAsLong(TweetContract.Column.ID));
        Log.d(TAG, "inserted uri: " + ret);

        return ret;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }
}
