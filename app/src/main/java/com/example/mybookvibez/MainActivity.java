package com.example.mybookvibez;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mybookvibez.Leaderboard.Leaderboard;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseAuth firebaseAuth;
    public static FirebaseUser user;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new MapFragment());
        handlingBottomNavigationView();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

    }

    public void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location location = task.getResult();
//                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//                            Log.d(TAG, "onComplete: latitude: " + location.getLatitude());
//                            Log.d(TAG, "onComplete: longitude: " + location.getLongitude());

//                            FragmentManager fm = getSupportFragmentManager();
//
//                            //if you added fragment via layout xml
//                            MapFragment fragment = (MapFragment)fm.findFragmentById(R.id.relLayout1);
//                            fragment.moveCamera(
//                                    new LatLng(location.getLatitude(), location.getLongitude()),
//                                    DEFAULT_ZOOM, "my location");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }
    }


    private void handlingBottomNavigationView(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        BottomNavigationView navView = findViewById(R.id.navigation);
        navView.setSelectedItemId(R.id.navigation_map_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
//------------------------------------------------
    /* implementation of the listener to the bottom navigation menu */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_map_view:
                    fragment = new MapFragment();
                    break;
                case R.id.navigation_list:
                    fragment = new ListOfBooks();
                    break;
                case R.id.navigation_leaderboard:
//                    fragment = new ProfileFragment();
                    fragment = new Leaderboard();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    /**
     * this method is switching fragments.
     * @param fragment - a Fragment typed object
     * @return true if switched successfully.
     */
    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /// the main launching function is missing here
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                assert true;/// the main launching function is missing here
                getDeviceLocation();
            } else {
                // the boolean value is false, so we need to explicitly ask the user for permission
                getLocationPermission();
            }
        }
    }


    //-------------------------
    // all the functions that deal with permissions and google services - needed for later
    /**
     * this method runs the logic of checking whether the device has enabled map services
     * @return boolean value accordingly
     */
    private boolean checkMapServices() {
        // checks for google services
        if (isServicesOK()) {
            // checks that the GPS is enabled
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * this method builds a message that guides the user turn on their GPS.
     */
    private void buildAlertMessageNoGps() {
        //todo: put in an option for no!
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, MapFragment.PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "don't let access to location services");
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * this method determines whether the current application the user is using has GPS enabled on
     * the device.
     *
     * @return boolean value accordingly
     */
    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(); // if the GPS isn't enabled, calls this method + returns false
            return false;
        }
        return true; // if the GPS is enabled returns true
    }

    /**
     * Request location permission, so that we can get the location of the device. The result of
     * the permission request is handled by a callback, onRequestPermissionsResult.
     */
    private void getLocationPermission() {
        //checking if location permission has bean granted in the past
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            // a function that symbolizes using the application as intended. below - holder for future function.
            assert true; // todo:need to create a function that retrieves the available books
            getDeviceLocation();
        } else {
            //asking for permission to access fine location
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MapFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * this method determines if google services are installed in the device
     * @return boolean value accordingly
     */
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(
                    MainActivity.this, available, MapFragment.ERROR_DIALOG_REQUEST);
            dialog.show(); // the dialog guides the user to get google services
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * runs after the user has accepted/denied access to GPS (for the case that is wasn't granted
     * in the past.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults an array that keeps all prier permission results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case MapFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 // some results exist, check if they granted permission
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case MapFragment.PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    assert true; // todo:need to create a function that retrieves the available books
                    getDeviceLocation();
                } else {
                    getLocationPermission(); // ask user for explicit location permission
                }
            }
        }

    }
}

