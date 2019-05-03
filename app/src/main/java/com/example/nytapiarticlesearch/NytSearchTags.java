package com.example.nytapiarticlesearch;

import android.support.v7.app.AppCompatActivity;

import java.util.Date;

/**
 * Android Final Project
 * Project: New York Times API
 *
 * @author Felipe Marinho Soares da Silva
 * <p>
 * Each search input is stored inside a {@link NytSearchTags} object.
 */
public class NytSearchTags extends AppCompatActivity
{

    /**
     * Receives the Primary Key of the Database
     */
    private long id;

    /**
     * Search tag information from user input.
     * Time of the search and search values.
     */
    private String
            nyt_search_time,
            nyt_search_tag;

    /**
     * Initial Constructor
     *
     * @param searchTags {@link String} from {@link android.widget.EditText} nytSearchEditText
     */
    public NytSearchTags(String searchTags)
    {
        nyt_search_time = new Date().toString();
        // RE-FORMAT NEW DATE() STRING
        StringBuilder nytFormatedDate = new StringBuilder();
        nytFormatedDate
                .append(nyt_search_time.substring(4, 10)).append(", ")
                .append(nyt_search_time.substring(24, 28)).append(". ")
                .append(nyt_search_time.substring(11, 19));
        nyt_search_time = nytFormatedDate.toString();
        nyt_search_tag = searchTags;
    }

    /**
     * Overloaded Constructor
     *
     * @param id              {@link Long} Primary Key given from database.
     * @param nyt_search_time {@link String}
     * @param nyt_search_tag  {@link String}
     */
    public NytSearchTags(long id, String nyt_search_time, String nyt_search_tag)
    {
        this.id = id;
        this.nyt_search_time = nyt_search_time;
        this.nyt_search_tag = nyt_search_tag;
    }

    /**
     * @return {@link String}
     */
    public String getNyt_search_tag()
    {
        return nyt_search_tag;
    }

    /**
     * @return {@link String}
     */
    public String getNyt_search_time()
    {
        return nyt_search_time;
    }

    /**
     * @return database primary key as {@link Long}
     */
    public long getNyt_Id()
    {
        return id;
    }

    /**
     * Set {@link Long} id that represents the table primary key
     *
     * @param id {@link Long}
     */
    public void setNyt_Id(long id)
    {
        this.id = id;
    }

}
