package com.werelit.neurolls.neurolls;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.werelit.neurolls.neurolls.model.Media;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.mauker.materialsearchview.MaterialSearchView;

public class SearchMediaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MaterialSearchView searchView;
    private ArrayList<Media> movieList;
    private RecyclerView recyclerView;
    private MediaAdapter mediaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_test_2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupSearchView();
        setupRecyclerView();
    }

    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, Bundle args) {
        return new FilmSearchLoader(this, args.getString("queryString"));
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        movieList.clear();
        try{
            JSONObject baseJsonResponse = new JSONObject(data);
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
            for(int i = 0; i < resultsArray.length(); i++){
                JSONObject currentObj = resultsArray.getJSONObject(i);

                String title = currentObj.getString("title");
                String releaseDate = currentObj.getString("release_date");
                int releaseYear = 0;
                if(!releaseDate.equals(""))
                    releaseYear = Integer.parseInt(releaseDate.substring(0, 4));
                String language = currentObj.getString("original_language");
                movieList.add(new Media(title, language, releaseYear));
                    /*
                    System.out.println("Movie Index: " + i);
                    System.out.println("\tTitle: " + title);
                    System.out.println("\tRelease Date: " + releaseDate);
                    System.out.println("\tOverview: " + overview);
                    */
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mediaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {}

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchView.clearSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.activityResumed();
        //String[] arr = getResources().getStringArray(R.array.suggestions);

        //searchView.addSuggestions(arr);
    }

    private void clearHistory() {
        searchView.clearHistory();
    }

    private void clearSuggestions() {
        searchView.clearSuggestions();
    }

    private void clearAll() {
        searchView.clearAll();
    }

    private void setupSearchView(){
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO put in intent extras/ bundles here to see if it was from film, books, or games
                Intent intent = getIntent();
                Bundle queryBundle = new Bundle();
                // to know where the search was from based on FAB pressed
                queryBundle.putInt(MediaKeys.FAB_PRESSED, intent.getIntExtra(MediaKeys.FAB_PRESSED, 1));
                queryBundle.putString(MediaKeys.SEARCH_QUERY, query);
                getSupportLoaderManager().restartLoader(0, queryBundle, SearchMediaActivity.this);
                //intent.putExtra(MediaKeys.FAB_PRESSED, intent.getIntExtra(MediaKeys.FAB_PRESSED, 1));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO do search suggestions
                return false;
            }
        });

        searchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewOpened() {
                // Do something once the view is open.
            }

            @Override
            public void onSearchViewClosed() {
                // Do something once the view is closed.
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);

                searchView.setQuery(suggestion, false);
            }
        });

//        searchView.setTintAlpha(200);
        searchView.adjustTintAlpha(0.8f);

        final Context context = this;
        searchView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Long clicked position: " + i, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        searchView.setOnVoiceClickedListener(new MaterialSearchView.OnVoiceClickedListener() {
            @Override
            public void onVoiceClicked() {
                Toast.makeText(context, "Voice clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(){
        this.movieList = new ArrayList<>();
        this.mediaAdapter = new MediaAdapter(movieList);

        this.recyclerView = findViewById(R.id.search_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mediaAdapter);
        //prepareMovieData();

        if(getSupportLoaderManager().getLoader(0) != null){
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }
}
