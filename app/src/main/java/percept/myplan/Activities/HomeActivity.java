package percept.myplan.Activities;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.Global.Constant;
import percept.myplan.R;
import percept.myplan.adapters.NavigationDrawerAdapter;
import percept.myplan.fragments.fragmentContacts;
import percept.myplan.fragments.fragmentHome;
import percept.myplan.fragments.fragmentHopeBox;
import percept.myplan.fragments.fragmentMoodRatings;
import percept.myplan.fragments.fragmentNearestEmergencyRoom;
import percept.myplan.fragments.fragmentQuickMessage;
import percept.myplan.fragments.fragmentSettings;
import percept.myplan.fragments.fragmentShareMyLocation;
import percept.myplan.fragments.fragmentStrategies;
import percept.myplan.fragments.fragmentSymptoms;

public class HomeActivity extends BaseActivity {
    // implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private NavigationDrawerAdapter ADAPTER;
    private ListView LST_MENUITEMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (getIntent().hasExtra("FROM")) {
            String FROM = getIntent().getExtras().getString("FROM", "");
            if (FROM.equals("NOTIFICATION")) {
                Toast.makeText(HomeActivity.this, "From Notification", Toast.LENGTH_SHORT).show();
            }
        }
        LST_MENUITEMS = (ListView) findViewById(R.id.lst_menu_items);
        List<String> LST_SIDEMENU = new ArrayList<>();
        LST_SIDEMENU.add(getString(R.string.symptops));
        LST_SIDEMENU.add(getString(R.string.strategies));
        LST_SIDEMENU.add(getString(R.string.contacts));
        LST_SIDEMENU.add(getString(R.string.hopebox));
        LST_SIDEMENU.add(getString(R.string.moodratings));
        LST_SIDEMENU.add(" ");
        LST_SIDEMENU.add(getString(R.string.nearestemerroom));
        LST_SIDEMENU.add(getString(R.string.quickmsg));
        LST_SIDEMENU.add(getString(R.string.shareloc));
        LST_SIDEMENU.add(" ");
        LST_SIDEMENU.add(getString(R.string.settings));
        LST_SIDEMENU.add(getString(R.string.logout));
        ADAPTER = new NavigationDrawerAdapter(HomeActivity.this, LST_SIDEMENU);
        LST_MENUITEMS.setAdapter(ADAPTER);
        toolbar = setToolBar();
        getSupportActionBar().setTitle("Home Page");

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        LST_MENUITEMS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawer.closeDrawers();
                selectItem(i + 1);
            }
        });


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
    protected void onResume() {
        super.onResume();
        selectItem(Constant.CURRENT_FRAGMENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
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

        return false;
    }


    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        switch (position) {
            case fragmentHome.INDEX:
                fragment = new fragmentHome();
                Constant.CURRENT_FRAGMENT = fragmentHome.INDEX;
                break;
            case fragmentSymptoms.INDEX:
                fragment = new fragmentSymptoms();
                Constant.CURRENT_FRAGMENT = fragmentSymptoms.INDEX;
                break;
            case fragmentStrategies.INDEX:
                fragment = new fragmentStrategies();
                Constant.CURRENT_FRAGMENT = fragmentStrategies.INDEX;
                break;
            case fragmentContacts.INDEX:
                fragment = new fragmentContacts();
                Constant.CURRENT_FRAGMENT = fragmentContacts.INDEX;
                break;
            case fragmentHopeBox.INDEX:
                fragment = new fragmentHopeBox();
                Constant.CURRENT_FRAGMENT = fragmentHopeBox.INDEX;
                break;
            case fragmentMoodRatings.INDEX:
                fragment = new fragmentMoodRatings();
                Constant.CURRENT_FRAGMENT = fragmentMoodRatings.INDEX;
                break;
            case fragmentNearestEmergencyRoom.INDEX:
                fragment = new fragmentNearestEmergencyRoom();
                Constant.CURRENT_FRAGMENT = fragmentNearestEmergencyRoom.INDEX;
                break;
            case fragmentQuickMessage.INDEX:
                fragment = new fragmentQuickMessage();
                Constant.CURRENT_FRAGMENT = fragmentQuickMessage.INDEX;
                break;
            case fragmentShareMyLocation.INDEX:
                fragment = new fragmentShareMyLocation();
                Constant.CURRENT_FRAGMENT = fragmentShareMyLocation.INDEX;
                break;
            case fragmentSettings.INDEX:
                fragment = new fragmentSettings();
                Constant.CURRENT_FRAGMENT = fragmentSettings.INDEX;
                break;
            case 12:
                // Logout
                fragment = new fragmentHome();
                Constant.CURRENT_FRAGMENT = 0;
                break;
            default:
                fragment = new fragmentHome();
                Constant.CURRENT_FRAGMENT = 0;
        }

//        Bundle args = new Bundle();
//        args.putInt(fragmentHome.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
    }

}
