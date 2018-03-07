package com.werelit.neurolls.neurolls;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.com.mauker.materialsearchview.MaterialSearchView;

public class SearchableActivity extends AppCompatActivity{

    // TODO implement task loader methods here instead, passing the searchFilm as a parameter
    // and set searchFilm in FilmSearchLoader class as an attribute that determines
    // which moviedbuitls method to call
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);

            // TODO do search depending on which fab button was pressed
            int searchFilm = intent.getIntExtra(MediaKeys.FAB_PRESSED, 1);

            switch (searchFilm){
                case MainActivity.FILM_FAB:
                    // TODO do film search
                    break;
                case MainActivity.BOOK_FAB:
                    // TODO do book search
                    break;
                case MainActivity.GAME_FAB:
                    // TODO do game search
                    break;
            }

            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
    }
}
