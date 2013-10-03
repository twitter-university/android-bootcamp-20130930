package com.twitter.yamba;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, TweetContract.DB_NAME, null, TweetContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s int primary key, %s text, %s text, %s int)",
                TweetContract.TABLE, TweetContract.Column.ID, TweetContract.Column.USER,
                TweetContract.Column.MESSAGE, TweetContract.Column.CREATED_AT);
        Log.d("DbHelper", "onCreated with SQL: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For dev purposes, typically you'd have ALTER TABLE ...
        db.execSQL("drop table if exists " + TweetContract.TABLE);
        onCreate(db);
    }
}
