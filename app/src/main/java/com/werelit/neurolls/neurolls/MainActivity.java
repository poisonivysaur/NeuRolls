package com.werelit.neurolls.neurolls;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    public static final int SEARCH_NEUROLLS = 0;
    public static final int FILM_FAB = 1;
    public static final int BOOK_FAB = 2;
    public static final int GAME_FAB = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFABs();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setupNavigationDrawer();
        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager();
        }

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = "Neurolls";
        //Intent intent = new Intent();

        boolean pressedArchive = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        int archiveColor = getResources().getColor(R.color.colorPrimary);

        if (id == R.id.nav_home) {
            // Handle the home action
            viewPager.setCurrentItem(CategoryAdapter.CATEGORY_ALL);
        } else if (id == R.id.nav_archived) {
            title = "Archived Media";
            pressedArchive = true;
            archiveColor = getResources().getColor(R.color.item_name);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(MainActivity.this, SearchMediaActivity.class);
            intent.putExtra(MediaKeys.FAB_PRESSED, SEARCH_NEUROLLS);
            startActivity(intent);
        } else if (id == R.id.nav_films) {
            viewPager.setCurrentItem(CategoryAdapter.CATEGORY_FILMS);
        } else if (id == R.id.nav_books) {
            viewPager.setCurrentItem(CategoryAdapter.CATEGORY_BOOKS);
        } else if (id == R.id.nav_games) {
            viewPager.setCurrentItem(CategoryAdapter.CATEGORY_GAMES);
        }
        /*
        if(item.getItemId() == R.id.nav_home)
            setTitle("NeuRolls");
        else
            setTitle(item.getTitle());
        startActivity(i);
        */

        getFilteredMedia(pressedArchive);
        viewPager.getAdapter().notifyDataSetChanged();

        toolbar.setBackgroundColor(archiveColor);

        setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setupFABs(){

        FabSpeedDial fab = (FabSpeedDial) findViewById(R.id.fab);
        fab.addOnStateChangeListener(new FabSpeedDial.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean open) {
                // do something
            }
        });

        fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton fab, TextView textView, int id) {

                Intent intent = new Intent(MainActivity.this, SearchMediaActivity.class);

                if (id == R.id.action_films) {
                    // Handle the home action
                    // TODO put in intent extras/ bundles here to see if it was from film, books, or games
                    intent.putExtra(MediaKeys.FAB_PRESSED, FILM_FAB);
                    startActivity(intent);
                } else if (id == R.id.action_books) {
                    //Toast.makeText(MainActivity.this , " Books clicked ", Toast.LENGTH_LONG).show();
                    // TODO put in intent extras/ bundles here to see if it was from film, books, or games
                    intent.putExtra(MediaKeys.FAB_PRESSED, BOOK_FAB);
                    startActivity(intent);
                } else if (id == R.id.action_games) {
                    //Toast.makeText(MainActivity.this , " Games clicked ", Toast.LENGTH_LONG).show();
                    // TODO put in intent extras/ bundles here to see if it was from film, books, or games
                    intent.putExtra(MediaKeys.FAB_PRESSED, GAME_FAB);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupNavigationDrawer(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);
        //For each icon
        navigationView.getMenu().findItem(R.id.nav_home).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.nav_archived).getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.nav_search).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.nav_films).getIcon().setColorFilter(getResources().getColor(R.color.films), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.nav_books).getIcon().setColorFilter(getResources().getColor(R.color.books), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.nav_games).getIcon().setColorFilter(getResources().getColor(R.color.games), PorterDuff.Mode.SRC_IN);

    }

    private void setupViewPager(){
        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        adapter.addFragment(new ViewAllMediaFragment(CategoryAdapter.CATEGORY_ALL), getResources().getString(R.string.menu_item_all).toUpperCase());
        adapter.addFragment(new ViewAllMediaFragment(CategoryAdapter.CATEGORY_FILMS), getResources().getString(R.string.menu_item_films).toUpperCase());
        adapter.addFragment(new ViewAllMediaFragment(CategoryAdapter.CATEGORY_BOOKS), getResources().getString(R.string.menu_item_books).toUpperCase());
        adapter.addFragment(new ViewAllMediaFragment(CategoryAdapter.CATEGORY_GAMES), getResources().getString(R.string.menu_item_games).toUpperCase());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
    }

    // this method will get the archived media
    private void getFilteredMedia(boolean isArchived){
        for(int i = 0; i < viewPager.getAdapter().getCount(); i++){
            ViewAllMediaFragment f = (ViewAllMediaFragment)((CategoryAdapter)viewPager.getAdapter()).getItem(i);
            f.setArchived(isArchived);
        }
    }
}
