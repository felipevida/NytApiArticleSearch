package com.example.nytapiarticlesearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Android Final Project
 * Project: New York Times API
 *
 * @author Felipe Marinho Soares da Silva
 * <p>
 * Empty Activity to load {@link NytArticleFragment}
 */
public class NytEmptyActivity extends AppCompatActivity
{

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyt_empty);

        Bundle dataToPass = getIntent().getExtras();

        NytArticleFragment dFragment = new NytArticleFragment();

        dFragment.setArguments(dataToPass);
        dFragment.setTablet(false);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.NytBookmarksLayout, dFragment, "NYT_FRAGMENT")
                .commit();


    } // onCreate END

} // NytEmptyActivity END
