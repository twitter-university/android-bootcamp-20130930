package com.twitter.yamba;

import android.os.Bundle;
import android.util.Log;

public class DetailsActivity extends BaseActivity {
    private DetailsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (fragment == null) {
            fragment = new DetailsFragment();
            getFragmentManager().beginTransaction().add(android.R.id.content,
                    fragment, fragment.getClass().getSimpleName()).commit();
        }

        Log.d("DetailsActivity", this.toString());
    }
}
