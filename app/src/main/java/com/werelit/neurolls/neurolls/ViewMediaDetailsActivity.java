package com.werelit.neurolls.neurolls;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewMediaDetailsActivity extends AppCompatActivity{

    private TextView name, genre, year;
    private ImageView image;
    private View rootView;
    private boolean isArchived = false;
    private boolean isForAdding = false;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();
        setupClickListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

        }

        // Inflate the menu; this adds items to the action bar if it is present.
        if(isArchived){ // if item is archived, menu shows delete or unarchive options
            getMenuInflater().inflate(R.menu.archived_media_menu, menu);
        }
        else if(isForAdding){   // if this is a new media for adding, then menu shows submit or cancel options
            getMenuInflater().inflate(R.menu.add_media_menu, menu);
        }
        else{   // else menu shows archive or share options
            getMenuInflater().inflate(R.menu.unarchived_media_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_archive) {
            //this.finish();
            Toast.makeText(this, "TO DO: set media to Archived!", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.action_share) {
            //this.finish();
            Toast.makeText(this, "TO DO: Share by calling implicit intent!", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.action_submit) {
            //this.finish();
            Toast.makeText(this, "TO DO: insert new media to db!", Toast.LENGTH_SHORT).show();
            // TODO db insertion happens here
        }
        else if(item.getItemId() == R.id.action_cancel) {
            this.finish();
        }
        else if(item.getItemId() == R.id.action_unarchive) {
            //this.finish();
            Toast.makeText(this, "TO DO: set media to unarchived!", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.action_delete) {
            //this.finish();
            Toast.makeText(this, "TO DO: delete media from db!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupUI(){

        // get intent that was passed when the recycler view item was pressed
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) { // check first if the bundle is not empty

            // get all the common attributes of the 3 categories of media
            String mediaName = bundle.getString(MediaKeys.MEDIA_NAME_KEY);
            String mediaGenre = bundle.getString(MediaKeys.MEDIA_GENRE_KEY);
            String mediaYear = bundle.getString(MediaKeys.MEDIA_YEAR_KEY);

            isArchived = bundle.getBoolean(MediaKeys.MEDIA_ARCHIVED);
            isForAdding = bundle.getBoolean(MediaKeys.ADDING_NEW_MEDIA);

            // get the the media category (depending which type of recycler view item was pressed)
            // this only applies for the view ALL media fragment but neverthe less this activity
            // will be used for all the other fragments for each type of category
            int mediaCategory = bundle.getInt(MediaKeys.MEDIA_CATEGORY_KEY);

            // if the recycler view item pressed is a film,
            if (mediaCategory == CategoryAdapter.CATEGORY_FILMS) {

                // set the content of the detail to the film detail xml layout then
                // get the views of that view group
                setContentView(R.layout.view_film_detail);
                TextView duration = (TextView) findViewById(R.id.duration);
                TextView director = (TextView) findViewById(R.id.director);
                TextView production = (TextView) findViewById(R.id.production);
                TextView synopsis = (TextView) findViewById(R.id.synopsis);

                // get the attributes for a Film object
                int filmDuration = bundle.getInt(MediaKeys.FILM_DURATION_KEY);
                String filmDirector = bundle.getString(MediaKeys.FILM_DIRECTOR_KEY);
                String filmProduction = bundle.getString(MediaKeys.FILM_PRODUCTION_KEY);
                String filmSynopsis = bundle.getString(MediaKeys.FILM_SYNOPSIS_KEY);

                // set the views of the xml layout to the attribute values
                duration.setText("" + filmDuration);
                director.setText(filmDirector);
                production.setText(filmProduction);
                synopsis.setText(filmSynopsis);

            }
            // if the recycler view item pressed is a book,
            else if(mediaCategory == CategoryAdapter.CATEGORY_BOOKS) {
                // set the content of the detail to the book detail xml layout then
                // get the views of that view group
                setContentView(R.layout.view_book_detail);
                TextView author = (TextView) findViewById(R.id.author);
                TextView publisher = (TextView) findViewById(R.id.publisher);
                TextView description = (TextView) findViewById(R.id.description);

                // get the attributes for a Book object
                String bookAuthor = bundle.getString(MediaKeys.BOOK_AUTHOR_KEY);
                String bookPublisher = bundle.getString(MediaKeys.BOOK_PUBLISHER_KEY);
                String bookDescription = bundle.getString(MediaKeys.BOOK_DESCRIPTION_KEY);

                // set the views of the xml layout to the attribute values
                author.setText(bookAuthor);
                publisher.setText(bookPublisher);
                description.setText(bookDescription);

            }else if(mediaCategory == CategoryAdapter.CATEGORY_GAMES) {

                // set the content of the detail to the game detail xml layout then
                // get the views of that view group
                setContentView(R.layout.view_game_detail);
                TextView platform = (TextView) findViewById(R.id.platform);
                TextView publisher = (TextView) findViewById(R.id.publisher);
                TextView series = (TextView) findViewById(R.id.series);
                TextView storyline = (TextView) findViewById(R.id.storyline);

                // get the attributes for a Game object
                String gamePlatform = bundle.getString(MediaKeys.GAME_PLATFORM_KEY);
                String gamePublisher = bundle.getString(MediaKeys.GAME_PUBLISHER_KEY);
                String gameSeries = bundle.getString(MediaKeys.GAME_PLATFORM_KEY);
                String gameStoryline = bundle.getString(MediaKeys.GAME_STORYLINE_KEY);

                // set the views of the xml layout to the attribute values
                platform.setText(gamePlatform);
                publisher.setText(gamePublisher);
                series.setText(gameSeries);
                storyline.setText(gameStoryline);
            }

            name = (TextView) findViewById(R.id.name);
            genre = (TextView) findViewById(R.id.genre);
            year = (TextView) findViewById(R.id.year);

            name.setText("" + mediaName);
            genre.setText("" + mediaGenre);
            year.setText("" + mediaYear);
        }
    }

    public void setupClickListeners(){

        // TODO set up date picker, notification modal
        LinearLayout scheduledDate = (LinearLayout) findViewById(R.id.scheduled_date);
        final TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
        scheduledDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewMediaDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                //String month = c.getDisplayName(monthOfYear, Calendar.SHORT, Locale.getDefault());
                                c.set(year, monthOfYear, dayOfMonth);

                                //get selected date from datepicker dialog
                                Date SelectedDate = c.getTime();

                                //date format in US: e.g. September 14, 1998
                                DateFormat dateformat_US = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
                                String StringDateformat_US = dateformat_US.format(SelectedDate);
                                dateTextView.setText(StringDateformat_US);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                Toast.makeText(ViewMediaDetailsActivity.this, "TO DO: Date Picker!", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout notifSettings = (LinearLayout) findViewById(R.id.notif_settings);
        notifSettings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO
                Toast.makeText(ViewMediaDetailsActivity.this, "TO DO: Notif Settings Modal!", Toast.LENGTH_SHORT).show();
                //displayNotifSettings(v);
            }
        });
    }

    //Changes the fragment displayed
    public void displayNotifSettings(View view) {
        //Calls and displays NotificationSettings dialog
        if (view == findViewById(R.id.notif_settings)) {
            FragmentManager fm = getSupportFragmentManager();
            NotificationSettings notifSettings = new NotificationSettings();
            notifSettings.show(fm, "Notification Settings");
        }
    }
}
