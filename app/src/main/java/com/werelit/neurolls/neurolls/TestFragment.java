package com.werelit.neurolls.neurolls;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.werelit.neurolls.neurolls.model.Book;
import com.werelit.neurolls.neurolls.model.Film;
import com.werelit.neurolls.neurolls.model.Game;
import com.werelit.neurolls.neurolls.model.Media;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    /** The list containing Media objects */               private List<Media> entertainments = new ArrayList<>();
    /** The recycler view containing the Media items */    private RecyclerView mRecyclerView;
    /** The adapter used for the recycler view */               private MediaAdapter mAdapter;
    /** The layout manager for the recycler view */             private RecyclerView.LayoutManager mLayoutManager;
    /** The layout for the snackbar with undo delete */         private ConstraintLayout constraintLayout;
    /** TextView that is displayed when the list is empty */    private TextView mEmptyStateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(
                R.layout.entertainment_roll, container, false);

        // use a constraint layout for the delete snackbar with UNDO
        constraintLayout = rootView.findViewById(R.id.constraint_layout);

        // set visibility of the empty view to be GONE initially
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        // setup the recycler view adapter, layout, etc.
        prepareRecyclerView(rootView);

        // add Media items into the Medias list
        prepareMedias();

        // prepare the buttons in the UI
        prepareButtons(rootView);

        return rootView;
    }

    /**
     * This method setups the recycler view
     * - setting the adapter
     * - setting the layout of the recycler view
     * - adding an item touch listener, etc.
     */
    public void prepareRecyclerView(final View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // This draws a line separator for each row, but card views are used so no need for this
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // specify an adapter (see also next example)
        mAdapter = new MediaAdapter(entertainments);
        mRecyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);


        // add a click listener to go to the restaurant details for editing an existing item
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(rootView.getContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(rootView.getContext(), entertainments.get(position).getmMediaName() + " is selected!", Toast.LENGTH_SHORT).show();

                // Make a bundle containing the current restaurant details
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("name", entertainments.get(position).getmMediaName());
                bundle.putString("genre", entertainments.get(position).getmMediaGenre());
                bundle.putDouble("year", entertainments.get(position).getmMediaYear());

                // View the details depending what category the media is
                Media media = entertainments.get(position);
                Intent intent = new Intent();


                if(media instanceof Film){
                    //bundle.putInt();
                    intent.setClass(rootView.getContext(), ViewFilmDetails.class);
                }
                else if(media instanceof Book){
                    intent.setClass(rootView.getContext(), ViewBookDetails.class);
                }
                else if(media instanceof Game){
                    intent.setClass(rootView.getContext(), ViewGameDetails.class);
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
        entertainments.add(new Media("Pericos", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Media("La Casita @ 6th Andrew", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Media("La Casita @ 2nd Razon", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Media("first resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Media("second resto", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Media("third resto", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Media("fourth resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Media("5th rest", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Media("6th resto", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Media("seventh resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Media("eighth resto", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Media("9th", "Canteen @ Razon building DLSU", 3));
    }

    /**
     * This method setups the buttons to be displayed in the Media activity UI
     */
    public void prepareButtons(final View rootView){

        // ADD Button to go to add a new Media activity
        Button add = (Button) rootView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), MainActivity.class);
                //startActivityForResult(intent, ADD_RESTO_REQUEST);
            }
        });

        // SURPRISE button to pick a random Media
        Button surprise = (Button) rootView.findViewById(R.id.surprise);
        surprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EQUAL RANDOMNESS
                Collections.shuffle(entertainments);
                mAdapter.notifyDataSetChanged(); //enable this to view the shuffling animation

                if(entertainments.size() != 0){
                    Snackbar snackbar = Snackbar.make(view, "Go for... " + entertainments.get(entertainments.size()/2).getmMediaName() + "!!!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null).show();
                    TextView snackbarActionTextView =  snackbar.getView().findViewById( android.support.design.R.id.snackbar_text );
                    snackbarActionTextView.setTextSize( 30 );
                    snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
                }
            }
        });

        // CLEAR Button to go to add a new Media activity
        Button clear = (Button) rootView.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entertainments.clear();
                //mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.GONE);
                mEmptyStateTextView.setText("No Medias. :(");
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MediaAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = entertainments.get(viewHolder.getAdapterPosition()).getmMediaName();

            // backup of removed item for undo purpose
            final Media deletedItem = entertainments.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            if(entertainments.size() == 0 ){
                mRecyclerView.setVisibility(View.GONE);
                mEmptyStateTextView.setText("No Medias. :(");
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);

            snackbar.show();
        }
    }
}
