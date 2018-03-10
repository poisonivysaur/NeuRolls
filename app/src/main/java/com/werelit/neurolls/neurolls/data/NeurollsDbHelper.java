package com.werelit.neurolls.neurolls.data;


import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.werelit.neurolls.neurolls.data.MediaContract.FilmEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.BookEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.GameEntry;

/**
 * Database helper for Neurolls app. Manages database creation and version management.
 */
public class NeurollsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = NeurollsDbHelper.class.getSimpleName();

    private int mediaType = 0;

    /** Name of the database file */
    private static final String DATABASE_NAME = "neurolls.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link NeurollsDbHelper}.
     *
     * @param context of the app
     */
    public NeurollsDbHelper(Context context, int mediaType) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mediaType = mediaType;
    }

    public NeurollsDbHelper(Context context) {
        this(context, 1);
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
       // TODO: Query

        String SQL_CREATE_FILMS_TABLE =  "CREATE TABLE " + FilmEntry.TABLE_NAME + " ("
                + FilmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FilmEntry.COLUMN_FILM_ID + " TEXT NOT NULL, "
                + FilmEntry.COLUMN_FILM_NAME + " TEXT NOT NULL, "
                + FilmEntry.COLUMN_FILM_GENRE + " TEXT, "
                + FilmEntry.COLUMN_FILM_YEAR_RELEASED + " DATE, "

                + FilmEntry.COLUMN_FILM_DIRECTOR + " TEXT, "
                + FilmEntry.COLUMN_FILM_DURATION + " INTEGER, "
                + FilmEntry.COLUMN_FILM_PRODUCTION + " TEXT, "
                + FilmEntry.COLUMN_FILM_SYNOPSIS + " TEXT, "

                + FilmEntry.COLUMN_FILM_IMG_DIR + " TEXT, "
                + FilmEntry.COLUMN_FILM_DATE_TO_WATCH + " DATE, "
                + FilmEntry.COLUMN_FILM_NOTIF_SETTINGS + " TEXT,"
                + FilmEntry.COLUMN_FILM_ARCHIVED + " INTEGER NOT NULL DEFAULT 0, "
                + FilmEntry.COLUMN_FILM_WATCHED + " INTEGER NOT NULL DEFAULT 0); ";

        String SQL_CREATE_BOOKS_TABLE =  "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_ID + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_GENRE + " TEXT, "
                + BookEntry.COLUMN_BOOK_YEAR_PUBLISHED + " DATE, "

                + BookEntry.COLUMN_BOOK_AUTHOR + " TEXT, "
                + BookEntry.COLUMN_BOOK_PAGES + " INTEGER NOT NULL DEFAULT 0, "
                + BookEntry.COLUMN_BOOK_PUBLISHER + " TEXT, "
                + BookEntry.COLUMN_BOOK_DESCRIPTION + " TEXT, "

                + BookEntry.COLUMN_BOOK_IMG_DIR + " TEXT, "
                + BookEntry.COLUMN_BOOK_DATE_TO_READ + " DATE, "
                + BookEntry.COLUMN_BOOK_NOTIF_SETTINGS + " TEXT,"
                + BookEntry.COLUMN_BOOK_ARCHIVED + " INTEGER NOT NULL DEFAULT 0, "
                + BookEntry.COLUMN_BOOK_READ + " INTEGER NOT NULL DEFAULT 0); ";

        String SQL_CREATE_GAMES_TABLE =  "CREATE TABLE " + GameEntry.TABLE_NAME + " ("
                + GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GameEntry.COLUMN_GAME_ID + " TEXT NOT NULL, "
                + GameEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, "
                + GameEntry.COLUMN_GAME_GENRE + " TEXT, "
                + GameEntry.COLUMN_GAME_YEAR_RELEASED + " DATE, "

                + GameEntry.COLUMN_GAME_PLATFORM + " TEXT, "
                + GameEntry.COLUMN_GAME_PUBLISHER + " TEXT, "
                + GameEntry.COLUMN_GAME_SERIES + " TEXT, "
                + GameEntry.COLUMN_GAME_STORYLINE + " TEXT, "

                + GameEntry.COLUMN_GAME_IMG_DIR + " TEXT, "
                + GameEntry.COLUMN_GAME_DATE_TO_PLAY + " DATE, "
                + GameEntry.COLUMN_GAME_NOTIF_SETTINGS + " TEXT,"
                + GameEntry.COLUMN_GAME_ARCHIVED + " INTEGER NOT NULL DEFAULT 0, "
                + GameEntry.COLUMN_GAME_PLAYED + " INTEGER NOT NULL DEFAULT 0); ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_FILMS_TABLE);
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        db.execSQL(SQL_CREATE_GAMES_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}