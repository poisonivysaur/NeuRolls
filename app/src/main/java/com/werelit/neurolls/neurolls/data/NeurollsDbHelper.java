package com.werelit.neurolls.neurolls.data;


import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.werelit.neurolls.neurolls.data.MediaContract.FilmEntry;

/**
 * Database helper for Neurolls app. Manages database creation and version management.
 */
public class NeurollsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = NeurollsDbHelper.class.getSimpleName();

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
    public NeurollsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + FilmEntry.TABLE_NAME + " ("
                + FilmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FilmEntry.COLUMN + " TEXT NOT NULL, "
                + FilmEntry.COLUMN_PET_BREED + " TEXT, "
                + FilmEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + FilmEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}