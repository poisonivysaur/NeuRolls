package com.werelit.neurolls.neurolls;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.werelit.neurolls.neurolls.network.ConnectMovieDB;
import com.werelit.neurolls.neurolls.network.JsonConverter;
import com.werelit.neurolls.neurolls.network.MediaTaskLoader;

import com.werelit.neurolls.neurolls.data.MediaContract.FilmEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.BookEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.GameEntry;

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

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    private View loadingIndicator;

    private int searchType = 1;

    private boolean hasSearchedFilmAlready;

    private String incompleteFilmID = "0";

    private Bundle bundle;

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

        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        hasSearchedFilmAlready = false;

        searchType = getIntent().getExtras().getInt(MediaKeys.FAB_PRESSED, 1);
        switch (searchType){
            case Media.CATEGORY_FILMS:
                setTitle(R.string.search_films);
                searchView.setHint("Search Films");
                break;
            case Media.CATEGORY_BOOKS:
                setTitle(R.string.search_books);
                searchView.setHint("Search Books");
                break;
            case Media.CATEGORY_GAMES:
                setTitle(R.string.search_games);
                searchView.setHint("Search Games");
                break;
            default:
                setTitle(R.string.search_media);
                searchView.setHint("Search NeuRolls");
                break;
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        //Toast.makeText(this,"CALLED ON CREATE LOADER!",Toast.LENGTH_SHORT).show();
        mediaTaskLoader = new MediaTaskLoader(this, args.getString(MediaKeys.SEARCH_QUERY));
        mediaTaskLoader.setMediaCategory(searchType);
        mediaTaskLoader.setHasSearchedFilmAlready(hasSearchedFilmAlready);
        mediaTaskLoader.setFilmdID(incompleteFilmID);
        return mediaTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        if(hasSearchedFilmAlready){
            Film completeFilm = JsonConverter.revisedSpecificFilm(data);
            //Toast.makeText(this, "" + completeFilm.getMediaID(),Toast.LENGTH_SHORT).show();
            retrieveFilmDetails(completeFilm);
        }
        else {
            // Clear the adapter of previous data
            mediaList.clear();

            ArrayList<Media> m = new ArrayList<>();
            switch (searchType) {
                case Media.CATEGORY_FILMS:
                    m = JsonConverter.revisedSearchFilms(data);
                    break;
                case Media.CATEGORY_BOOKS:
                    m = JsonConverter.revisedBookSearchResult(data);
                    break;
                case Media.CATEGORY_GAMES:
                    break;
                default:
                    Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            }

            for (Media a : m) {
                mediaList.add(a);
            }

            updateSearchResultsUI();
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
                loadingIndicator.setVisibility(View.VISIBLE);
                mediaList.clear();
                if(searchType > 0) {    // if not internal search
                    // Get a reference to the ConnectivityManager to check state of network connectivity
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);

                    // Get details on the currently active default data network
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    // If there is a network connection, fetch data
                    if (networkInfo != null && networkInfo.isConnected()) {
                        if (searchType == Media.CATEGORY_GAMES) {
                            setupGameSearch(query);
                        } else {
                            searchMedia(query);
                        }
                    } else {
                        // Otherwise, display error
                        // First, hide loading indicator so error message will be visible
                        loadingIndicator.setVisibility(View.GONE);
                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    loadingIndicator.setVisibility(View.GONE);
                    searchNeuRolls(query);
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
                if(mediaList.size() > 0) loadingIndicator.setVisibility(View.GONE);
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
                // Toast.makeText(SearchMediaActivity.this, mediaList.get(position).getmMediaName() + " is selected!", Toast.LENGTH_SHORT).show();
                prepareData(position);
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

        Intent intent = new Intent(this, ViewMediaDetailsActivity.class);
        Media media = mediaList.get(position);

        // Make a bundle containing the current media details
        bundle = new Bundle();
        prepareMediaDetails(media);

        // View the details depending what category the media is
        switch (searchType){
            case Media.CATEGORY_FILMS:
                hasSearchedFilmAlready = true;
                incompleteFilmID = mediaList.get(position).getMediaID();
                Bundle queryBundle = new Bundle();
                queryBundle.putString(MediaKeys.SEARCH_QUERY, "test query");
                getSupportLoaderManager().restartLoader(0, queryBundle, SearchMediaActivity.this);
                break;

            case Media.CATEGORY_BOOKS:
                prepareBookDetails((Book) media);
                break;

            case Media.CATEGORY_GAMES:
                prepareGameDetails((Game) media);
                break;

            default:
                if(media instanceof Film){
                    prepareFilmDetails((Film) media);
                } else if(media instanceof Book){
                    prepareBookDetails((Book) media);
                } else {
                    prepareGameDetails((Game) media);
                }
        }
        if(searchType != Media.CATEGORY_FILMS){
            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();
        }
    }

    private void retrieveFilmDetails(Film film){
        Intent intent = new Intent(this, ViewMediaDetailsActivity.class);
        // Make a bundle containing the current media details
        bundle = new Bundle();
        prepareMediaDetails(film);
        prepareFilmDetails(film);

        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    private void prepareMediaDetails(Media media){
        boolean isForAdding = (searchType != MainActivity.SEARCH_NEUROLLS);
        //bundle.putBoolean(MediaKeys.MEDIA_ARCHIVED, isArchived); // change this later to mediaList.get(i).isArchived()
        bundle.putBoolean(MediaKeys.ADDING_NEW_MEDIA, isForAdding);
        bundle.putString(MediaKeys.MEDIA_ID_KEY, media.getMediaID());
        bundle.putString(MediaKeys.MEDIA_NAME_KEY, media.getmMediaName());
        bundle.putString(MediaKeys.MEDIA_GENRE_KEY, media.getmMediaGenre());
        bundle.putString(MediaKeys.MEDIA_YEAR_KEY, media.getmMediaYear());
        bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, getCategoryCode(media));
    }

    private void prepareFilmDetails(Film film){
        bundle.putString(MediaKeys.FILM_DIRECTOR_KEY, film.getDirector());
        bundle.putInt(MediaKeys.FILM_DURATION_KEY, film.getDuration());
        bundle.putString(MediaKeys.FILM_PRODUCTION_KEY, film.getProduction());
        bundle.putString(MediaKeys.FILM_SYNOPSIS_KEY, film.getSynopsis());
    }

    private void prepareBookDetails(Book book){
        bundle.putString(MediaKeys.BOOK_AUTHOR_KEY, book.getAuthor());
        bundle.putInt(MediaKeys.BOOK_PAGES_KEY, book.getPages());
        bundle.putString(MediaKeys.BOOK_PUBLISHER_KEY, book.getPublisher());
        bundle.putString(MediaKeys.BOOK_DESCRIPTION_KEY, book.getDescription());
    }

    private void prepareGameDetails(Game game){
        bundle.putString(MediaKeys.GAME_PLATFORM_KEY, game.getPlatform());
        bundle.putString(MediaKeys.GAME_PUBLISHER_KEY, game.getPublisher());
        bundle.putString(MediaKeys.GAME_SERIES_KEY, game.getSeries());
        bundle.putString(MediaKeys.GAME_STORYLINE_KEY, game.getStoryline());
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

    private void setupGameSearch(String query){
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
                // Hide loading indicator because the data has been loaded
                loadingIndicator.setVisibility(View.GONE);

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

    private void searchNeuRolls(String query){
        getFilms(query);
        getBooks(query);
        getGames(query);
        updateSearchResultsUI();

//        Bundle queryBundle = new Bundle();
//        queryBundle.putString(MediaKeys.SEARCH_QUERY, query);
//        getSupportLoaderManager().restartLoader(0, queryBundle, SearchMediaActivity.this);
    }

    private void getFilms(String query){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                //FilmEntry._ID,
                FilmEntry.COLUMN_FILM_ID,
                FilmEntry.COLUMN_FILM_NAME,
                FilmEntry.COLUMN_FILM_GENRE,
                FilmEntry.COLUMN_FILM_YEAR_RELEASED,
                FilmEntry.COLUMN_FILM_IMG_DIR,

                FilmEntry.COLUMN_FILM_DIRECTOR,
                FilmEntry.COLUMN_FILM_DURATION,
                FilmEntry.COLUMN_FILM_PRODUCTION,
                FilmEntry.COLUMN_FILM_SYNOPSIS,

                FilmEntry.COLUMN_FILM_DATE_TO_WATCH,
                FilmEntry.COLUMN_FILM_NOTIF_SETTINGS,
                FilmEntry.COLUMN_FILM_WATCHED,
                FilmEntry.COLUMN_FILM_ARCHIVED };

        String selection = FilmEntry.COLUMN_FILM_NAME + " LIKE ?";
        String[] selectionArgs = new String[] { "%" + query + "%"};

        Cursor cursor = getContentResolver().query(
                FilmEntry.CONTENT_URI,          // The content URI of the films table
                projection,                     // The columns to return for each row
                selection,                      // The columns for the WHERE clause; selection criteria
                selectionArgs,                  // The values for the WHERE clause
                FilmEntry.COLUMN_LAST_UPDATE+" DESC");                // The sort order for the returned rows
        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_ID);
            int nameColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_NAME);
            int genreColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_GENRE);
            int yearColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_YEAR_RELEASED);
            int imageColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_IMG_DIR);

            int directorColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_DIRECTOR);
            int durationColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_DURATION);
            int prodColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_PRODUCTION);
            int synopsisColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_SYNOPSIS);

            int dateColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_DATE_TO_WATCH);
            int notifColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_NOTIF_SETTINGS);
            int watchedColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_WATCHED);
            int archivedColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_ARCHIVED);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentID = cursor.getString(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentGenre = cursor.getString(genreColumnIndex);
                String currentYear = cursor.getString(yearColumnIndex);
                String currentImage = cursor.getString(imageColumnIndex);

                String currentDirector = cursor.getString(directorColumnIndex);
                int currentDuration = cursor.getInt(durationColumnIndex);
                String currentProd = cursor.getString(prodColumnIndex);
                String currentSynopsis = cursor.getString(synopsisColumnIndex);

                String currentDate = cursor.getString(dateColumnIndex);
                String currentNotif = cursor.getString(notifColumnIndex);
                String currentWatched = cursor.getString(watchedColumnIndex);
                String currentArchived = cursor.getString(archivedColumnIndex);

                Film film = new Film(currentID, currentName, currentGenre, currentYear, currentDirector, currentDuration, currentProd, currentSynopsis);
                int n = Integer.parseInt(currentArchived);
                film.setArchived((n == 1)? true : false);
                film.setNotifSettings(currentNotif);
                //Log.wtf(LOG_TAG, "CURRENT ARCHIVED: " + n);
                mediaList.add(0, film);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void getBooks(String query){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                //BookEntry._ID,
                BookEntry.COLUMN_BOOK_ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_GENRE,
                BookEntry.COLUMN_BOOK_YEAR_PUBLISHED,
                BookEntry.COLUMN_BOOK_IMG_DIR,

                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_PAGES,
                BookEntry.COLUMN_BOOK_PUBLISHER,
                BookEntry.COLUMN_BOOK_DESCRIPTION,

                BookEntry.COLUMN_BOOK_DATE_TO_READ,
                BookEntry.COLUMN_BOOK_NOTIF_SETTINGS,
                BookEntry.COLUMN_BOOK_READ,
                BookEntry.COLUMN_BOOK_ARCHIVED };

        String selection = BookEntry.COLUMN_BOOK_NAME + " LIKE ?";
        String[] selectionArgs = new String[] { "%" + query + "%"};

        Cursor cursor = getContentResolver().query(
                BookEntry.CONTENT_URI,          // The content URI of the films table
                projection,                     // The columns to return for each row
                selection,                      // The columns for the WHERE clause; selection criteria
                selectionArgs,                  // The values for the WHERE clause
                BookEntry.COLUMN_LAST_UPDATE+" DESC");                // The sort order for the returned rows
        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ID);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int genreColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_GENRE);
            int yearColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_YEAR_PUBLISHED);
            int imageColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_IMG_DIR);

            int directorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_AUTHOR);
            int durationColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PAGES);
            int prodColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PUBLISHER);
            int synopsisColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_DESCRIPTION);

            int dateColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_DATE_TO_READ);
            int notifColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NOTIF_SETTINGS);
            int watchedColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_READ);
            int archivedColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ARCHIVED);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentID = cursor.getString(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentGenre = cursor.getString(genreColumnIndex);
                String currentYear = cursor.getString(yearColumnIndex);
                String currentImage = cursor.getString(imageColumnIndex);

                String currentDirector = cursor.getString(directorColumnIndex);
                int currentDuration = cursor.getInt(durationColumnIndex);
                String currentProd = cursor.getString(prodColumnIndex);
                String currentSynopsis = cursor.getString(synopsisColumnIndex);

                String currentDate = cursor.getString(dateColumnIndex);
                String currentNotif = cursor.getString(notifColumnIndex);
                String currentWatched = cursor.getString(watchedColumnIndex);
                String currentArchived = cursor.getString(archivedColumnIndex);

                Book book = new Book(currentID, currentName, currentGenre, currentYear, currentDirector, currentDuration, currentProd, currentSynopsis);
                int n = Integer.parseInt(currentArchived);
                book.setArchived((n == 1)? true : false);

                mediaList.add(0, book);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void getGames(String query){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                //GameEntry._ID,
                GameEntry.COLUMN_GAME_ID,
                GameEntry.COLUMN_GAME_NAME,
                GameEntry.COLUMN_GAME_GENRE,
                GameEntry.COLUMN_GAME_YEAR_RELEASED,
                GameEntry.COLUMN_GAME_IMG_DIR,

                GameEntry.COLUMN_GAME_PLATFORM,
                GameEntry.COLUMN_GAME_PUBLISHER,
                GameEntry.COLUMN_GAME_SERIES,
                GameEntry.COLUMN_GAME_STORYLINE,

                GameEntry.COLUMN_GAME_DATE_TO_PLAY,
                GameEntry.COLUMN_GAME_NOTIF_SETTINGS,
                GameEntry.COLUMN_GAME_PLAYED,
                GameEntry.COLUMN_GAME_ARCHIVED };

        String selection = GameEntry.COLUMN_GAME_NAME + " LIKE ?";
        String[] selectionArgs = new String[] { "%" + query + "%"};

        Cursor cursor = getContentResolver().query(
                GameEntry.CONTENT_URI,          // The content URI of the films table
                projection,                     // The columns to return for each row
                selection,                      // The columns for the WHERE clause; selection criteria
                selectionArgs,                  // The values for the WHERE clause
                GameEntry.COLUMN_LAST_UPDATE+" DESC");                // The sort order for the returned rows
        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_ID);
            int nameColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_NAME);
            int genreColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_GENRE);
            int yearColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_YEAR_RELEASED);
            int imageColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_IMG_DIR);

            int directorColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_PLATFORM);
            int durationColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_PUBLISHER);
            int prodColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_SERIES);
            int synopsisColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_STORYLINE);

            int dateColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_DATE_TO_PLAY);
            int notifColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_NOTIF_SETTINGS);
            int watchedColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_PLAYED);
            int archivedColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_ARCHIVED);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentID = cursor.getString(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentGenre = cursor.getString(genreColumnIndex);
                String currentYear = cursor.getString(yearColumnIndex);
                String currentImage = cursor.getString(imageColumnIndex);

                String currentDirector = cursor.getString(directorColumnIndex);
                String currentDuration = cursor.getString(durationColumnIndex);
                String currentProd = cursor.getString(prodColumnIndex);
                String currentSynopsis = cursor.getString(synopsisColumnIndex);

                String currentDate = cursor.getString(dateColumnIndex);
                String currentNotif = cursor.getString(notifColumnIndex);
                String currentWatched = cursor.getString(watchedColumnIndex);
                String currentArchived = cursor.getString(archivedColumnIndex);

                Game game = new Game(currentID, currentName, currentGenre, currentYear, currentDirector, currentDuration, currentProd, currentSynopsis);
                int n = Integer.parseInt(currentArchived);
                game.setArchived((n == 1)? true : false);

                mediaList.add(0, game);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void updateSearchResultsUI(){
        mediaAdapter.notifyDataSetChanged();
        if (mediaList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            // Set empty state text to display "No matching results :("
            mEmptyStateTextView.setText(R.string.no_matching_results);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    private int getCategoryCode(Media media){
        if(media instanceof Film){
            return CategoryAdapter.CATEGORY_FILMS;
        } else if(media instanceof Book){
            return CategoryAdapter.CATEGORY_BOOKS;
        } else {
            return CategoryAdapter.CATEGORY_GAMES;
        }
    }
}
