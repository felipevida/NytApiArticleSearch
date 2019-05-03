package com.example.nytapiarticlesearch;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Android Final Project
 * Project: New York Times API
 *
 * @author Felipe Marinho Soares da Silva
 * <p>
 * This class creates a database with two tables:
 * <b>SearchTags</b>: For user search inputs.
 * <b>BookmarkedArticles</b>: User saved articles.
 */
public class NytDatabase extends SQLiteOpenHelper
{

    /**
     * Database name, tables and columns.
     */
    public static final String
            DATABASE_NAME = "NytDatabase",
            COL_ID = "_id",
            TABLE_SEARCH_TAGS_NAME = "SearchTags",
            TIME_WHEN_SEARCH = "timeOfSearch",
            SEARCH_TAGS = "searchTag",
            TABLE_BOOKMARKED_ARTICLES = "BookmarkedArticles",
            WEB_URL = "webUrl",
            SNIPPET = "snippet",
            SECTION_NAME = "sectionView";

    /**
     * Current version of the Database.
     */
    public static final int VERSION_NUM = 12;

    /**
     * Initial Constructor
     *
     * @param ctx {@link Activity}
     */
    public NytDatabase(Activity ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * If the Database doesn't exist,
     * this method creates one.
     *
     * @param db {@link SQLiteDatabase}
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_SEARCH_TAGS_NAME + "( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TIME_WHEN_SEARCH + " TEXT," +
                SEARCH_TAGS + " TEXT) ");

        db.execSQL("CREATE TABLE " + TABLE_BOOKMARKED_ARTICLES + "( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WEB_URL + " TEXT," +
                SNIPPET + " TEXT," +
                SECTION_NAME + " TEXT) ");
    }

    /**
     * If a Database needs to be upgraded
     *
     * @param db         {@link SQLiteDatabase}
     * @param oldVersion {@link Integer} value
     * @param newVersion {@link Integer} value
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade",
                "Old version:" + oldVersion + " newVersion:" + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_TAGS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKED_ARTICLES);
        onCreate(db);
    }

    /**
     * If a Database needs to be downgraded
     *
     * @param db         {@link SQLiteDatabase}
     * @param oldVersion {@link Integer} value
     * @param newVersion {@link Integer} value
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade",
                "Old version:" + oldVersion + " newVersion:" + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_TAGS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKED_ARTICLES);
        onCreate(db);
    }
}
