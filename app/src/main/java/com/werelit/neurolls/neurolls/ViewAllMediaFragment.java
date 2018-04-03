package com.werelit.neurolls.neurolls;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.werelit.neurolls.neurolls.data.MediaContract;
import com.werelit.neurolls.neurolls.data.NeurollsDbHelper;
import com.werelit.neurolls.neurolls.model.Book;
import com.werelit.neurolls.neurolls.model.Film;
import com.werelit.neurolls.neurolls.model.Game;
import com.werelit.neurolls.neurolls.model.Media;

import com.werelit.neurolls.neurolls.data.MediaContract.FilmEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.BookEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.GameEntry;
import com.werelit.neurolls.neurolls.network.BitmapConverter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ViewAllMediaFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static final String LOG_TAG = ViewAllMediaFragment.class.getSimpleName();
    /** The list containing Media objects */                    private List<Media> entertainments;
    /** The recycler view containing the Media items */         private RecyclerView mRecyclerView;
    /** The adapter used for the recycler view */               private MediaAdapter mAdapter;
    /** The layout manager for the recycler view */             private RecyclerView.LayoutManager mLayoutManager;
    /** The layout for the snackbar with undo delete */         private ConstraintLayout constraintLayout;
    /** TextView that is displayed when the list is empty */    private View mEmptyStateTextView;

    private int mediaCategory = -1;
    private boolean isArchived = false;

    private View rootView;

    public ViewAllMediaFragment(){

    }

    public ViewAllMediaFragment(int mediaCategory){
        this.mediaCategory = mediaCategory;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
        if(isArchived){
            // query here later on for isArchived attribute from the db for the entertainments array list
            // TODO enetertainments = dbHelper.query ...
            mAdapter = new MediaAdapter(entertainments, mediaCategory, isArchived);
        }
        else {
            mAdapter = new MediaAdapter(entertainments, mediaCategory);
        }
        if(mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
            prepareMedias();
            shouldDisplayEmptyView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareMedias();
        mAdapter.notifyDataSetChanged();
        Log.wtf(LOG_TAG, "VIEW ALL MEDIA FRAGMENT ON RESUME CALLED");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(
                R.layout.view_all_media, container, false);

        this.rootView = rootView;

        // use a constraint layout for the delete snackbar with UNDO
        constraintLayout = rootView.findViewById(R.id.constraint_layout);

        // set visibility of the empty view to be GONE initially
        mEmptyStateTextView = (View) rootView.findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        // add Media items into the Medias list
        entertainments = new ArrayList<>(); // always put this before setting up recycler view

        // setup the recycler view adapter, layout, etc.
        prepareRecyclerView(rootView);

        prepareMedias();
        mAdapter.notifyDataSetChanged();
        shouldDisplayEmptyView();

        return rootView;
    }

    /**
     * This method setups the recycler view
     * - setting the adapter
     * - setting the layout of the recycler view
     * - adding an item touch listener, etc.
     */
    private void prepareRecyclerView(final View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator()); // does not change anything
        mRecyclerView.setLayoutManager(mLayoutManager);

        // This draws a line separator for each row, but card views are used so no need for this
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // specify an adapter
        if(isArchived){
            mAdapter = new MediaAdapter(entertainments, mediaCategory, isArchived);
        }
        else {
            mAdapter = new MediaAdapter(entertainments, mediaCategory);
        }

        mRecyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);


        // add a click listener to go to the restaurant details for editing an existing item
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(rootView.getContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(rootView.getContext(), media.getmMediaName() + " is selected!", Toast.LENGTH_SHORT).show();

                prepareData(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getApplicationContext(), "You Long pressed me!", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void prepareData(int position){

        Media media = entertainments.get(position);

        // Make a bundle containing the current media details
        Bundle bundle = new Bundle();

        bundle.putBoolean(MediaKeys.ADDING_NEW_MEDIA, false);
        bundle.putString(MediaKeys.MEDIA_ID_KEY, media.getMediaID());
        bundle.putString(MediaKeys.MEDIA_NAME_KEY, media.getmMediaName());
        bundle.putString(MediaKeys.MEDIA_GENRE_KEY, media.getmMediaGenre());
        bundle.putString(MediaKeys.MEDIA_YEAR_KEY, media.getmMediaYear());
        bundle.putBoolean(MediaKeys.MEDIA_ARCHIVED, media.isArchived());
        if(media.getThumbnailBmp() != null)
            bundle.putString(MediaKeys.MEDIA_IMAGE_KEY, BitmapConverter.bitmapToString(media.getThumbnailBmp()));
        //bundle.putString(MediaKeys.NOTIFICATION_ID, media.getNotifSettings());/////////////////////////////////////////////////////////////////////

        // TODO add image directory to bundle

        // View the details depending what category the media is
        Intent intent = new Intent(rootView.getContext(), ViewMediaDetailsActivity.class);

        if(media instanceof Film){
            bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_FILMS);
            bundle.putInt(MediaKeys.FILM_DURATION_KEY, ((Film)media).getDuration());
            bundle.putString(MediaKeys.FILM_DIRECTOR_KEY, ((Film)media).getDirector());
            bundle.putString(MediaKeys.FILM_PRODUCTION_KEY, ((Film)media).getProduction());
            bundle.putString(MediaKeys.FILM_SYNOPSIS_KEY, ((Film)media).getSynopsis());
            bundle.putString(FilmEntry.COLUMN_FILM_DATE_TO_WATCH, ((Film)media).getDateToWatch());
            bundle.putString(FilmEntry.COLUMN_FILM_NOTIF_TIME, ((Film)media).getTimeToWatch());
            bundle.putString(FilmEntry.COLUMN_FILM_NOTIF_SETTINGS, ((Film)media).getNotifSettings());
        }
        else if(media instanceof Book){
            bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_BOOKS);
            bundle.putString(MediaKeys.BOOK_AUTHOR_KEY, ((Book)media).getAuthor());
            bundle.putInt(MediaKeys.BOOK_PAGES_KEY, ((Book)media).getPages());
            bundle.putString(MediaKeys.BOOK_PUBLISHER_KEY, ((Book)media).getPublisher());
            bundle.putString(MediaKeys.BOOK_DESCRIPTION_KEY, ((Book)media).getDescription());
            bundle.putString(BookEntry.COLUMN_BOOK_DATE_TO_READ, ((Book)media).getDateToRead());
            bundle.putString(BookEntry.COLUMN_BOOK_NOTIF_TIME, ((Book)media).getTimeToRead());
            bundle.putString(BookEntry.COLUMN_BOOK_NOTIF_SETTINGS, ((Book)media).getNotifSettings());
        }
        else if(media instanceof Game){
            bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_GAMES);
            bundle.putString(MediaKeys.GAME_PLATFORM_KEY, ((Game)media).getPlatform());
            bundle.putString(MediaKeys.GAME_PUBLISHER_KEY, ((Game)media).getPublisher());
            bundle.putString(MediaKeys.GAME_SERIES_KEY, ((Game)media).getSeries());
            bundle.putString(MediaKeys.GAME_STORYLINE_KEY, ((Game)media).getStoryline());
            bundle.putString(GameEntry.COLUMN_GAME_DATE_TO_PLAY, ((Game)media).getDateToPlay());
            bundle.putString(GameEntry.COLUMN_GAME_NOTIF_TIME, ((Game)media).getTimeToPlay());
            bundle.putString(GameEntry.COLUMN_GAME_NOTIF_SETTINGS, ((Game)media).getNotifSettings());
        }

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void prepareMedias() {

        entertainments.clear();
//        Film dummyFilm = new Film("ID#1", "Sherlock Holmes", "Thriller/Action", "2009",
//                "Guy Ritchie", 130, "Silver Pictures, Wigram Productions, Village Roadshow Pictures",
//                "When a string of brutal murders terrorizes London, it doesn't take long for legendary detective Sherlock Holmes (Robert Downey Jr.) and his crime-solving partner, Dr. Watson (Jude Law), to find the killer, Lord Blackwood (Mark Strong). A devotee of the dark arts, Blackwood has a bigger scheme in mind, and his execution plays right into his plans. The game is afoot when Blackwood seems to rise from the grave, plunging Holmes and Watson into the world of the occult and strange technologies.");
//        Book dummyBook = new Book("ID#1", "Charlotte's Web", "Children's literature", "1952", "E. B. White", 192, "Harper & Brothers", "Charlotte's Web is a children's novel by American author E. B. White and illustrated by Garth Williams; it was published on October 15, 1952, by Harper & Brothers.");
//        Game dummyGame = new Game("ID#1", "Shadow the Hedgehog", "Platformer, action-adventure, third-person shooter", "2005",
//                "Nintendo GameCube, PlayStation 2, Xbox", "Sega", "Sonic the Hedgehog", "Shadow the Hedgehog is a platform video game developed by Sega Studio USA, the former United States division of Sega's Sonic Team, and published by Sega.");
//        dummyFilm.setArchived(true);
//        dummyBook.setArchived(true);
//        dummyGame.setArchived(true);
        switch (mediaCategory){
            case 0:
                getFilms(isArchived? 1 : 0);
                getBooks(isArchived? 1 : 0);
                getGames(isArchived? 1 : 0);
//                entertainments.add(dummyFilm);
//                entertainments.add(dummyBook);
//                entertainments.add(dummyGame);
                break;
            case 1:
                getFilms(isArchived? 1 : 0);
//                entertainments.add(dummyFilm);
                break;
            case 2:
                getBooks(isArchived? 1 : 0);
//                entertainments.add(dummyBook);
                break;
            case 3:
                getGames(isArchived? 1 : 0);
//                entertainments.add(dummyGame);
                break;
        }
        shouldDisplayEmptyView();
    }

    private void getFilms(int isArchived){
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
                FilmEntry.COLUMN_FILM_NOTIF_TIME,
                FilmEntry.COLUMN_FILM_WATCHED,
                FilmEntry.COLUMN_FILM_ARCHIVED };

        String selection = FilmEntry.COLUMN_FILM_ARCHIVED + "=?";
        String[] selectionArgs = new String[] { String.valueOf(isArchived) };

        Cursor cursor = rootView.getContext().getContentResolver().query(
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
            int timeColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_NOTIF_TIME);
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
                String currentTime = cursor.getString(timeColumnIndex);
                String currentNotif = cursor.getString(notifColumnIndex);
                String currentWatched = cursor.getString(watchedColumnIndex);
                String currentArchived = cursor.getString(archivedColumnIndex);

                Film film = new Film(currentID, currentName, currentGenre, currentYear, currentDirector, currentDuration, currentProd, currentSynopsis);
                int n = Integer.parseInt(currentArchived);
                film.setDateToWatch(currentDate);
                film.setTimeToWatch(currentTime);
                film.setArchived((n == 1)? true : false);
                film.setNotifSettings(currentNotif);
                film.setThumbnailBmp(BitmapConverter.stringToBitMap(currentImage));
                //Log.wtf(LOG_TAG, "CURRENT ARCHIVED: " + n);
                entertainments.add(film);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void getBooks(int isArchived){
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
                BookEntry.COLUMN_BOOK_NOTIF_TIME,
                BookEntry.COLUMN_BOOK_READ,
                BookEntry.COLUMN_BOOK_ARCHIVED };

        String selection = BookEntry.COLUMN_BOOK_ARCHIVED + "=?";
        String[] selectionArgs = new String[] { String.valueOf(isArchived) };

        // Perform a query on the pets table
        /*Cursor cursor = db.query(
                BookEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                selection,                  // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                BookEntry.COLUMN_LAST_UPDATE+" DESC");                   // The sort order

        //TextView displayView = (TextView) findViewById(R.id.text_view_pet);
        */
        Cursor cursor = rootView.getContext().getContentResolver().query(
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
            int timeColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NOTIF_TIME);
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
                String currentTime = cursor.getString(timeColumnIndex);
                String currentNotif = cursor.getString(notifColumnIndex);
                String currentWatched = cursor.getString(watchedColumnIndex);
                String currentArchived = cursor.getString(archivedColumnIndex);

                Book book = new Book(currentID, currentName, currentGenre, currentYear, currentDirector, currentDuration, currentProd, currentSynopsis);
                int n = Integer.parseInt(currentArchived);
                book.setDateToRead(currentDate);
                book.setTimeToRead(currentTime);
                book.setNotifSettings(currentNotif);
                book.setArchived((n == 1)? true : false);
                book.setThumbnailBmp(BitmapConverter.stringToBitMap(currentImage));
                entertainments.add(book);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void getGames(int isArchived){
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
                GameEntry.COLUMN_GAME_NOTIF_TIME,
                GameEntry.COLUMN_GAME_PLAYED,
                GameEntry.COLUMN_GAME_ARCHIVED };

        String selection = GameEntry.COLUMN_GAME_ARCHIVED + "=?";
        String[] selectionArgs = new String[] { String.valueOf(isArchived) };

        // Perform a query on the pets table
        /*Cursor cursor = db.query(
                GameEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                selection,                  // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                GameEntry.COLUMN_LAST_UPDATE+" DESC");                   // The sort order

        //TextView displayView = (TextView) findViewById(R.id.text_view_pet);
        */
        Cursor cursor = rootView.getContext().getContentResolver().query(
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
            int timeColumnIndex = cursor.getColumnIndex(GameEntry.COLUMN_GAME_NOTIF_TIME);
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
                String currentTime = cursor.getString(timeColumnIndex);
                String currentNotif = cursor.getString(notifColumnIndex);
                String currentWatched = cursor.getString(watchedColumnIndex);
                String currentArchived = cursor.getString(archivedColumnIndex);

                Game game = new Game(currentID, currentName, currentGenre, currentYear, currentDirector, currentDuration, currentProd, currentSynopsis);
                int n = Integer.parseInt(currentArchived);
                game.setDateToPlay(currentDate);
                game.setTimeToPlay(currentTime);
                game.setArchived((n == 1)? true : false);
                game.setNotifSettings(currentNotif);
                game.setThumbnailBmp(BitmapConverter.stringToBitMap(currentImage));
                entertainments.add(game);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        /*
        if (viewHolder instanceof MediaAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = entertainments.get(viewHolder.getAdapterPosition()).getmMediaName();

            // backup of removed item for undo purpose
            final Media deletedItem = entertainments.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            String action = " archived!";
            if(!deletedItem.isArchived()){  // if item is not yet archived
                deletedItem.setArchived(true);  // archive it!
                updateMedia(getContentUri(deletedItem), deletedItem, true);
            }
            if(isArchived){
                //action = " deleted from media roll!";
                //deleteMedia(getContentUri(deletedItem), deletedItem);
                showDeleteConfirmationDialog(deletedItem, deletedIndex);
            }

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            shouldDisplayEmptyView();

            // showing snack bar with Undo option

            if(!isArchived) {
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, name + action, Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // undo is selected, restore back to how it was before
                        if (deletedItem.isArchived()) { // if it was archived, then unarchive it
                            deletedItem.setArchived(false);
                            updateMedia(getContentUri(deletedItem), deletedItem, false);
                        }

                        mAdapter.restoreItem(deletedItem, deletedIndex);
                        mEmptyStateTextView.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);

                snackbar.show();
            }
        }
        */
    }
    
    private void shouldDisplayEmptyView(){
        if(entertainments.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            TextView title = (TextView) mEmptyStateTextView.findViewById(R.id.empty_title_text);
            TextView subtitle = (TextView) mEmptyStateTextView.findViewById(R.id.empty_subtitle_text);
            switch (isArchived ? 1 : 0){
                case 1:
                    //mEmptyStateTextView.setText("You have no archived media.");
                    title.setText(R.string.empty_archived_view_subtitle_text);
                    subtitle.setText("");
                    break;
                case 0:
                    title.setText(R.string.empty_view_title_text);
                    switch (mediaCategory){
                        case CategoryAdapter.CATEGORY_ALL:
                            subtitle.setText(R.string.empty_view_subtitle_text);
                            break;
                        case CategoryAdapter.CATEGORY_FILMS:
                            subtitle.setText(R.string.empty_film_view_subtitle_text);
                            break;
                        case CategoryAdapter.CATEGORY_BOOKS:
                            subtitle.setText(R.string.empty_book_view_subtitle_text);
                            break;
                        case CategoryAdapter.CATEGORY_GAMES:
                            subtitle.setText(R.string.empty_game_view_subtitle_text);
                            break;
                    }
                    break;
            }
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        else {
            mEmptyStateTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void updateMedia(Uri uri, Media media, boolean willArchive){
        ContentValues values = new ContentValues();
        if(willArchive){
            values.put(FilmEntry.COLUMN_FILM_ARCHIVED, "1");
        }
        else{
            values.put(FilmEntry.COLUMN_FILM_ARCHIVED, "0");
        }

        Uri currentUri = getCurrentUri(uri, media);

        int rowsAffected = rootView.getContext().getContentResolver().update(currentUri, values, null, null);

        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(rootView.getContext(), getString(R.string.editor_update_media_failed), Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(rootView.getContext(), getString(R.string.editor_update_media_successful), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteMedia(Uri uri, Media media){
        Uri currentUri = getCurrentUri(uri, media);

        // Call the ContentResolver to delete the media at the given content URI.
        // content URI already identifies the media that we want.
        int rowsDeleted = rootView.getContext().getContentResolver().delete(currentUri, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(rootView.getContext(), getString(R.string.editor_delete_media_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(rootView.getContext(), getString(R.string.editor_delete_media_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Prompt the user to confirm that they want to delete this media.
     */
    private void showDeleteConfirmationDialog(final Media deletedItem, final int deletedIndex) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the media.
                deleteMedia(getContentUri(deletedItem), deletedItem);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the media.
                if (dialog != null) {
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private Uri getCurrentUri(Uri uri, Media media){
        if(media instanceof Film){
            return ContentUris.withAppendedId(uri, Long.parseLong(media.getMediaID()));
        } else if(media instanceof Book){
            return Uri.withAppendedPath(uri, media.getMediaID());
        } else {
            return ContentUris.withAppendedId(uri, Long.parseLong(media.getMediaID()));
        }
    }

    private Uri getContentUri(Media media){
        if(media instanceof Film){
            return FilmEntry.CONTENT_URI;
        } else if(media instanceof Book){
            return BookEntry.CONTENT_URI;
        } else {
            return GameEntry.CONTENT_URI;
        }
    }
}
