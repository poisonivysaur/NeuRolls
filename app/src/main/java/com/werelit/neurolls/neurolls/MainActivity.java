package com.werelit.neurolls.neurolls;

import android.content.Intent;
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
import android.widget.Toast;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import io.github.kobakei.materialfabspeeddial.FabSpeedDialMenu;
//
//import io.github.yavski.fabspeeddial.FabSpeedDial;
//import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

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

        Intent i = new Intent();
        if (id == R.id.nav_home) {
            // Handle the home action
            i.setClass(getBaseContext(),EntertainmentRoll.class);
        } else if (id == R.id.nav_films) {
            i.setClass(getBaseContext(),EntertainmentRoll.class);
        } else if (id == R.id.nav_books) {
            i.setClass(getBaseContext(),EntertainmentRoll.class);
        } else if (id == R.id.nav_games) {
            i.setClass(getBaseContext(),EntertainmentRoll.class);
        } /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/
        if(item.getItemId() == R.id.nav_home)
            setTitle("NeuRolls");
        else
            setTitle(item.getTitle());
        startActivity(i);

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
                // do something
                if (id == R.id.action_films) {
                    // Handle the home action
                    Intent intent = new Intent(MainActivity.this, EntertainmentRoll.class);
                    startActivity(intent);
                } else if (id == R.id.action_books) {
                    Toast.makeText(MainActivity.this , " Books clicked ", Toast.LENGTH_LONG).show();

                } else if (id == R.id.action_games) {
                    Toast.makeText(MainActivity.this , " Games clicked ", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // TODO: Do something with yout menu items, or return false if you don't want to show them
                return true;
            }
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();

                if (id == R.id.action_films) {
                    // Handle the home action
                    Intent intent = new Intent(MainActivity.this, EntertainmentRoll.class);
                    startActivity(intent);
                } else if (id == R.id.action_books) {
                    Toast.makeText(MainActivity.this , " Books clicked ", Toast.LENGTH_LONG).show();

                } else if (id == R.id.action_games) {
                    Toast.makeText(MainActivity.this , " Games clicked ", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        */
    }

    public void setupNavigationDrawer(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);
        //For each icon
        navigationView.getMenu().findItem(R.id.nav_home).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.nav_films).getIcon().setColorFilter(getResources().getColor(R.color.films), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.nav_books).getIcon().setColorFilter(getResources().getColor(R.color.books), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.nav_games).getIcon().setColorFilter(getResources().getColor(R.color.games), PorterDuff.Mode.SRC_IN);

    }
}
