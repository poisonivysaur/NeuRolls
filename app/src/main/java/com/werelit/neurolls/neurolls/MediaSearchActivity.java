package com.werelit.neurolls.neurolls;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import br.com.mauker.materialsearchview.MaterialSearchView;
/**
 *
 * TODO: DELETE THIS CLASS IF WILL NO LONGER BE USED
 *
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

public class MediaSearchActivity extends AppCompatActivity{
    private MaterialSearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_test);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        //Toast.makeText(this, "" + searchView.isOpen(), Toast.LENGTH_SHORT).show();

        // If you want to submit the query from the selected suggestion
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);

                searchView.setQuery(suggestion, true);
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchView.openSearch();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here. It'll
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                // Open the search view on the menu item click.

                searchView.openSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
