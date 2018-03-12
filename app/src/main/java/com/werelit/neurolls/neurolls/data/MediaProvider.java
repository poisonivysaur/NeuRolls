package com.werelit.neurolls.neurolls.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.werelit.neurolls.neurolls.MediaKeys;
import com.werelit.neurolls.neurolls.data.MediaContract.FilmEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.BookEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.GameEntry;
import com.werelit.neurolls.neurolls.model.Book;
import com.werelit.neurolls.neurolls.model.Film;

/**
 * {@link ContentProvider} for NeuRolls app.
 */
public class MediaProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = MediaProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the films table */
    private static final int FILMS = 100;

    /** URI matcher code for the content URI for a single film in the films table */
    private static final int FILM_ID = 101;

    // TODO for animes, tv series, there will be an additional parameter in the URI

    /** URI matcher code for the content URI for the books table */
    private static final int BOOKS = 200;

    /** URI matcher code for the content URI for a single book in the books table */
    private static final int BOOK_ID = 201;

    /** URI matcher code for the content URI for the games table */
    private static final int GAMES = 300;

    /** URI matcher code for the content URI for a single game in the games table */
    private static final int GAME_ID = 301;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.werelit.neurolls.neurolls/films" will map to the
        // integer code {@link #FILMS}. This URI is used to provide access to MULTIPLE rows
        // of the films table.
        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_FILMS, FILMS);

        // The content URI of the form "content://com.werelit.neurolls.neurolls/films/#" will map to the
        // integer code {@link #FILM_ID}. This URI is used to provide access to ONE single row
        // of the films table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.werelit.neurolls.neurolls/films/3" matches, but
        // "content://com.werelit.neurolls.neurolls/films" (without a number at the end) doesn't match.
        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_FILMS + "/#", FILM_ID);

        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_BOOKS + "/#", BOOK_ID);

        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_GAMES, GAMES);
        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_GAMES + "/#", GAME_ID);
    }

    /** Database helper object */
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
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FILMS:
                // For the FILMS code, query the films table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the films table.
                cursor = database.query(FilmEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FILM_ID:
                // For the FILM_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.werelit.neurolls.neurolls/films/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = FilmEntry.COLUMN_FILM_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the films table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(FilmEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOKS:
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookEntry.COLUMN_BOOK_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GAMES:
                cursor = database.query(GameEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GAME_ID:
                selection = GameEntry.COLUMN_GAME_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(GameEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FILMS:
                return insertMedia(uri, contentValues, FilmEntry.TABLE_NAME);
            case BOOKS:
                return insertMedia(uri, contentValues, BookEntry.TABLE_NAME);
            case GAMES:
                return insertMedia(uri, contentValues, GameEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a media into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertMedia(Uri uri, ContentValues values, String tableName) {
        /* no need for this as inputs are automatically provided
        // Check that the name is not null
        String id = values.getAsString(FilmEntry.COLUMN_FILM_ID);
        if (id == null) {
            throw new IllegalArgumentException("Film requires an ID");
        }

        String name = values.getAsString(FilmEntry.COLUMN_FILM_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Film requires a name");
        }

        // If the duration is provided, check that it's greater than or equal to 0 mins
        Integer duration = values.getAsInteger(FilmEntry.COLUMN_FILM_DURATION);
        if (duration != null && duration < 0) {
            throw new IllegalArgumentException("Film requires valid duratoin");
        }
        */
        // TODO SANITY CHECKING HERE!!!!!!!!!!!!!!!!!!!!!!!!!

        // TODO: check date picker to see if date chosen is greater than or equal to the current date

        // TODO: same goes with notif settings

        // No need to check the other attributes, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long status = -1;
        try {
            // Insert the new pet with the given values
            status = database.insertOrThrow(tableName, null, values);
            // If the ID is -1, then the insertion failed. Log an error and return null.
        }catch (SQLiteConstraintException e){
            Log.e(LOG_TAG, "Media already exists!" + uri);
            return null;
        }
        if (status == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the media content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, status);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FILMS: return updateMedia(uri, contentValues, selection, selectionArgs, FilmEntry.TABLE_NAME);
            case BOOKS: return updateMedia(uri, contentValues, selection, selectionArgs, BookEntry.TABLE_NAME);
            case GAMES: return updateMedia(uri, contentValues, selection, selectionArgs, GameEntry.TABLE_NAME);
            case FILM_ID:
                // For the FILM_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = FilmEntry.COLUMN_FILM_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMedia(uri, contentValues, selection, selectionArgs, FilmEntry.TABLE_NAME);
            case BOOK_ID:
                selection = BookEntry.COLUMN_BOOK_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMedia(uri, contentValues, selection, selectionArgs, BookEntry.TABLE_NAME);
            case GAME_ID:
                selection = GameEntry.COLUMN_GAME_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMedia(uri, contentValues, selection, selectionArgs, GameEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update media in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateMedia(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {

        // TODO SANITY CHECKING HERE!!!!!!!!!!!!!!!!!!!!!!!!!
        /*
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        * */

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(tableName, values, selection, selectionArgs);
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