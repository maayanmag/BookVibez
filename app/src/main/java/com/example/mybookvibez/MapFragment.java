package com.example.mybookvibez;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybookvibez.AddBook.AddBookPopup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.mybookvibez.Constants.DEFAULT_ZOOM;
import static com.example.mybookvibez.Constants.MAPVIEW_BUNDLE_KEY;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //widgets
    private TextView mSearchText;
    GoogleMap mGoogleMap;
    MapView mMapView;
    private ImageView mRecenter;
    private static final String TAG = "MapFragment";
    private SlidingUpPanelLayout mLayout;
    public static List<BookItem> bookItemList = new ArrayList<>();
    private List<Marker> markersList = new ArrayList<Marker>();
    private FirebaseFirestore mDb;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment_sliding_up, container, false);
        setAttributes(view);
        initGoogleMap(savedInstanceState);
        /* handling click on "addBook" button */
        handlingAddBookButton(view);
        /* handling click on "centerMapToMyLocation" button */
        handlingRecenterFAB(view);
        return view;
    }


    private void setAttributes(View view){
        mMapView = (MapView) view.findViewById(R.id.map);
        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.input_search);
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
    }



    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
             //overrides the return key, so it will execute the search
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute method for searching
                    geoLocate();
                }
                return false;
            }
        });
        mRecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                //((MainActivity)getActivity()).getDeviceLocation();
                LatLng manaliCottage = new LatLng(32.249929, 77.183620);
                moveCamera(manaliCottage, DEFAULT_ZOOM, "my location");            }
        });
    }


    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString,1);
        } catch (IOException e){
            Log.d(TAG, "geoLocate: IOException:" + e.getMessage());
        }
        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            //todo: change the title! now is set to address line
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }

    }

    public void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "move camera: moving camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mGoogleMap.addMarker(options);
        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
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
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.071181,
                79.596163), 4)); // initial zoom and center for map
        mGoogleMap.setOnMarkerClickListener(this);
//        initMarkers();
//        tempBookMarkers();
        //googleMap.setMyLocationEnabled(true); // todo: current location -if we want it, comment back.
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker != null){
                    int id = (int)marker.getTag();
                    BookPageFragment.bookToDisplay = bookItemList.get(id);
//                if (marker.getTitle().equals("The Art of Hearing Heartbeats")){
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

    private void initMarkers(){
        bookItemList.get(0).setLatLng(new LatLng(32.250504, 77.178156));
        bookItemList.get(1).setLatLng(new LatLng(32.251735, 77.180873));
        bookItemList.get(2).setLatLng(new LatLng(32.243710, 77.180214));
        bookItemList.get(3).setLatLng(new LatLng(32.254074, 77.191976));

        bookItemList.get(0).setSLocation("Manali Heights Guesthouse");
        bookItemList.get(1).setSLocation("Hamta Cottage");
        bookItemList.get(2).setSLocation("Shaina Mareema Cottage");
        bookItemList.get(3).setSLocation("Manu Allaya Resort");

        bookItemList.get(0).setGenre("chill");
        bookItemList.get(1).setGenre("thinker");
        bookItemList.get(2).setGenre("trophy");;
        bookItemList.get(3).setSLocation("smoker");


        for (int i = 0; i <4; i++){
            BookItem b = bookItemList.get(i);
            String snip =  "Location: "+b.getSLocation()+ "\nCurrent Owner: "+"Lior Saadon";        //TODO
            MarkerOptions m = new MarkerOptions().position(b.getLatLng()).snippet(snip)
                    .title(b.getTitle()).icon(addIconToMap(b));
            Marker marker = mGoogleMap.addMarker(m);
            marker.setTag(b.getId());
            markersList.add(marker);
        }
    }



    private BitmapDescriptor addIconToMap(BookItem book){
        if (book.getGenre().equals("chill")){
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_chill);
        }
        else if (book.getGenre().equals("thinker")){
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_thinker);
        }
        else if (book.getGenre().equals("trophy")){
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_tropht);
        }
        else { //todo: if going to add categories - add else if
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_smoker);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        if (marker.getTitle().equals("The Art of Hearing Heartbeats")){
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED); //to open
//        }
        return false;
    }
}

