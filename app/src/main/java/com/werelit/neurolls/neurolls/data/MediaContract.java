package com.werelit.neurolls.neurolls.data;

import android.provider.BaseColumns;

public class MediaContract {

    public static final class FilmEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "films";

        public static final String COLUMN_FILM_ID = "film_id";
        public static final String COLUMN_FILM_NAME = "film_name";
        public static final String COLUMN_FILM_GENRE = "genre";
        public static final String COLUMN_FILM_YEAR_RELEASED = "year_released";

        public static final String COLUMN_FILM_DIRECTOR = "director";
        public static final String COLUMN_FILM_DURATION = "duration";
        public static final String COLUMN_FILM_PRODUCTION = "production";
        public static final String COLUMN_FILM_SYNOPSIS = "synopsis";

        public static final String COLUMN_FILM_IMG_DIR = "image_directory";
        public static final String COLUMN_FILM_DATE_TO_WATCH = "date_to_watch";
        public static final String COLUMN_FILM_NOTIF_SETTINGS = "notif_settings";
        public static final String COLUMN_FILM_WATCHED = "isWatched";
        public static final String COLUMN_FILM_ARCHIVED = "isArchived";
    }

    public static final class BookEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "books";

        public static final String COLUMN_BOOK_ID = "book_id";
        public static final String COLUMN_BOOK_NAME = "book_name";
        public static final String COLUMN_BOOK_GENRE = "genre"; // this will become page number
        public static final String COLUMN_BOOK_YEAR_PUBLISHED = "year_published";

        public static final String COLUMN_BOOK_AUTHOR = "author";
        public static final String COLUMN_BOOK_PUBLISHER = "publisher";
        public static final String COLUMN_BOOK_DESCRIPTION = "description";

        public static final String COLUMN_BOOK_IMG_DIR = "image_directory";
        public static final String COLUMN_BOOK_DATE_TO_READ = "date_to_read";
        public static final String COLUMN_BOOK_NOTIF_SETTINGS = "notif_settings";
        public static final String COLUMN_FILM_READ = "isRead";
        public static final String COLUMN_BOOK_ARCHIVED = "isArchived";
    }

    public static final class GameEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "games";

        public static final String COLUMN_GAME_ID = "game_id";
        public static final String COLUMN_GAME_NAME = "game_name";
        public static final String COLUMN_GAME_GENRE = "genre";
        public static final String COLUMN_GAME_YEAR_RELEASED = "year_released";

        public static final String COLUMN_GAME_PLATFORM = "platform";
        public static final String COLUMN_GAME_PUBLISHER = "publisher";
        public static final String COLUMN_GAME_SERIES = "series";
        public static final String COLUMN_GAME_STORYLINE = "storyline";

        public static final String COLUMN_GAME_IMG_DIR = "image_directory";
        public static final String COLUMN_GAME_DATE_TO_PLAY = "date_to_play";
        public static final String COLUMN_GAME_NOTIF_SETTINGS = "notif_settings";
        public static final String COLUMN_FILM_PLAYED = "isPlayed";
        public static final String COLUMN_GAME_ARCHIVED = "isArchived";
    }
}
