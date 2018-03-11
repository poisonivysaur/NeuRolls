package com.werelit.neurolls.neurolls;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewAllMediaFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    /** The list containing Media objects */                    private List<Media> entertainments;
    /** The recycler view containing the Media items */         private RecyclerView mRecyclerView;
    /** The adapter used for the recycler view */               private MediaAdapter mAdapter;
    /** The layout manager for the recycler view */             private RecyclerView.LayoutManager mLayoutManager;
    /** The layout for the snackbar with undo delete */         private ConstraintLayout constraintLayout;
    /** TextView that is displayed when the list is empty */    private TextView mEmptyStateTextView;

    private int mediaCategory = -1;
    private boolean isArchived = false;
    private NeurollsDbHelper mDbHelper;

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
        if(mRecyclerView != null)
            mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(
                R.layout.view_all_media, container, false);

        // use a constraint layout for the delete snackbar with UNDO
        constraintLayout = rootView.findViewById(R.id.constraint_layout);

        // set visibility of the empty view to be GONE initially
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        // add Media items into the Medias list
        entertainments = new ArrayList<>();

        // setup the recycler view adapter, layout, etc.
        prepareRecyclerView(rootView);

        mDbHelper = new NeurollsDbHelper(rootView.getContext());
        displayDatabaseInfo();

        prepareMedias();
        if(entertainments.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setText("You have no media to enjoy later :(");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

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
                //Toast.makeText(rootView.getContext(), entertainments.get(position).getmMediaName() + " is selected!", Toast.LENGTH_SHORT).show();

                // Make a bundle containing the current media details
                Bundle bundle = new Bundle();

                bundle.putBoolean(MediaKeys.ADDING_NEW_MEDIA, false);
                bundle.putString(MediaKeys.MEDIA_ID_KEY, entertainments.get(position).getMediaID());
                bundle.putString(MediaKeys.MEDIA_NAME_KEY, entertainments.get(position).getmMediaName());
                bundle.putString(MediaKeys.MEDIA_GENRE_KEY, entertainments.get(position).getmMediaGenre());
                bundle.putString(MediaKeys.MEDIA_YEAR_KEY, entertainments.get(position).getmMediaYear());
                bundle.putBoolean(MediaKeys.MEDIA_ARCHIVED, entertainments.get(position).isArchived());

                // TODO add image directory to bundle

                // View the details depending what category the media is
                Media media = entertainments.get(position);
                Intent intent = new Intent(rootView.getContext(), ViewMediaDetailsActivity.class);

                if(media instanceof Film){
                    bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_FILMS);
                    bundle.putInt(MediaKeys.FILM_DURATION_KEY, ((Film)entertainments.get(position)).getDuration());
                    bundle.putString(MediaKeys.FILM_DIRECTOR_KEY, ((Film)entertainments.get(position)).getDirector());
                    bundle.putString(MediaKeys.FILM_PRODUCTION_KEY, ((Film)entertainments.get(position)).getProduction());
                    bundle.putString(MediaKeys.FILM_SYNOPSIS_KEY, ((Film)entertainments.get(position)).getSynopsis());
                }
                else if(media instanceof Book){
                    bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_BOOKS);
                    bundle.putString(MediaKeys.BOOK_AUTHOR_KEY, ((Book)entertainments.get(position)).getAuthor());
                    bundle.putString(MediaKeys.BOOK_PUBLISHER_KEY, ((Book)entertainments.get(position)).getPublisher());
                    bundle.putString(MediaKeys.BOOK_DESCRIPTION_KEY, ((Book)entertainments.get(position)).getDescription());
                }
                else if(media instanceof Game){
                    bundle.putInt(MediaKeys.MEDIA_CATEGORY_KEY, CategoryAdapter.CATEGORY_GAMES);
                    bundle.putString(MediaKeys.GAME_PLATFORM_KEY, ((Game)entertainments.get(position)).getPlatform());
                    bundle.putString(MediaKeys.GAME_PUBLISHER_KEY, ((Game)entertainments.get(position)).getPublisher());
                    bundle.putString(MediaKeys.GAME_SERIES_KEY, ((Game)entertainments.get(position)).getSeries());
                    bundle.putString(MediaKeys.GAME_STORYLINE_KEY, ((Game)entertainments.get(position)).getStoryline());
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getApplicationContext(), "You Long pressed me!", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    /**
     * This method adds the dummy Media data into the Medias list.
     */
    private void prepareMedias() {

        // TODO query db and return the result as an ArrayList entertainments

        /*
        entertainments.add(new Film("ID#1", "Phantom of the Opera", "Drama/Thriller", "2004",
                203, "Joel Schumacher", "Joel Schumacher Productions, Really Useful Films, Scion Films",
                "From his hideout beneath a 19th century Paris opera house, the brooding Phantom (Gerard Butler) schemes to get closer to vocalist Christine Daae (Emmy Rossum). The Phantom, wearing a mask to hide a congenital disfigurement, strong-arms management into giving the budding starlet key roles, but Christine instead falls for arts benefactor Raoul (Patrick Wilson). Terrified at the notion of her absence, the Phantom enacts a plan to keep Christine by his side, while Raoul tries to foil the scheme."));
        entertainments.add(new Film("ID#1", "不能說的·秘密", "Drama/Fantasy", "2007",
                101, "Jay Chou", "Sony Pictures Taiwan, Edko Film",
                "A piano prodigy (Jay Chou) encounters two mysterious students at a college of arts."));
        entertainments.add(new Book("ID#1", "Charlotte's Web", "Children's literature", "1952", "E. B. White", 192, "Harper & Brothers", "Charlotte's Web is a children's novel by American author E. B. White and illustrated by Garth Williams; it was published on October 15, 1952, by Harper & Brothers."));
        entertainments.add(new Game("ID#1", "Shadow the Hedgehog", "Platformer, action-adventure, third-person shooter", "2005",
                "Nintendo GameCube, PlayStation 2, Xbox", "Sega", "Sonic the Hedgehog", "Shadow the Hedgehog is a platform video game developed by Sega Studio USA, the former United States division of Sega's Sonic Team, and published by Sega."));
        entertainments.add(new Film("ID#1", "Sherlock Holmes", "Thriller/Action", "2009",
                130, "Guy Ritchie", "Silver Pictures, Wigram Productions, Village Roadshow Pictures",
                "When a string of brutal murders terrorizes London, it doesn't take long for legendary detective Sherlock Holmes (Robert Downey Jr.) and his crime-solving partner, Dr. Watson (Jude Law), to find the killer, Lord Blackwood (Mark Strong). A devotee of the dark arts, Blackwood has a bigger scheme in mind, and his execution plays right into his plans. The game is afoot when Blackwood seems to rise from the grave, plunging Holmes and Watson into the world of the occult and strange technologies."));
        entertainments.add(new Film("ID#1", "Sherlock Holmes: A Game of Shadows", "Crime film/Thriller", "2011",
                129, "Guy Ritchie", "Silver Pictures, Wigram Productions, Village Roadshow Pictures",
                "When Austria's crown prince is found dead, evidence seems to point to suicide. However, detective Sherlock Holmes (Robert Downey Jr.) deduces that the prince was murdered and that the crime is but a piece of a puzzle designed by an evil genius named Moriarty (Jared Harris). Holmes and his friend Dr. Watson (Jude Law), who are accompanied by a Gypsy (Noomi Rapace) whose life Holmes saved, chase Moriarty across Europe in the hope that they can thwart his plot before it can come to fruition."));
        entertainments.add(new Film("ID#1", "The Theory of Everything", "Drama/Romance", "2014",
                123, "James Marsh", "Working Title Films",
                "In the 1960s, Cambridge University student and future physicist Stephen Hawking (Eddie Redmayne) falls in love with fellow collegian Jane Wilde (Felicity Jones). At 21, Hawking learns that he has motor neuron disease. Despite this -- and with Jane at his side -- he begins an ambitious study of time, of which he has very little left, according to his doctor. He and Jane defy terrible odds and break new ground in the fields of medicine and science, achieving more than either could hope to imagine."));
        entertainments.add(new Film("ID#1", "Astro Boy", "Action/Adventure", "2009",
                94, "David Bowers", "Imagi Animation Studios",
                "In futuristic Metro City, a brilliant scientist named Tenma builds Astro Boy (Freddie Highmore), a robotic child with superstrength, X-ray vision and the ability to fly. Astro Boy sets out to explore the world and find acceptance, learning what being human is all about in the process. Finding that his friends and family in Metro City are in danger, he uses his incredible powers to save all that he loves."));
        entertainments.add(new Film("ID#1", "A Beautiful Mind", " Drama/Romance", "2001",
                101, "Ron Howard", "Imagine Entertainment",
                "A human drama inspired by events in the life of John Forbes Nash Jr., and in part based on the biography \"A Beautiful Mind\" by Sylvia Nasar. From the heights of notoriety to the depths of depravity, John Forbes Nash Jr. experienced it all. A mathematical genius, he made an astonishing discovery early in his career and stood on the brink of international acclaim. But the handsome and arrogant Nash soon found himself on a painful and harrowing journey of self-discovery."));
              */
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MediaAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = entertainments.get(viewHolder.getAdapterPosition()).getmMediaName();

            // backup of removed item for undo purpose
            final Media deletedItem = entertainments.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            String action = " archived!";
            if(!deletedItem.isArchived()){  // if item is not yet archived
                deletedItem.setArchived(true);  // archive it!
            }
            if(isArchived){
                action = " deleted from media roll!";
            }

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            if(entertainments.size() == 0 ){
                mRecyclerView.setVisibility(View.GONE);
                mEmptyStateTextView.setText("You have no media yet :(");
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }

            // showing snack bar with Undo option

            Snackbar snackbar = Snackbar
                    .make(constraintLayout, name + action, Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    if(deletedItem.isArchived()){
                        deletedItem.setArchived(false);
                    }

                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);

            snackbar.show();

            // TODO perform DELETE on the db if an item was removed completely & if that media is already archived
            // TODO if it just archived, then UPDATE the db only and not DELETE
        }
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the films database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FilmEntry._ID,
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

        // Perform a query on the pets table
        Cursor cursor = db.query(
                FilmEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        //TextView displayView = (TextView) findViewById(R.id.text_view_pet);

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

                entertainments.add(new Film(currentID, currentName, currentGenre, currentYear, currentDuration, currentDirector, currentProd, currentSynopsis));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded media data into the database. For debugging purposes only.
     */
    private void insertMedia() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a ContentValues object where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MediaContract.FilmEntry.COLUMN_FILM_ID, "Test ID");
        values.put(MediaContract.FilmEntry.COLUMN_FILM_NAME, "Astro Boy");
        values.put(MediaContract.FilmEntry.COLUMN_FILM_GENRE, "Action/Adventure");
        values.put(MediaContract.FilmEntry.COLUMN_FILM_YEAR_RELEASED, "2009-03-10");

        values.put(MediaContract.FilmEntry.COLUMN_FILM_DIRECTOR, "David Bowers");
        values.put(MediaContract.FilmEntry.COLUMN_FILM_DURATION, 94);
        values.put(MediaContract.FilmEntry.COLUMN_FILM_PRODUCTION, "Imagi Animation Studios");
        values.put(MediaContract.FilmEntry.COLUMN_FILM_SYNOPSIS, "In futuristic Metro City, a brilliant scientist named Tenma builds Astro Boy (Freddie Highmore), a robotic child with superstrength, X-ray vision and the ability to fly. Astro Boy sets out to explore the world and find acceptance, learning what being human is all about in the process. Finding that his friends and family in Metro City are in danger, he uses his incredible powers to save all that he loves.");

        values.put(MediaContract.FilmEntry.COLUMN_FILM_IMG_DIR, "test/img/dir.png");
        values.put(MediaContract.FilmEntry.COLUMN_FILM_DATE_TO_WATCH, "2018-03-10");
        values.put(MediaContract.FilmEntry.COLUMN_FILM_NOTIF_SETTINGS, "test notif settings");

        long newRowID = db.insert(MediaContract.FilmEntry.TABLE_NAME, null, values);

        Log.wtf("VIEW ALL MEDIA FRAGMENT", "" + newRowID);
    }

}
