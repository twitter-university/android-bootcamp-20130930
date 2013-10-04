package com.twitter.yamba;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
    private TextView userText, messageText, createdAtText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_item, null, false);

        userText = (TextView) view.findViewById(R.id.list_item_user);
        messageText = (TextView) view.findViewById(R.id.list_item_message);
        createdAtText = (TextView) view.findViewById(R.id.list_item_created_at);

        long id = getActivity().getIntent().getLongExtra("id", -1);
        updateId(id);

        return view;
    }

    public void updateId(long id) {
        if (id != -1) {
            Uri uri = ContentUris.withAppendedId(TweetContract.CONTENT_URI, id);
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                userText.setText(cursor.getString(cursor.getColumnIndex(TweetContract.Column.USER)));
                messageText.setText(cursor.getString(cursor.getColumnIndex(TweetContract.Column.MESSAGE)));
                createdAtText.setText(
                        DateUtils.getRelativeTimeSpanString(cursor.getLong(
                                cursor.getColumnIndex(TweetContract.Column.CREATED_AT))));
            }
        } else {
            userText.setText("");
            messageText.setText("No data");
            createdAtText.setText("");
        }
    }
}
