package com.twitter.yamba;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.List;

public class RefreshService extends IntentService {
    private static final String TAG = RefreshService.class.getSimpleName();

    public RefreshService() {
        super(TAG);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Check if first time around
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString("username", "");
        String password = prefs.getString("password", "");

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }

        ContentValues values = new ContentValues();
        List<YambaClient.Status> timeline = null;
        int count = 0;
        try {
            YambaClient twitterService = new YambaClient(username, password);
            timeline = twitterService.getTimeline(20);
            for(YambaClient.Status tweet: timeline) {
                values.clear();
                values.put(TweetContract.Column.ID, tweet.getId());
                values.put(TweetContract.Column.USER, tweet.getUser());
                values.put(TweetContract.Column.MESSAGE, tweet.getMessage());
                values.put(TweetContract.Column.CREATED_AT, tweet.getCreatedAt().getTime());
                if( getContentResolver().insert(TweetContract.CONTENT_URI, values) != null )
                    count++;

                Log.d(TAG, String.format("%s: %s", tweet.getUser(), tweet.getMessage()));
            }
        } catch (YambaClientException e) {
            e.printStackTrace();
        }

        if(count>0) {
            sendBroadcast( new Intent("com.twitter.yamba.action.NEW_TWEET").putExtra("count",count) );
        }

        Log.d(TAG, "onHandledIntent");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed");
    }
}
