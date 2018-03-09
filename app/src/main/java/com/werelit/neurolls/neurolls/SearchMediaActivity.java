package com.werelit.neurolls.neurolls;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.igdb.api_android_java.callback.onSuccessCallback;
import com.igdb.api_android_java.model.APIWrapper;
import com.igdb.api_android_java.model.Parameters;
import com.werelit.neurolls.neurolls.network.ConnectGameDB;
import com.werelit.neurolls.neurolls.model.Book;
import com.werelit.neurolls.neurolls.model.Film;
import com.werelit.neurolls.neurolls.model.Game;
import com.werelit.neurolls.neurolls.model.Media;
import com.werelit.neurolls.neurolls.network.JsonConverter;

import org.json.JSONArray;

import java.util.ArrayList;

import br.com.mauker.materialsearchview.MaterialSearchView;

public class SearchMediaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MaterialSearchView searchView;
    private ArrayList<Media> mediaList;
    private RecyclerView recyclerView;
    private MediaAdapter mediaAdapter;
    private MediaTaskLoader mediaTaskLoader;
    /** TextView that is displayed when the list is empty */    private TextView mEmptyStateTextView;
    private int searchType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_media);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupSearchView();
        setupRecyclerView();

        // set visibility of the empty view to be GONE initially
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        searchType = getIntent().getExtras().getInt(MediaKeys.FAB_PRESSED, 1);
    }

    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, Bundle args) {
        mediaTaskLoader = new MediaTaskLoader(this, args.getString(MediaKeys.SEARCH_QUERY));
        mediaTaskLoader.setMediaCategory(searchType);
        return mediaTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mediaList.clear();
        ArrayList<Media> m = new ArrayList<>();
        switch (searchType){
            case Media.CATEGORY_FILMS:
                m = JsonConverter.revisedSearchFilms(data);
                break;
            case Media.CATEGORY_BOOKS:
                m = JsonConverter.revisedBookSearchResult(data);
                break;
        }

        for(Media a : m) {
            mediaList.add(a);
        }

        mediaAdapter.notifyDataSetChanged();
        if(mediaList.size() == 0 ){
            recyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No matching results :(");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
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

                if(searchType == Media.CATEGORY_GAMES){
                    setupGameSearch(query);
                }
                else {
                    searchMedia(query);
                }
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
                recyclerView.setVisibility(View.INVISIBLE);
                mEmptyStateTextView.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                // Do something once the view is closed.
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);
                //Toast.makeText(SearchMediaActivity.this, "ehem ehem", Toast.LENGTH_SHORT).show();
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
        this.mediaList = new ArrayList<>();
        this.mediaAdapter = new MediaAdapter(mediaList);

        this.recyclerView = findViewById(R.id.search_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mediaAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(SearchMediaActivity.this, mediaList.get(position).getmMediaName() + " is selected!", Toast.LENGTH_SHORT).show();

                // TODO call api again, put it in bundle (prepareData method)
/*
                Bundle queryBundle = new Bundle();
                queryBundle.putString(MediaKeys.SEARCH_QUERY, mediaList.get(position).getmMediaName()); // get name or ID better
                getSupportLoaderManager().restartLoader(0, queryBundle, SearchMediaActivity.this);
                //prepareData(position);*/
            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getApplicationContext(), "You Long pressed me!", Toast.LENGTH_SHORT).show();
            }
        }));

        if(getSupportLoaderManager().getLoader(0) != null){
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    private void prepareData(int position){

        // Make a bundle containing the current media details
        Bundle bundle = new Bundle();

        //bundle.putBoolean(MediaKeys.MEDIA_ARCHIVED, isArchived); // change this later to mediaList.get(i).isArchived()
        bundle.putBoolean(MediaKeys.ADDING_NEW_MEDIA, true);
        bundle.putString(MediaKeys.MEDIA_NAME_KEY, mediaList.get(position).getmMediaName());
        bundle.putString(MediaKeys.MEDIA_GENRE_KEY, mediaList.get(position).getmMediaGenre());
        bundle.putString(MediaKeys.MEDIA_YEAR_KEY, mediaList.get(position).getmMediaYear());

        // View the details depending what category the media is
        Media media = mediaList.get(position);
        Intent intent = new Intent(this, ViewMediaDetailsActivity.class);

        // TODO instead of checking the instanceof, use searchType to determine which view to get
        switch (searchType){
            case Media.CATEGORY_FILMS:
                bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_FILMS);
                bundle.putInt(MediaKeys.FILM_DURATION_KEY, ((Film)mediaList.get(position)).getDuration());
                bundle.putString(MediaKeys.FILM_DIRECTOR_KEY, ((Film)mediaList.get(position)).getDirector());
                bundle.putString(MediaKeys.FILM_PRODUCTION_KEY, ((Film)mediaList.get(position)).getProduction());
                bundle.putString(MediaKeys.FILM_SYNOPSIS_KEY, ((Film)mediaList.get(position)).getSynopsis());
                break;

            case Media.CATEGORY_BOOKS:
                bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_BOOKS);
                bundle.putString(MediaKeys.BOOK_AUTHOR_KEY, ((Book)mediaList.get(position)).getAuthor());
                bundle.putString(MediaKeys.BOOK_PUBLISHER_KEY, ((Book)mediaList.get(position)).getPublisher());
                bundle.putString(MediaKeys.BOOK_DESCRIPTION_KEY, ((Book)mediaList.get(position)).getDescription());
                break;

            case Media.CATEGORY_GAMES:
                bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_GAMES);
                bundle.putString(MediaKeys.GAME_PLATFORM_KEY, ((Game)mediaList.get(position)).getPlatform());
                bundle.putString(MediaKeys.GAME_PUBLISHER_KEY, ((Game)mediaList.get(position)).getPublisher());
                bundle.putString(MediaKeys.GAME_SERIES_KEY, ((Game)mediaList.get(position)).getSeries());
                bundle.putString(MediaKeys.GAME_STORYLINE_KEY, ((Game)mediaList.get(position)).getStoryline());
                break;
        }

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void searchMedia(String query){
        // for films and books
        switch (searchType){
            case Media.CATEGORY_FILMS:
                //query = query + "@film";
                break;
            case Media.CATEGORY_BOOKS:
                query = query + "@book";
                break;
        }
        Bundle queryBundle = new Bundle();
        queryBundle.putString(MediaKeys.SEARCH_QUERY, query);
        getSupportLoaderManager().restartLoader(0, queryBundle, SearchMediaActivity.this);
        //intent.putExtra(MediaKeys.FAB_PRESSED, intent.getIntExtra(MediaKeys.FAB_PRESSED, 1));
    }

    public void setupGameSearch(String query){
        APIWrapper wrapper = new APIWrapper(SearchMediaActivity.this, ConnectGameDB.USER_KEY);
        Parameters params = new Parameters()
                .addSearch(query)
                .addFields("name,summary,collection,cover,release_dates,publishers,developers,platforms,genres")
                .addExpand("game,collection,developers,publishers,platforms,genres")
                .addFilter("[developers][exists]=true")
                .addFilter("[publishers][exists]=true")
                .addFilter("[category][eq]=0")
                .addFilter("[platforms][any]=6,48,9,38,11,12,37,20,130,5,41");

        wrapper.search(APIWrapper.Endpoint.GAMES, params, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                mediaList.clear();
                for(Media m : JsonConverter.revisedGetGameSearchResult(jsonArray.toString())){
                    mediaList.add(m);
                }
                mediaAdapter.notifyDataSetChanged();
                if(mediaList.size() == 0 ){
                    recyclerView.setVisibility(View.GONE);
                    mEmptyStateTextView.setText("No matching results :(");
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(VolleyError volleyError) {}
        });
    }
}
