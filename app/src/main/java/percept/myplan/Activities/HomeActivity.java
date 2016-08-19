package percept.myplan.Activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.VolleyResponseListener;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class HomeActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    // implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private NavigationDrawerAdapter ADAPTER;
    private ListView LST_MENUITEMS;
    private Utils UTILS;

    private TextView TV_PROFILE_NAME;

    public static double CURRENT_LAT, CURRENT_LONG;

    private final static int MY_PERMISSIONS_REQUEST = 19;

    //Location
    protected static final String TAG = "location-updates-sample";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        UTILS = new Utils(HomeActivity.this);
        if (getIntent().hasExtra("FROM")) {
            String FROM = getIntent().getExtras().getString("FROM", "");
            if (FROM.equals("NOTIFICATION")) {
                Toast.makeText(HomeActivity.this, "From Notification", Toast.LENGTH_SHORT).show();
            }
        }
        LST_MENUITEMS = (ListView) findViewById(R.id.lst_menu_items);
//        List<String> LST_SIDEMENU = new ArrayList<>();
//        LST_SIDEMENU.add(getString(R.string.symptops));
//        LST_SIDEMENU.add(getString(R.string.strategies));
//        LST_SIDEMENU.add(getString(R.string.contacts));
//        LST_SIDEMENU.add(getString(R.string.hopebox));
//        LST_SIDEMENU.add(getString(R.string.moodratings));
//        LST_SIDEMENU.add(" ");
//        LST_SIDEMENU.add(getString(R.string.nearestemerroom));
//        LST_SIDEMENU.add(getString(R.string.quickmsg));
//        LST_SIDEMENU.add(getString(R.string.shareloc));
//        LST_SIDEMENU.add(" ");
//        LST_SIDEMENU.add(getString(R.string.settings));
//        LST_SIDEMENU.add(getString(R.string.logout));


        ADAPTER = new NavigationDrawerAdapter(HomeActivity.this,
                Arrays.asList(getResources().getStringArray(R.array.navigation_menu)));
        LST_MENUITEMS.setAdapter(ADAPTER);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.home_page));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        TV_PROFILE_NAME = (TextView) findViewById(R.id.tvProfileName);
        TV_PROFILE_NAME.setText(getResources().getString(R.string.hello)+" "+ UTILS.getPreference(Constant.PREF_PROFILE_NAME));
        LST_MENUITEMS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawer.closeDrawers();
                selectItem(i + 1);
            }
        });

        CheckSession();


        // Set labels.
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {

                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST);
                }
            } else {
                // Kick off the process of building a GoogleApiClient and requesting the LocationServices
                // API.
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();
        }
    }

    private void CheckSession() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(HomeActivity.this, General.PHPServices.CHECK_LOGIN,
                    params, true, false, true, new VolleyResponseListener() {
                        @Override
                        public void onError(VolleyError message) {

                        }

                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
//            Snackbar snackbar = Snackbar
//                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
//                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            CheckSession();
//                        }
//                    });
//            snackbar.setActionTextColor(Color.RED);
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                } else {
                }
                return;
            }
        }
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
//                setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                Constant.CURRENT_FRAGMENT = 0;
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.click_to_exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectItem(Constant.CURRENT_FRAGMENT);
        if (mGoogleApiClient != null)
            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                startLocationUpdates();
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient != null)
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
            }
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = "";
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        switch (position) {
            case fragmentHome.INDEX:
                tag = fragmentHome.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentHome();
                }
                mTitle.setText(getResources().getString(R.string.myplan));
                Constant.CURRENT_FRAGMENT = fragmentHome.INDEX;

                break;
            case fragmentSymptoms.INDEX:
                tag = fragmentSymptoms.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentSymptoms();
                }
                mTitle.setText(getResources().getString(R.string.symptops));
                Constant.CURRENT_FRAGMENT = fragmentSymptoms.INDEX;

                break;
            case fragmentStrategies.INDEX:
                tag = fragmentStrategies.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentStrategies();
                }
                mTitle.setText(getResources().getString(R.string.strategies));
                Constant.CURRENT_FRAGMENT = fragmentStrategies.INDEX;

                break;
            case fragmentContacts.INDEX:
                tag = fragmentContacts.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentContacts();
                }
                mTitle.setText(getResources().getString(R.string.contacts));
                Constant.CURRENT_FRAGMENT = fragmentContacts.INDEX;
                break;
            case fragmentHopeBox.INDEX:
                tag = fragmentHopeBox.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentHopeBox();
                }
                mTitle.setText(getResources().getString(R.string.hopebox));
                Constant.CURRENT_FRAGMENT = fragmentHopeBox.INDEX;

                break;
            case fragmentMoodRatings.INDEX:
                tag = fragmentMoodRatings.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentMoodRatings();
                }
                mTitle.setText(getResources().getString(R.string.moodratings));
                Constant.CURRENT_FRAGMENT = fragmentMoodRatings.INDEX;
                break;
            case fragmentNearestEmergencyRoom.INDEX:
                tag = fragmentNearestEmergencyRoom.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentNearestEmergencyRoom();
                }
                mTitle.setText(getResources().getString(R.string.nearestemerroom));
                Constant.CURRENT_FRAGMENT = fragmentNearestEmergencyRoom.INDEX;
                break;
            case fragmentQuickMessage.INDEX:
                tag = fragmentQuickMessage.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentQuickMessage();
                }
                mTitle.setText(getResources().getString(R.string.quickmsg));
                Constant.CURRENT_FRAGMENT = fragmentQuickMessage.INDEX;
                break;
            case fragmentShareMyLocation.INDEX:
                tag = fragmentShareMyLocation.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentShareMyLocation();
                }
                mTitle.setText(getResources().getString(R.string.shareloc));
                Constant.CURRENT_FRAGMENT = fragmentShareMyLocation.INDEX;
                break;
            case fragmentSettings.INDEX:
                tag = fragmentSettings.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentSettings();
                }
                mTitle.setText(getResources().getString(R.string.settings));
                Constant.CURRENT_FRAGMENT = fragmentSettings.INDEX;
                break;
            case 12:
//                tag = fragmentHome.class.getSimpleName();
//                fragment = getSupportFragmentManager().findFragmentByTag(tag);
//                if (fragment == null) {
//                    fragment = new fragmentHome();
//                }
                // Logout
//                mTitle.setText(getResources().getString(R.string.myplan));
                Constant.CURRENT_FRAGMENT = 0;
                UTILS.setPreference(Constant.PREF_LOGGEDIN, "false");
                UTILS.setPreference(Constant.PREF_SID, "");
                UTILS.setPreference(Constant.PREF_SNAME, "");
                UTILS.setPreference(Constant.PREF_PROFILE_IMG_LINK, "");
                UTILS.setPreference(Constant.PREF_PROFILE_USER_NAME, "");
                UTILS.setPreference(Constant.PREF_PROFILE_EMAIL, "");
                UTILS.setPreference(Constant.PREF_PROFILE_NAME, "");
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                HomeActivity.this.finish();
                return;
            default:
                tag = fragmentHome.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragmentHome();
                }
                Constant.CURRENT_FRAGMENT = 0;

        }

//        Bundle args = new Bundle();
//        args.putInt(fragmentHome.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment, tag)
                .commit();

        // Highlight the selected item, update the TV_TITLE, and close the drawer
    }


    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        if (mCurrentLocation != null) {
            CURRENT_LAT = mCurrentLocation.getLatitude();
            CURRENT_LONG = mCurrentLocation.getLongitude();
            Log.d("::::: ", String.format("%s: %f", mLatitudeLabel,
                    mCurrentLocation.getLatitude()));
            Log.d("::::: ", String.format("%s: %f", mLongitudeLabel,
                    mCurrentLocation.getLongitude()));
            Log.d("::::: ", String.format("%s: %s", mLastUpdateTimeLabel,
                    mLastUpdateTime));
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        CURRENT_LAT = location.getLatitude();
        CURRENT_LONG = location.getLongitude();

        Toast.makeText(this, getString(R.string.location_update),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }
}
