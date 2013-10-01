package com.twitter.yamba;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;

public class TweetActivity extends Activity {
    private Button tweetButton;
    private EditText tweetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetButton = (Button) findViewById(R.id.tweet_button);
        tweetText = (EditText) findViewById(R.id.tweet_text);

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateTask().execute( tweetText.getText().toString() );

                Log.d("TweetActivity", "onClicked");
            }
        });
    }

    private class UpdateTask extends AsyncTask<String, Integer, String> {

        // Executes on a worker thread
        @Override
        protected String doInBackground(String... strings) {
            publishProgress(0);
            YambaClient twitterService = new YambaClient("student", "password");
            publishProgress(50);
            twitterService.updateStatus( strings[0] );
            publishProgress(100);

            return "Successfully posted!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        // Runs on UI thread
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(TweetActivity.this, "Successfully posted!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tweet, menu);
        return true;
    }

}
