package com.werelit.neurolls.neurolls;

import android.provider.BaseColumns;

public class MediaContract {

    public static final class FilmEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "films";
        public static final String COLUMN_YEAR_RELEASED = "year_released";
        public static final String COLUMN_DIRECTOR = "director";
    }

    public static final class BookEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "books";
        public static final String COLUMN_YEAR_PUBLISHED = "year_published";
        public static final String COLUMN_AUTHOR = "author";
    }

    public static final class GameEntry implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "games";
        public static final String COLUMN_YEAR_RELEASED = "year_released";
        public static final String COLUMN_PLATFORM = "platform";
        public static final String COLUMN_COMPOSER = "composer";
        public static final String COLUMN_WRITER = "writer";
        public static final String COLUMN_DESIGNER = "designer";
    }
}
