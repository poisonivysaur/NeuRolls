package com.werelit.neurolls.neurolls;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewFilmDetailsActivity extends AppCompatActivity{
    public final static String MEDIA_NAME_KEY = "MEDIA_NAME_KEY";
    public final static String MEDIA_GENRE_KEY = "MEDIA_GENRE_KEY";
    public final static String MEDIA_YEAR_KEY = "MEDIA_YEAR_KEY";

    public TextView name, genre, year;
    public ImageView image;
    public View rootView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_film_detail);

        name = (TextView) findViewById(R.id.name);
        genre = (TextView) findViewById(R.id.genre);
        year = (TextView) findViewById(R.id.year);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){ // there is a bundle
            String mediaName = bundle.getString(MEDIA_NAME_KEY);
            String mediaGenre = bundle.getString(MEDIA_GENRE_KEY);
            int mediaYear = bundle.getInt(MEDIA_YEAR_KEY, 0);

            name.setText("" + mediaName);
            genre.setText("" + mediaGenre);
            year.setText("" + mediaYear);
        }
    }
}
