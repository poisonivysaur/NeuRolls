package com.werelit.neurolls.neurolls.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MediaContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.werelit.neurolls.neurolls";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_FILMS = "films";
    public static final String PATH_BOOKS = "books";
    public static final String PATH_GAMES = "games";

    // TODO path anime, tv-series, comics, manga paths

    public static final class FilmEntry implements BaseColumns{

        /** The content URI to access the film data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FILMS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of films.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single film.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILMS;

        /** Name of database table for films */
        public static final String TABLE_NAME = "films";

        public static final String COLUMN_FILM_ID = "film_id";
        public static final String COLUMN_FILM_NAME = "film_name";
        public static final String COLUMN_FILM_GENRE = "genre";
        public static final String COLUMN_FILM_YEAR_RELEASED = "year_released";
        public static final String COLUMN_FILM_IMG_DIR = "image_directory";

        public static final String COLUMN_FILM_DIRECTOR = "director";
        public static final String COLUMN_FILM_DURATION = "duration";
        public static final String COLUMN_FILM_PRODUCTION = "production";
        public static final String COLUMN_FILM_SYNOPSIS = "synopsis";

        public static final String COLUMN_FILM_DATE_TO_WATCH = "date_to_watch";
        public static final String COLUMN_FILM_NOTIF_SETTINGS = "notif_settings";
        public static final String COLUMN_FILM_NOTIF_TIME = "notif_time";
        public static final String COLUMN_FILM_WATCHED = "isWatched";
        public static final String COLUMN_FILM_ARCHIVED = "isArchived";

        public static final String COLUMN_LAST_UPDATE = "last_update";
    }

    public static final class BookEntry implements BaseColumns{

        /** The content URI to access the book data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /** Name of database table for books */
        public static final String TABLE_NAME = "books";

        public static final String COLUMN_BOOK_ID = "book_id";
        public static final String COLUMN_BOOK_NAME = "book_name";
        public static final String COLUMN_BOOK_GENRE = "genre";
        public static final String COLUMN_BOOK_YEAR_PUBLISHED = "year_published";
        public static final String COLUMN_BOOK_IMG_DIR = "image_directory";

        public static final String COLUMN_BOOK_AUTHOR = "author";
        public static final String COLUMN_BOOK_PAGES = "pages";
        public static final String COLUMN_BOOK_PUBLISHER = "publisher";
        public static final String COLUMN_BOOK_DESCRIPTION = "description";

        public static final String COLUMN_BOOK_DATE_TO_READ = "date_to_read";
        public static final String COLUMN_BOOK_NOTIF_SETTINGS = "notif_settings";
        public static final String COLUMN_BOOK_NOTIF_TIME = "notif_time";
        public static final String COLUMN_BOOK_READ = "isRead";
        public static final String COLUMN_BOOK_ARCHIVED = "isArchived";

        public static final String COLUMN_LAST_UPDATE = "last_update";
    }

    public static final class GameEntry implements BaseColumns{

        /** The content URI to access the game data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GAMES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of games.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single game.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        /** Name of database table for games */
        public static final String TABLE_NAME = "games";

        public static final String COLUMN_GAME_ID = "game_id";
        public static final String COLUMN_GAME_NAME = "game_name";
        public static final String COLUMN_GAME_GENRE = "genre";
        public static final String COLUMN_GAME_YEAR_RELEASED = "year_released";
        public static final String COLUMN_GAME_IMG_DIR = "image_directory";

        public static final String COLUMN_GAME_PLATFORM = "platform";
        public static final String COLUMN_GAME_PUBLISHER = "publisher";
        public static final String COLUMN_GAME_SERIES = "series";
        public static final String COLUMN_GAME_STORYLINE = "storyline";

        public static final String COLUMN_GAME_DATE_TO_PLAY = "date_to_play";
        public static final String COLUMN_GAME_NOTIF_SETTINGS = "notif_settings";
        public static final String COLUMN_GAME_NOTIF_TIME = "notif_time";
        public static final String COLUMN_GAME_PLAYED = "isPlayed";
        public static final String COLUMN_GAME_ARCHIVED = "isArchived";

        public static final String COLUMN_LAST_UPDATE = "last_update";
    }
}
