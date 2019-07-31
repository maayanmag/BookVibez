package com.example.mybookvibez;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybookvibez.AddBook.AddBookPopup;
import com.example.mybookvibez.BookPage.BookPageFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.mybookvibez.AddBook.NewBookFragment.PLACE_AUTOCOMPLETE_REQUEST_CODE;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int DEFAULT_ZOOM = 16;
    public static String API_KEY = "";

    private TextView mSearchText;
    public GoogleMap mGoogleMap;
    MapView mMapView;
    private ImageView mRecenter, mProfilePic;
    private static final String TAG = "MapFragment";
    private SlidingUpPanelLayout mLayout;
    private static ArrayList<BookItem> bookList;
    private static HashMap<Marker, BookItem> markerMap = new HashMap<>();
    private FirebaseFirestore mDb;
    private LatLng newLatLng;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment_sliding_up, container, false);
        API_KEY = getActivity().getResources().getString(R.string.google_maps_api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), API_KEY);
        }

        setAttributes(view);
        initGoogleMap(savedInstanceState);
        /* handling click on "addBook" button */
        handlingAddBookButton(view);
        /* handling click on "centerMapToMyLocation" button */
        handlingRecenterFAB(view);

        bookList = new ArrayList<>();
        Callable<Void> func = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return initMarkers();
            }
        };
        ServerApi.getInstance().getBooksListForMap(bookList, func);

        return view;
    }


    public static ArrayList<BookItem> getListOfBooks(){
        return bookList;
    }

    private void setAttributes(View view){
        mMapView = (MapView) view.findViewById(R.id.map);
        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.input_search);
        mProfilePic = (ImageView) view.findViewById(R.id.profile_pic);
        ServerApi.getInstance().downloadProfilePic(mProfilePic, MainActivity.userId);
        mRecenter = (ImageView) view.findViewById(R.id.myLocationFloatingBottom);
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.slidingLayout);
        mDb = FirebaseFirestore.getInstance();
        mLayout.setPanelHeight(0);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState
                    previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.userIdToDisplay = MainActivity.userId;
                Log.d("ownerImgListener: ", MainActivity.userId);
                ProfileFragment.displayMyProfile = true;
                loadProfileFragment();
            }
        });
        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fields).build(getContext());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }});
    }

    private void init(){
        Log.d(TAG, "init: initializing");
        mRecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                //((MainActivity)getActivity()).getDeviceLocation();
                LatLng manaliCottage = new LatLng(32.249929, 77.183620);
                moveCamera(manaliCottage, DEFAULT_ZOOM, "my location");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                newLatLng = place.getLatLng();
                mSearchText.setText(place.getName());
                Log.i(TAG, "latlng: " + newLatLng);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                moveCamera(newLatLng, DEFAULT_ZOOM, place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "move camera: moving camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mGoogleMap.addMarker(options);
        mGoogleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(getContext()));
        //todo: when searching, destory marker after first search.
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //map type - if want to change, read googleMaps API
        // permission check
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        init();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.071181,79.596163), 4)); // initial zoom and center for map
        initMarkers();
        //googleMap.setMyLocationEnabled(true); // todo: current location -if we want it, comment back.
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker != null){
//                    String id = (String)marker.getTag();
                    BookPageFragment.bookToDisplay = markerMap.get(marker);
                    loadBookPageFragment();
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED); //to open
                }
            }
        });
    }

    /**
     * this function replaces the layout to a book page layout in case some book was clicked in the list
     */
    private void loadBookPageFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("MapView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new BookPageFragment());
        transaction.commit();
    }


    /**
     * the function handles the Add floating button object in fragment_map_layout.
     * it defines a listener.
     * @param view - current view (fragment_map_layout)
     */
    private void handlingRecenterFAB(View view){
        FloatingActionButton mylocation = (FloatingActionButton) view.findViewById(R.id.myLocationFloatingBottom);
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * the function handles the Add floating button object in fragment_map_layout.
     * it defines a listener.
     * @param view - current view (fragment_map_layout)
     */
    private void handlingAddBookButton(View view){
        FloatingActionButton fabAdd = (FloatingActionButton) view.findViewById(R.id.addBookFloatingBottom);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new AddBookPopup();
                dialogFragment.show(ft, "dialog");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * the func goes over the books in bookList and places them on the map by their location
     */
    private Void initMarkers(){
        for (BookItem book: bookList){
            if(book.getLatLng() != null) {
                String snip = "Category: " + book.getGenre() +"\n"+
                        book.getOwnedBy() +
                        " people read this book" +
                        "\n" + book.getPoints() + " VibePoints";
                double lat = book.getLatLng().getLatitude();
                double lng = book.getLatLng().getLongitude ();
                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions m = new MarkerOptions().position(latLng).snippet(snip)
                        .title(book.getTitle()).icon(BitmapDescriptorFactory.fromResource(matchIcon(book.getGenre())));
                Marker marker = mGoogleMap.addMarker(m);
                marker.setTag(book.getId());
                markerMap.put(marker, book);
            }
        }
        return null;
    }

    /**
     * this func matches the relevant icon for each genre available
     * @param genre - the genre of the book
     * @return int image
     */
    public static int matchIcon(String genre){
        if (genre.equals("For never-ending rides")){
            return R.mipmap.ic_bus;
        }
        else if (genre.equals("Kills some time")){
            return R.mipmap.ic_cool;
        }
        else if (genre.equals("Take on a trekk")){
            return R.mipmap.ic_treck;
        }
        else if (genre.equals("Flying high")){
            return R.mipmap.ic_trip;
        }
        else if (genre.equals("Stoner vibes")){
            return R.mipmap.ic_smoker;
        }
        else if (genre.equals("Good for sick days")){
            return R.mipmap.ic_vomit;
        }
        else if (genre.equals("Spiritual vibes")){
            return R.mipmap.ic_meditate;
        }
        else {
            return R.mipmap.ic_summer;
        }
    }

    public static HashMap<Marker, BookItem> getMarkerMap() {
        return markerMap;
    }

    public static void setMarkerMap(HashMap<Marker, BookItem> markerMap) {
        MapFragment.markerMap = markerMap;
    }

    private void loadProfileFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("MapFragment");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new ProfileFragment());
        transaction.commit();
    }

}

