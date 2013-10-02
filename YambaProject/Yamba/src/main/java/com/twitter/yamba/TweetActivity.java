package com.twitter.yamba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;

public class TweetActivity extends BaseActivity {
    private static final String TAG = TweetActivity.class.getSimpleName();
    private Button tweetButton;
    private EditText tweetText;
    private TextView tweetLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetButton = (Button) findViewById(R.id.tweet_button);
        tweetText = (EditText) findViewById(R.id.tweet_text);
        tweetLength = (TextView) findViewById(R.id.tweet_length);

        tweetText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = 140 - editable.length();
                tweetLength.setText(Integer.toString(length));
            }
        });

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateTask().execute( tweetText.getText().toString() );

                Log.d("TweetActivity", "onClicked");
            }
        });

        Log.d(TAG, "onCreated: " +this );
    }

    private final class UpdateTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(TweetActivity.this, "Posting", "Please wait...");
        }

        // Executes on a worker thread
        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TweetActivity.this);
            String username = prefs.getString( "username", "");
            String password = prefs.getString("password", "");

            publishProgress(0);
            YambaClient twitterService = new YambaClient(username, password);
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
            dialog.dismiss();
            Toast.makeText(TweetActivity.this, "Successfully posted!", Toast.LENGTH_LONG).show();
        }
    }

}
