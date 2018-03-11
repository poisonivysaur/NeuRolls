package com.werelit.neurolls.neurolls.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * {@link ContentProvider} for Medias app.
 */
public class MediaProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = MediaProvider.class.getSimpleName();

    private NeurollsDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a MediaDbHelper object to gain access to the medias database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new NeurollsDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}