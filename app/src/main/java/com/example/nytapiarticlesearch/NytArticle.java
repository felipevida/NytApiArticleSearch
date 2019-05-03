package com.example.nytapiarticlesearch;

import android.support.v7.app.AppCompatActivity;

/**
 * Android Final Project
 * Project: New York Times API
 *
 * @author Felipe Marinho Soares da Silva
 * <p>
 * Each article retrieved from New York Times API
 * is stored inside a {@link NytArticle} object.
 */
public class NytArticle extends AppCompatActivity
{

    /**
     * Receives the Primary Key of the Database
     */
    private long id;

    /**
     * JSON values used from NewYorkTimes API
     */
    private String
            webUrl,
            snippet,
            section_name;

    /**
     * Default Constructor
     */
    public NytArticle()
    {
        this("NoURL", "NoSnippet", "NoSectionName");
    }

    /**
     * Initial Constructor
     * <p>
     * Used when loading elements into {@link NewYorkTimes} Activity.
     *
     * @param webUrl       {@link String}
     * @param snippet      {@link String}
     * @param section_name {@link String}
     */
    public NytArticle(String webUrl, String snippet, String section_name)
    {
        setWebUrl(webUrl);
        setSnippet(snippet);
        setSection_name(section_name);
    }

    /**
     * Overloaded Constructor
     * <p>
     * Used when saving Articles to the Database.
     *
     * @param id
     * @param webUrl
     * @param snippet
     * @param section_name
     */
    public NytArticle(long id, String webUrl, String snippet, String section_name)
    {
        setId(id);
        setWebUrl(webUrl);
        setSnippet(snippet);
        setSection_name(section_name);
    }

    /**
     * @return {@link Long}
     */
    public long getId()
    {
        return id;
    }

    /**
     * @param id sets {@link Long} Primary Key Id
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * @return {@link String}
     */
    public String getWebUrl()
    {
        return webUrl;
    }

    /**
     * @param webUrl sets {@link String} webUrl of the full article.
     */
    public void setWebUrl(String webUrl)
    {
        this.webUrl = webUrl;
    }

    /**
     * @return {@link String}
     */
    public String getSnippet()
    {
        return snippet;
    }

    /**
     * @param snippet sets {@link String} of the article snippet.
     */
    public void setSnippet(String snippet)
    {
        this.snippet = snippet;
    }

    /**
     * @return {@link String}
     */
    public String getSection_name()
    {
        return section_name;
    }

    /**
     * @param section_name sets a {@link String} of the article section name.
     */
    public void setSection_name(String section_name)
    {
        this.section_name = section_name;
    }

}
