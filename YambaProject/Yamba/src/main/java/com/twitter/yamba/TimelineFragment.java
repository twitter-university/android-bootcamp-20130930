package com.twitter.yamba;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static android.widget.SimpleCursorAdapter.ViewBinder;

public class TimelineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] FROM = {TweetContract.Column.USER, TweetContract.Column.MESSAGE,
            TweetContract.Column.CREATED_AT};
    private static final int[] TO = {R.id.list_item_user, R.id.list_item_message,
            R.id.list_item_created_at};
    private static final int LOADER_ID = 42;
    private SimpleCursorAdapter adapter;

    private static final ViewBinder VIEW_BINDER = new ViewBinder() {

        @Override
        public boolean setViewValue(View view, Cursor cursor, int index) {
            if (view.getId() != R.id.list_item_created_at) return false;

            // Custom binding
            long timestamp = cursor.getLong(index);
            CharSequence relTime = DateUtils.getRelativeTimeSpanString(timestamp);
            ((TextView) view).setText(relTime);

            return true;
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("Loading...");

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item, null, FROM, TO, 0);
        adapter.setViewBinder(VIEW_BINDER);

        setListAdapter(adapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().
                findFragmentById(R.id.fragment_details);

        if (detailsFragment != null && detailsFragment.isVisible()) {
            detailsFragment.updateId(id);
        } else {
            getActivity().startActivity(new Intent(getActivity(), DetailsActivity.class).
                    addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).putExtra("id", id));
        }
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
