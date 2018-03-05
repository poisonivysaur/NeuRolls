package com.werelit.neurolls.neurolls;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.com.mauker.materialsearchview.MaterialSearchView;

public class SearchableActivity extends AppCompatActivity{



    private MaterialSearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_test);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
    }
}
