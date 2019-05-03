package com.example.nytapiarticlesearch;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Android Final Project
 * Project: New York Times API
 *
 * @author Felipe Marinho Soares da Silva
 * <p>
 * This Class is the Main Page of NytArticlesSearch Activity
 */
public class MainActivity extends AppCompatActivity
{

    /**
     * Checks if FrameLayout is loaded by a larger screen inside onCreate().
     */
    private boolean isTablet;

    /**
     * Holds the ITEM selected in a fragment.
     */
    public static final String ITEM_SELECTED = "ITEM";

    /**
     * Holds the POSITION of an item in a fragment.
     */
    public static final String ITEM_POSITION = "POSITION";

    /**
     * Holds the ID of a selected ITEM.
     */
    public static final String ITEM_ID = "ID";

    /**
     * Saves the last search tag.
     */
    private SharedPreferences sp;

    /**
     * Used to input search tags to find articles.
     */
    private EditText nytSearchEditText;

    /**
     * Triggers user search.
     */
    private Button nytSearchButton;

    /**
     * Holds search results.
     */
    public ArrayList<NytArticle> articles;

    /**
     * Loads icons for navigation.
     */
    private Toolbar nytToolbar;

    /**
     * Loads Articles from an ArrayList of {@link NytArticle}.
     */
    private ListView nytListView;

    /**
     * Shows the loading progress whenever a search is made.
     */
    private ProgressBar nytProgressBar;

    /**
     * Responsible for loading the next Activity.
     */
    private Intent nextPage;

    /**
     * Used to load items into {@link ListView} nytListView
     * using {@link NytArticle} articles.
     */
    private ArticlesAdapter nytAdapter;

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
     * Used to track an user longClick in a {@link ListView} item.
     */
    private int longClickPosition;

    /**
     * Value used to open a fragment into on a phone.
     */
    public static final int EMPTY_ACTIVITY = 345;

    /**
     * Loads resources to display the Activity.
     *
     * @param savedInstanceState {@link Bundle}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INI Database
        nytdb = new NytDatabase(this);
        db = nytdb.getWritableDatabase();

        // Check if it's a Tablet
        isTablet = ( FrameLayout ) findViewById(R.id.nytFrameLayout) != null;
        Log.i("isTablet IS", Boolean.toString(isTablet));


        // INI ACTIVITY NEW YORK TIME ELEMENTS
        nytSearchEditText = ( EditText ) findViewById(R.id.nytEditText);
        nytSearchButton = ( Button ) findViewById(R.id.nytSearchButton);
        nytListView = ( ListView ) findViewById(R.id.nytListView);
        nytProgressBar = ( ProgressBar ) findViewById(R.id.nytProgressBar);
        nytProgressBar.setVisibility(View.INVISIBLE);
        nytToolbar = findViewById(R.id.nytToolBar);
        setActionBar(nytToolbar);

        // INITIALIZING SHARED PREFERENCES
        sp = getSharedPreferences("Search", Context.MODE_PRIVATE);
        String savedString = sp.getString("PreviousSearch", "");
        nytSearchEditText.setText(savedString);

        nytSearchButton.setOnClickListener(new View.OnClickListener()
                                           {
                                               @Override
                                               public void onClick(View s)
                                               {
                                                   String nytSearchTag = nytSearchEditText.getText().toString();
                                                   if (nytSearchTag == null || nytSearchTag.equals(""))
                                                   {
                                                       String nytToastEmptySearch = "You need to input something.";
                                                       Toast.makeText(getApplicationContext(), (( CharSequence ) nytToastEmptySearch), Toast.LENGTH_LONG).show();
                                                   } else
                                                   {

                                                       String nytToastText = "Searching for: " + nytSearchTag;
                                                       Toast.makeText(getApplicationContext(), (( CharSequence ) nytToastText), Toast.LENGTH_LONG).show();

                                                       articles = new ArrayList<>();
                                                       nytProgressBar.setVisibility(View.VISIBLE);

                                                       // PREPARE TO QUERY
                                                       NytQuery nytQuery = new NytQuery();

                                                       String searchQuery
                                                               = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q="
                                                               + nytSearchTag
                                                               + "&api-key=89kmL9QdZSaSnHNrZtgRuPmf11e3mPQh";
                                                       nytQuery.execute(searchQuery);

                                                       Log.i("SearchInput", nytSearchTag);
                                                       Log.i("FullSearchURL:", searchQuery);

                                                       nytListView.setAdapter(nytAdapter);

                                                       // ADD ROW TO DATABASE
                                                       ContentValues newRowValue = new ContentValues();

                                                       NytSearchTags newTag = new NytSearchTags(nytSearchTag);

                                                       newRowValue.put(
                                                               NytDatabase.TIME_WHEN_SEARCH,
                                                               newTag.getNyt_search_time());

                                                       newRowValue.put(
                                                               NytDatabase.SEARCH_TAGS,
                                                               newTag.getNyt_search_tag());

                                                       long id = db.insert(NytDatabase.TABLE_SEARCH_TAGS_NAME, null, newRowValue);

                                                       // Hide keyboard
                                                       nytSearchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                                                   } // END ELSE

                                                   // CHECKS IF THERE'S ANY FRAGMENT LOADED
                                                   // IF YES, REMOVES THE OLD ONE
                                                   checkIfThereIsAnyFragment();

                                                   Log.i("Fragments are", String.valueOf(getSupportFragmentManager().getFragments()));

                                               } // END onClick()

                                           }
        );

        nytListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {

                Log.i("Position IS: ", Integer.toString(position));
                Log.i("Id IS: ", Long.toString(id));
                Log.i("WebUrl IS: ", articles.get(position).getWebUrl());

                Snackbar sb
                        = Snackbar.make(parent, "Do you want to see the full article?", Snackbar.LENGTH_LONG)
                        .setAction("Click here", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View e)
                            {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(articles.get(position).getWebUrl())));
                            }
                        }).setActionTextColor(Color.WHITE);

                sb.show();
            }
        });

        nytListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                longClickPosition = position;
                Snackbar sb
                        = Snackbar.make(parent, "Do you want to bookmark this article?", Snackbar.LENGTH_LONG)
                        .setAction("YES", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View e)
                            {
                                // ADD ROW TO DATABASE
                                ContentValues newRowValue = new ContentValues();

                                newRowValue.put(
                                        NytDatabase.SECTION_NAME,
                                        articles.get(longClickPosition).getSection_name());

                                newRowValue.put(
                                        NytDatabase.SNIPPET,
                                        articles.get(longClickPosition).getSnippet());

                                newRowValue.put(
                                        NytDatabase.WEB_URL,
                                        articles.get(longClickPosition).getWebUrl());

                                long id = db.insert(NytDatabase.TABLE_BOOKMARKED_ARTICLES, null, newRowValue);

                                Toast.makeText(getApplicationContext(), "Article added successfully!", Toast.LENGTH_LONG).show();

                                checkIfThereIsAnyFragment();

                            }
                        }).setActionTextColor(Color.GREEN);

                sb.show();

                return true;
            }
        });

    }

    /**
     * Inflates the action bar Menu
     *
     * @param menu {@link Menu}
     * @return true {@link Boolean}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newyorktimes_menu, menu);
        ArrayList<NytArticle> arcts;
        return true;
    }

    /**
     * Responsible for redirecting the user
     * to a new {@link android.app.Activity} or {@link android.app.Fragment}.
     *
     * @param item {@link MenuItem}
     * @return true {@link Boolean}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nytMenuBookmarks:

                Bundle dataToPass = new Bundle();
                NytArticleFragment articleFragment = new NytArticleFragment();
                articleFragment.setArguments(dataToPass); //pass it a bundle for information

                if (isTablet)
                {
                    articleFragment.setTablet(true);
                    if (getSupportFragmentManager().getFragments().size() > 0)
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(getSupportFragmentManager().getFragments().get(0))
                                .add(R.id.nytFrameLayout, articleFragment, "NYT_FRAGMENT")
                                .commit();
                    else
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.nytFrameLayout, articleFragment, "NYT_FRAGMENT")
                                .commit();

                } else
                {
                    nextPage = new Intent(MainActivity.this, NytEmptyActivity.class);
                    nextPage.putExtras(dataToPass);
                    startActivityForResult(nextPage, EMPTY_ACTIVITY);
                }
                break;

            case R.id.nytMenuInfo:

                showAboutAndHowToAlertDialog();

                break;

        }
        return true;

    } // onOptionsItemSelected END

    /**
     * Creates a {@link View} on top of the {@link android.app.Activity}
     */
    public void showAboutAndHowToAlertDialog()
    {
        View middle = getLayoutInflater().inflate(R.layout.activity_nyt_alert_message, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("About")
                .setPositiveButton("Close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // DO NOTHING.
                    }
                }).setView(middle);

        builder.create().show();
    }

    /**
     * Loads resources when the {@link android.app.Activity} life cycle
     * is on pause state.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        // Saving search input
        SharedPreferences.Editor editor = sp.edit();
        String whatWasTyped = nytSearchEditText.getText().toString();
        editor.putString("PreviousSearch", whatWasTyped);
        editor.commit();
    }

    /**
     * Creates a query using {@link EditText} nytSearchButton.
     * The URL is customized for each search
     * that access NewYorkTimes API to retrieve a {@link JSONObject}
     */
    public class NytQuery extends AsyncTask<String, Integer, String>
    {
        /**
         * Values from JSON object from New York Times API
         * are going to be stored in here.
         */
        private String web_url, section_name, snippet;

        /**
         * This method runs in the background while connects to New York Times API
         * to retrieve articles.
         *
         * @param strings Receives a URL at a time.
         * @return {@link String}
         */
        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                // Get the URL from vararg.
                String myUrl = strings[0];
                URL url = new URL(myUrl);
                // Create the network connection:
                HttpURLConnection urlConnection = ( HttpURLConnection ) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader
                        = new BufferedReader(
                        new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                try
                {
                    JSONObject jObject = new JSONObject(result);
                    Log.i("JsonObject is:", jObject.toString());

                    JSONArray response = jObject.names();
                    Log.i("response IS:", response.toString());

                    JSONObject responseObject = ( JSONObject ) jObject.get("response");
                    Log.i("responseObject IS:", responseObject.toString());

                    JSONArray docsArray = ( JSONArray ) responseObject.get("docs");
                    Log.i("docsArray IS:", docsArray.toString());

                    // Looper.Prepare() ALLOWS TO MAKE MULTIPLE SEARCHES
                    if (Looper.myLooper() == null)
                        Looper.prepare();

                    int publishProgressIncrement = 100 / docsArray.length();
                    for (
                            int i = 0, publishProgressCounter = 0;
                            i < docsArray.length();
                            i++, publishProgressCounter += publishProgressIncrement)
                    {
                        JSONObject oneObject = docsArray.getJSONObject(i);

                        // Pulling items from the array
                        web_url = oneObject.getString("web_url");
                        Log.i("web_url IS:", web_url);

                        snippet = oneObject.getString("snippet");
                        Log.i("snippet IS:", snippet);

                        section_name = oneObject.getString("section_name");
                        Log.i("section_name IS:", section_name);

                        // This only works because Looper.prepare() is called
                        articles.add(new NytArticle(web_url, snippet, section_name));

                        Log.i("ARTICLE ADDED", articles.toString());

                        Thread.sleep(100);
                        nytProgressBar.setProgress(publishProgressCounter);
                        publishProgress(nytProgressBar.getProgress());

                    }

                } catch (JSONException je)
                {
                    Log.i("jObjectERROR!:", je.getMessage());
                }


            } catch (Exception ex)
            {
                Log.e("Crashed!!", ex.getMessage());
            }
            publishProgress(100);
            return "doInBackground() Finished";
        }

        /**
         * @param values {@link Integer[]}
         */
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            Log.i("nytOnProgressUpdate", "update:" + values[0]);
        }

        /**
         * When this class concurrent methods finish, this method is called.
         *
         * @param s {@link String}
         */
        @Override
        protected void onPostExecute(String s)
        {
            Log.i("On Post Execute: ", "ON POST EXECUTE.");
            nytProgressBar.setVisibility(View.INVISIBLE);
            // INI ADAPTER
            nytAdapter = new ArticlesAdapter();
            nytListView.setAdapter(nytAdapter);
            nytAdapter.notifyDataSetChanged();
        }

    } // NytQuery END

    /**
     * Inflates {@link ListView} bookmarkedArticlesListView
     */
    public class ArticlesAdapter extends BaseAdapter
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
            return articles.size();
        }

        /**
         * @param position is an index used to get objects from {@link ArrayList<NytArticle>}
         * @return a single {@link NytArticle} based on given parameter position.
         */
        public NytArticle getItem(int position)
        {
            return articles.get(position);
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

    } // ArticlesAdapter END

    /**
     * Checks if isTablet and if a fragment is loaded.
     * If yes, it will add the new one and remove the previous one at the same time.
     * Works as a refresh button.
     */
    public void checkIfThereIsAnyFragment()
    {
        if (isTablet && (getSupportFragmentManager().getFragments().size() > 0))
        {
            Bundle dataToPass = new Bundle();
            NytArticleFragment articleFragment = new NytArticleFragment();

            articleFragment.setArguments(dataToPass); //pass it a bundle for information
            articleFragment.setTablet(true);

            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().getFragments().get(0))
                    .add(R.id.nytFrameLayout, articleFragment, "NYT_FRAGMENT")
                    .commit();
        }
    }

} // NewYorkTimes END
