package com.example.nytapiarticlesearch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Android Final Project
 * Project: New York Times API
 *
 * @author Felipe Marinho Soares da Silva
 * <p>
 * This fragment is used in a phone or tablet.
 * It loads two tables from the database:
 * <b>SearchTags</b>
 * <b>BookmarkedArticles</b>
 */
public class NytArticleFragment extends Fragment
{

    /**
     * Holds the result of a {@link android.widget.FrameLayout} check
     * if the resolution is higher than 720 isTablet is true.
     */
    private boolean isTablet;

    /**
     * Holds data passed from the Activity
     * that created this {@link Fragment}.
     * Not used in this Project but left for future use.
     */
    private Bundle dataFromActivity;

    /**
     * @param tablet takes a {@link Boolean} result from another Activity.
     */
    public void setTablet(boolean tablet)
    {
        isTablet = tablet;
    }

    /**
     * Loads resources to display the Fragment.
     *
     * @param inflater           {@link LayoutInflater}
     * @param container          {@link ViewGroup}
     * @param savedInstanceState {@link Bundle}
     * @return {@link View}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity a = new AppCompatActivity();
        Log.i("OnCreate ArtclFragment", String.valueOf(a.getSupportFragmentManager().getFragments().size()));

        // Inflate the layout for this fragment
        View nytBookmarksLayout = inflater.inflate(R.layout.activity_nyt_bookmarks, container, false);

        searchTags = new ArrayList<>();
        bookmarks = new ArrayList<>();

        searchTagsListView = ( ListView ) nytBookmarksLayout.findViewById(R.id.nytSearchTagsListViewInBookmarks);
        bookmarkedArticlesListView = ( ListView ) nytBookmarksLayout.findViewById(R.id.nytBookmarkedArticlesListView);

        nytdb = new NytDatabase(getActivity());
        db = nytdb.getWritableDatabase();

        // SEARCH TAGS LISTVIEW
        String[] nytSearchTagsTableColumns =
                {
                        NytDatabase.COL_ID,
                        NytDatabase.TIME_WHEN_SEARCH,
                        NytDatabase.SEARCH_TAGS
                };

        queryResults = db.query(
                false,
                NytDatabase.TABLE_SEARCH_TAGS_NAME,
                nytSearchTagsTableColumns,
                null,
                null,
                null,
                null,
                null,
                null);

        // SET CURSOR FOR DEBUGGING
        setCursor(queryResults);

        // GET ALL COLUMNS INDEX
        int searchTagIdColIndex
                = queryResults.getColumnIndex(NytDatabase.COL_ID);
        int timeWhenSearchTagColIndex
                = queryResults.getColumnIndex(NytDatabase.TIME_WHEN_SEARCH);
        int searchTagColIndex
                = queryResults.getColumnIndex(NytDatabase.SEARCH_TAGS);

        // ITERATE OVER queryResults
        while (queryResults.moveToNext())
        {
            long id
                    = queryResults.getLong(searchTagIdColIndex);
            String timeWhenSearch
                    = queryResults.getString(timeWhenSearchTagColIndex);
            String searchTag
                    = queryResults.getString(searchTagColIndex);
            // RE-CREATE searchTags ARRAYLIST FROM DATABASE
            searchTags.add(new NytSearchTags(id, timeWhenSearch, searchTag));
        }
        Collections.reverse(searchTags);
        searchTagAdapter = new SearchTagsAdapter();
        searchTagsListView.setAdapter(searchTagAdapter);

        //BOOKMARKED LISTVIEW
        String[] nytBookmarkedTableColumns =
                {
                        NytDatabase.COL_ID,
                        NytDatabase.WEB_URL,
                        NytDatabase.SNIPPET,
                        NytDatabase.SECTION_NAME
                };

        queryResults = db.query(
                false,
                NytDatabase.TABLE_BOOKMARKED_ARTICLES,
                nytBookmarkedTableColumns,
                null,
                null,
                null,
                null,
                null,
                null);

        // SET CURSOR FOR DEBUGGING
        setCursor(queryResults);

        // GET ALL COLUMNS INDEX
        int bookmarkedIdColIndex
                = queryResults.getColumnIndex(NytDatabase.COL_ID);
        int webUrlColIndex
                = queryResults.getColumnIndex(NytDatabase.WEB_URL);
        int snippetColIndex
                = queryResults.getColumnIndex(NytDatabase.SNIPPET);
        int sectionViewColIndex
                = queryResults.getColumnIndex(NytDatabase.SECTION_NAME);

        // ITERATE OVER queryResults
        while (queryResults.moveToNext())
        {
            long id
                    = queryResults.getLong(bookmarkedIdColIndex);
            String webUrl
                    = queryResults.getString(webUrlColIndex);
            String snippet
                    = queryResults.getString(snippetColIndex);
            String sectionView
                    = queryResults.getString(sectionViewColIndex);

            bookmarks.add(new NytArticle(id, webUrl, snippet, sectionView));
        }
        Collections.reverse(bookmarks);
        bookmarkAdapter = new BookmarksAdapter();
        bookmarkedArticlesListView.setAdapter(bookmarkAdapter);

        // WANT TO DELETE SEARCH TAG?
        searchTagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                clickPosition = position;
                clickIdPosition = id;

                Snackbar sb
                        = Snackbar.make(parent, "Do you want to delete this search input?", Snackbar.LENGTH_LONG)
                        .setAction("Click here", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View e)
                            {
                                db.delete(NytDatabase.TABLE_SEARCH_TAGS_NAME,
                                        NytDatabase.COL_ID + "=?",
                                        new String[]{Long.toString(searchTags.get(position).getNyt_Id())});
                                searchTags.remove(clickPosition);
                                searchTagAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(),
                                        "Search input deleted successfully!", Toast.LENGTH_LONG).show();
                            }
                        }).setActionTextColor(Color.YELLOW);
                searchTagAdapter.notifyDataSetChanged();
                sb.show();
            }
        });

        // WANT TO VISIT THE FULL ARTCILE?
        bookmarkedArticlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {

                Log.i("Position IS: ", Integer.toString(position));
                Log.i("Id IS: ", Long.toString(id));
                Log.i("WebUrl IS: ", bookmarks.get(position).getWebUrl());

                Snackbar sb
                        = Snackbar.make(parent, "Do you want to see the full article?", Snackbar.LENGTH_LONG)
                        .setAction("Click here", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View e)
                            {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bookmarks.get(position).getWebUrl())));
                            }
                        }).setActionTextColor(Color.WHITE);
                bookmarkAdapter.notifyDataSetChanged();
                sb.show();
            }
        });

        // DELETE BOOKMARKED ARTICLE
        bookmarkedArticlesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                Log.i("Position IS: ", Integer.toString(position));
                Log.i("Id IS: ", Long.toString(id));
                clickPosition = position;
                clickIdPosition = id;
                Snackbar sb
                        = Snackbar.make(parent,
                        "Do you want to delete this article?", Snackbar.LENGTH_LONG)
                        .setAction("YES", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View e)
                            {
                                db.delete(NytDatabase.TABLE_BOOKMARKED_ARTICLES,
                                        NytDatabase.COL_ID + "=?",
                                        new String[]{Long.toString(bookmarks.get(position).getId())});
                                bookmarks.remove(clickPosition);
                                bookmarkAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(),
                                        "Article deleted successfully!", Toast.LENGTH_LONG).show();
                            }
                        }).setActionTextColor(Color.RED);

                sb.show();

                return true;
            }

        });

        return nytBookmarksLayout;
    }

    /**
     * Create an instance of {@link NytDatabase} having two tables:
     * SearchTags
     * BookmarkedArticles
     */
    private NytDatabase nytdb;

    /**
     * Initiate a {@link SQLiteDatabase} connection
     */
    private SQLiteDatabase db;

    /**
     * Used to retrieve queries to fill a {@link ListView}
     */
    private Cursor queryResults;

    /**
     * Object responsible for holding all {@link NytSearchTags} searchTags
     * from the {@link NytDatabase} nytdb
     */
    private ArrayList<NytSearchTags> searchTags;

    /**
     * Object responsible for holding all {@link NytArticle} searchTags
     * from the {@link NytDatabase} nytdb
     */
    private ArrayList<NytArticle> bookmarks;

    /**
     * Inflates {@link ListView} searchTagsListView rows
     * using {@link ArrayList<NytSearchTags>} searchTags
     */
    private SearchTagsAdapter searchTagAdapter;

    /**
     * Inflates {@link ListView} bookmarkedArticlesListView rows
     * using {@link ArrayList<NytSearchTags>} searchTags
     */
    private BookmarksAdapter bookmarkAdapter;

    /**
     * These objects are inflated by:
     * {@link SearchTagsAdapter} searchTagAdapter, and
     * {@link BookmarksAdapter} bookmarkAdapter
     */
    private ListView searchTagsListView, bookmarkedArticlesListView;

    /**
     * Used for {@link Log} a {@link ListView} position
     */
    protected int clickPosition;

    /**
     * Used for {@link Log} a {@link ListView} id
     */
    protected long clickIdPosition;

    /**
     * Used to set values inside onCreate()
     *
     * @param queryResults {@link Cursor}
     */
    public void setCursor(Cursor queryResults)
    {
        this.queryResults = queryResults;
    }

    /**
     * Inflates {@link ListView} searchTagsListView
     */
    protected class SearchTagsAdapter extends BaseAdapter
    {
        /**
         * This View holds each inflated element of {@link ListView}
         * with a {@link NytSearchTags}
         */
        private View rowView;

        /**
         * All elements from the layout where this ListView is been inflated.
         */
        private TextView timeSearched, searchTag;

        /**
         * @return {@link NytSearchTags} size.
         */
        @Override
        public int getCount()
        {
            return searchTags.size();
        }

        /**
         * @param position is an index used to get objects from {@link ArrayList<NytSearchTags>}
         * @return a single {@link NytSearchTags} based on given parameter position.
         */
        public NytSearchTags getItem(int position)
        {
            return searchTags.get(position);
        }

        /**
         * Inflates a row of a {@link ListView}
         *
         * @param position    {@link Integer} index of {@link ArrayList<NytSearchTags>}
         * @param convertView {@link View}
         * @param parent      {@link ViewGroup}
         * @return {@link View}
         */
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            rowView = inflater.inflate(R.layout.activity_nyt_search_tags, parent, false);

            timeSearched = rowView.findViewById(R.id.nytSearchTagTimeWhenSearchedTextView);
            timeSearched.setText(getItem(position).getNyt_search_time());

            searchTag = rowView.findViewById(R.id.nytSearchTagTextView);
            searchTag.setText(getItem(position).getNyt_search_tag());

            return rowView;
        }

        /**
         * Get Database Position Id
         *
         * @param position {@link Integer}
         * @return {@link Long}
         */
        public long getItemId(int position)
        {
            return position;
        }
    }

    /**
     * Inflates {@link ListView} bookmarkedArticlesListView
     */
    public class BookmarksAdapter extends BaseAdapter
    {
        /**
         * This View holds each inflated element of {@link ListView}
         * with a {@link NytArticle}
         */
        private View rowView;

        /**
         * All elements from the layout where this ListView is been inflated.
         */
        private TextView snippetTextView, sectionTextView;

        /**
         * @return {@link NytArticle} size.
         */
        @Override
        public int getCount()
        {
            return bookmarks.size();
        }

        /**
         * @param position is an index used to get objects from {@link ArrayList<NytArticle>}
         * @return a single {@link NytArticle} based on given parameter position.
         */
        public NytArticle getItem(int position)
        {
            return bookmarks.get(position);
        }

        /**
         * Inflates a row of a {@link ListView}
         *
         * @param position    {@link Integer} index of {@link ArrayList<NytArticle>}
         * @param convertView {@link View}
         * @param parent      {@link ViewGroup}
         * @return {@link View}
         */
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            rowView = inflater.inflate(R.layout.activity_nyt_article, parent, false);

            snippetTextView = rowView.findViewById(R.id.nytSnippet);
            snippetTextView.setText(getItem(position).getSnippet());

            sectionTextView = rowView.findViewById(R.id.nytSectionName);
            sectionTextView.setText(getItem(position).getSection_name());

            return rowView;
        }

        /**
         * Get Database Position Id
         *
         * @param position {@link Integer}
         * @return {@link Long}
         */
        public long getItemId(int position)
        {
            return position;
        }
    }

} // NytArticleFragment END
