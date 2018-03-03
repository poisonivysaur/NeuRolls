package com.werelit.neurolls.neurolls;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewFilmDetailsActivity extends AppCompatActivity {

    public TextView name, genre, year;
    public ImageView image;
    public View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) { // there is a bundle
            int mediaCategory = bundle.getInt(MediaKeys.MEDIA_CATEGORY_KEY);
            String mediaName = bundle.getString(MediaKeys.MEDIA_NAME_KEY);
            String mediaGenre = bundle.getString(MediaKeys.MEDIA_GENRE_KEY);
            int mediaYear = bundle.getInt(MediaKeys.MEDIA_YEAR_KEY, 0);

            if (mediaCategory == CategoryAdapter.CATEGORY_FILMS) {
                setContentView(R.layout.view_film_detail);
                TextView duration = (TextView) findViewById(R.id.duration);
                TextView director = (TextView) findViewById(R.id.director);
                TextView production = (TextView) findViewById(R.id.production);
                TextView synopsis = (TextView) findViewById(R.id.synopsis);

                int filmDuration = bundle.getInt(MediaKeys.FILM_DURATION_KEY);
                String filmDirector = bundle.getString(MediaKeys.FILM_DIRECTOR_KEY);
                String filmProduction = bundle.getString(MediaKeys.FILM_PRODUCTION_KEY);
                String filmSynopsis = bundle.getString(MediaKeys.FILM_SYNOPSIS_KEY);

                duration.setText("" + filmDuration);
                director.setText(filmDirector);
                production.setText(filmProduction);
                synopsis.setText(filmSynopsis);

            }else if(mediaCategory == CategoryAdapter.CATEGORY_BOOKS) {
                setContentView(R.layout.view_book_detail);
                TextView author = (TextView) findViewById(R.id.author);
                TextView publisher = (TextView) findViewById(R.id.publisher);
                TextView description = (TextView) findViewById(R.id.description);

                String bookAuthor = bundle.getString(MediaKeys.BOOK_AUTHOR_KEY);
                String bookPublisher = bundle.getString(MediaKeys.BOOK_PUBLISHER_KEY);
                String bookDescription = bundle.getString(MediaKeys.BOOK_DESCRIPTION_KEY);

                author.setText(bookAuthor);
                publisher.setText(bookPublisher);
                description.setText(bookDescription);

            }else if(mediaCategory == CategoryAdapter.CATEGORY_GAMES) {

                setContentView(R.layout.view_game_detail);
                TextView platform = (TextView) findViewById(R.id.platform);
                TextView publisher = (TextView) findViewById(R.id.publisher);
                TextView series = (TextView) findViewById(R.id.series);
                TextView storyline = (TextView) findViewById(R.id.storyline);

                String gamePlatform = bundle.getString(MediaKeys.GAME_PLATFORM_KEY);
                String gamePublisher = bundle.getString(MediaKeys.GAME_PUBLISHER_KEY);
                String gameSeries = bundle.getString(MediaKeys.GAME_PLATFORM_KEY);
                String gameStoryline = bundle.getString(MediaKeys.GAME_STORYLINE_KEY);

                platform.setText(gamePlatform);
                publisher.setText(gamePublisher);
                series.setText(gameSeries);
                storyline.setText(gameStoryline);
            }

            name = (TextView) findViewById(R.id.name);
            genre = (TextView) findViewById(R.id.genre);
            year = (TextView) findViewById(R.id.year);

            name.setText("" + mediaName);
            genre.setText("" + mediaGenre);
            year.setText("" + mediaYear);
        }
    }
}
