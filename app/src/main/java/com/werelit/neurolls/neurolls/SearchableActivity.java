package com.werelit.neurolls.neurolls;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import br.com.mauker.materialsearchview.MaterialSearchView;

public class SearchableActivity extends AppCompatActivity{

    /**
     * To open the search view, simply call the searchView.openSearch() method.
     * To close the search view, call the searchView.closeSearch() method.
     * You can check if the view is open by using the searchView.isOpen() method.
     * to get the query anytime by using the searchView.getCurrentQuery() method.
     *
     * You can provide search suggestions by using the following methods:
     * addSuggestions(String[] suggestions)
     * addSuggestions(ArrayList<String> suggestions)
     *
     * It's also possible to add a single suggestion using the following method:
     *
     * addSuggestion(String suggestion)
     *
     *
     *
     * To remove all the search suggestions use:
     *
     * clearSuggestions()
     *
     *
     * And to remove a single suggestion, use the following method:
     *
     * removeSuggestion(String suggestion)
     *
     *
     * The search history is automatically handled by the view, and it can be cleared by using:
     *
     * clearHistory()
     *
     *
     * You can also remove both by using the method below:
     *
     * clearAll()
     */

    private MaterialSearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_test);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        //searchView.openSearch();
        Toast.makeText(this, "" + searchView.isOpen(), Toast.LENGTH_SHORT).show();

        // If you want to submit the query from the selected suggestion
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);

                searchView.setQuery(suggestion, true);
            }
        });

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
    }

    // To close the search view using the back button

    @Override
    public void onBackPressed() {
        if (searchView.isOpen()) {
            // Close the search on the back button press.
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
