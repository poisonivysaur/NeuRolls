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

        Toast.makeText(rootView.getContext(), "on create view !!!", Toast.LENGTH_SHORT).show();

        // use a constraint layout for the delete snackbar with UNDO
        constraintLayout = rootView.findViewById(R.id.constraint_layout);

        // set visibility of the empty view to be GONE initially
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        // setup the recycler view adapter, layout, etc.
        prepareRecyclerView(rootView);

        // add Media items into the Medias list
        if(entertainments.isEmpty())
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
                //bundle.putDouble("year", entertainments.get(position).getmMediaYear());

                // View the details depending what category the media is
                Media media = entertainments.get(position);
                Intent intent = new Intent();


                if(media instanceof Film){
                    //bundle.putInt();
                    Toast.makeText(rootView.getContext(), "instanceof Film", Toast.LENGTH_SHORT).show();
                    intent.setClass(rootView.getContext(), ViewMediaDetailsActivity.class);
                }
                else if(media instanceof Book){
                    Toast.makeText(rootView.getContext(), "instanceof Book", Toast.LENGTH_SHORT).show();
                    intent.setClass(rootView.getContext(), ViewMediaDetailsActivity.class);
                }
                else if(media instanceof Game){
                    Toast.makeText(rootView.getContext(), "instanceof Game", Toast.LENGTH_SHORT).show();
                    intent.setClass(rootView.getContext(), ViewMediaDetailsActivity.class);
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
        /*
        entertainments.add(new Film("Phantom of the Opera", "Drama/Thriller", 2004,
                203, "Joel Schumacher", "Joel Schumacher Productions, Really Useful Films, Scion Films",
                "From his hideout beneath a 19th century Paris opera house, the brooding Phantom (Gerard Butler) schemes to get closer to vocalist Christine Daae (Emmy Rossum). The Phantom, wearing a mask to hide a congenital disfigurement, strong-arms management into giving the budding starlet key roles, but Christine instead falls for arts benefactor Raoul (Patrick Wilson). Terrified at the notion of her absence, the Phantom enacts a plan to keep Christine by his side, while Raoul tries to foil the scheme."));
        entertainments.add(new Film("不能說的·秘密", "Drama/Fantasy", 2007,
                101, "Jay Chou", "Sony Pictures Taiwan, Edko Film",
                "A piano prodigy (Jay Chou) encounters two mysterious students at a college of arts."));
        entertainments.add(new Book("Charlotte's Web", "Children's literature", 1952, "E. B. White","Harper & Brothers", "Charlotte's Web is a children's novel by American author E. B. White and illustrated by Garth Williams; it was published on October 15, 1952, by Harper & Brothers."));
        entertainments.add(new Game("Shadow the Hedgehog", "Platformer, action-adventure, third-person shooter", 2005,
                "Nintendo GameCube, PlayStation 2, Xbox", "Sega", "Shadow the Hedgehog is a platform video game developed by Sega Studio USA, the former United States division of Sega's Sonic Team, and published by Sega.", "Sonic the Hedgehog"));
        entertainments.add(new Film("Sherlock Holmes", "Thriller/Action", 2009,
                130, "Guy Ritchie", "Silver Pictures, Wigram Productions, Village Roadshow Pictures",
                "When a string of brutal murders terrorizes London, it doesn't take long for legendary detective Sherlock Holmes (Robert Downey Jr.) and his crime-solving partner, Dr. Watson (Jude Law), to find the killer, Lord Blackwood (Mark Strong). A devotee of the dark arts, Blackwood has a bigger scheme in mind, and his execution plays right into his plans. The game is afoot when Blackwood seems to rise from the grave, plunging Holmes and Watson into the world of the occult and strange technologies."));
        entertainments.add(new Film("Sherlock Holmes: A Game of Shadows", "Crime film/Thriller", 2011,
                129, "Guy Ritchie", "Silver Pictures, Wigram Productions, Village Roadshow Pictures",
                "When Austria's crown prince is found dead, evidence seems to point to suicide. However, detective Sherlock Holmes (Robert Downey Jr.) deduces that the prince was murdered and that the crime is but a piece of a puzzle designed by an evil genius named Moriarty (Jared Harris). Holmes and his friend Dr. Watson (Jude Law), who are accompanied by a Gypsy (Noomi Rapace) whose life Holmes saved, chase Moriarty across Europe in the hope that they can thwart his plot before it can come to fruition."));
        entertainments.add(new Film("The Theory of Everything", "Drama/Romance", 2014,
                123, "James Marsh", "Working Title Films",
                "In the 1960s, Cambridge University student and future physicist Stephen Hawking (Eddie Redmayne) falls in love with fellow collegian Jane Wilde (Felicity Jones). At 21, Hawking learns that he has motor neuron disease. Despite this -- and with Jane at his side -- he begins an ambitious study of time, of which he has very little left, according to his doctor. He and Jane defy terrible odds and break new ground in the fields of medicine and science, achieving more than either could hope to imagine."));
        entertainments.add(new Film("Astro Boy", "Action/Adventure", 2009,
                94, "David Bowers", "Imagi Animation Studios",
                "In futuristic Metro City, a brilliant scientist named Tenma builds Astro Boy (Freddie Highmore), a robotic child with superstrength, X-ray vision and the ability to fly. Astro Boy sets out to explore the world and find acceptance, learning what being human is all about in the process. Finding that his friends and family in Metro City are in danger, he uses his incredible powers to save all that he loves."));
        entertainments.add(new Film("A Beautiful Mind", " Drama/Romance", 2001,
                101, "Ron Howard", "Imagine Entertainment",
                "A human drama inspired by events in the life of John Forbes Nash Jr., and in part based on the biography \"A Beautiful Mind\" by Sylvia Nasar. From the heights of notoriety to the depths of depravity, John Forbes Nash Jr. experienced it all. A mathematical genius, he made an astonishing discovery early in his career and stood on the brink of international acclaim. But the handsome and arrogant Nash soon found himself on a painful and harrowing journey of self-discovery."));
                */
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
                mEmptyStateTextView.setText("No Media. :(");
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
