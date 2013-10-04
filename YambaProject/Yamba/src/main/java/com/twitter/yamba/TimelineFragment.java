package com.twitter.yamba;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class TimelineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] FROM = {TweetContract.Column.USER, TweetContract.Column.MESSAGE};
    private static final int[] TO = {android.R.id.text1, android.R.id.text2};
    private static final int LOADER_ID = 42;
    private SimpleCursorAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("Loading...");

        adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null, FROM, TO, 0);

        setListAdapter(adapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    // Runs on a non-UI thread
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id != LOADER_ID) return null;
        return new CursorLoader(getActivity(), TweetContract.CONTENT_URI, null, null, null,
                TweetContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }
}
