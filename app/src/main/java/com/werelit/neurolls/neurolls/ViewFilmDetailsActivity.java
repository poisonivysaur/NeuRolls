package com.werelit.neurolls.neurolls;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewFilmDetailsActivity extends AppCompatActivity{

    public TextView name, genre, year, duration, director, production, synopsis;
    public ImageView image;
    public View rootView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_film_detail);

        name = (TextView) findViewById(R.id.name);
        genre = (TextView) findViewById(R.id.genre);
        year = (TextView) findViewById(R.id.year);
        duration = (TextView) findViewById(R.id.duration);
        director = (TextView) findViewById(R.id.director);
        production = (TextView) findViewById(R.id.production);
        synopsis = (TextView) findViewById(R.id.synopsis);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){ // there is a bundle
            String filmName = bundle.getString(MediaKeys.MEDIA_NAME_KEY);
            String filmGenre = bundle.getString(MediaKeys.MEDIA_GENRE_KEY);
            int filmYear = bundle.getInt(MediaKeys.MEDIA_YEAR_KEY, 0);
            int filmDuration = bundle.getInt(MediaKeys.FILM_DURATION_KEY);
            String filmDirector = bundle.getString(MediaKeys.FILM_DIRECTOR_KEY);
            String filmProduction = bundle.getString(MediaKeys.FILM_PRODUCTION_KEY);
            String filmSynopsis = bundle.getString(MediaKeys.FILM_SYNOPSIS_KEY);

            name.setText("" + filmName);
            genre.setText("" + filmGenre);
            year.setText("" + filmYear);
            duration.setText("" + filmDuration);
            director.setText("" + filmDirector);
            production.setText("" + filmProduction);
            synopsis.setText("" + filmSynopsis);
        }
    }
}
