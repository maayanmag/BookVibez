package com.vibez.mybookvibez;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.vibez.mybookvibez.AddBook.AddBookPopup;
import com.vibez.mybookvibez.AddBook.NewBookFragment;
import com.vibez.mybookvibez.BookPage.BookPageFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class ListOfBooks extends Fragment {

    public static final ArrayList<BookItem> booksList = MapFragment.getListOfBooks();
    private ArrayList<BookItem> booksResult = new ArrayList<>();
    private RecyclerView recyclerView;
    private static BooksRecyclerAdapter adapter;
    private AutoCompleteTextView searchAutoComplete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_fragment_layout, container, false);

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), MapFragment.API_KEY);
        }
        searchAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.search_bar);

        searchAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fields).build(getContext());
                startActivityForResult(intent, NewBookFragment.PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }});

        handlingRecycleViewer(view);
        handlingAddBookButton(view);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NewBookFragment.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                searchAutoComplete.setText(place.getName());
                Log.i("location search", "Place: " + place.getName() + ", " + place.getId());

                getResultsList(place.getLatLng());

                if (booksResult.size() == 0){
                    Toast.makeText(getContext(), "No Results Found", Toast.LENGTH_LONG).show();
                }

                adapter.filter(booksResult);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("location search", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    /**
     * this func filters all the books in booksList by their distance from the place that was
     * typed by the user
     * @param latLng - the place to look around
     */
    private void getResultsList(LatLng latLng) {
        booksResult.clear();
        for (BookItem book : booksList){
            float [] distanceFromLatLng = new float[1];
            Location.distanceBetween(latLng.latitude, latLng.longitude, book.getLatLng().getLatitude(),
                    book.getLatLng().getLongitude(), distanceFromLatLng);
            if(distanceFromLatLng[0] <= 10000){
                booksResult.add(book);
                Log.i("getResultsList", "added: " + book.getTitle());
            }
        }
        Log.i("getResultsList", "found " + booksResult.size() + " results");
    }


    /**
     * the function handles the RecycleView object in list_content_scrolling.
     * it defines the viewer and set a manager and an adapter to it.
     * @param view - current view (list_content_scrolling)
     */
    private void handlingRecycleViewer(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new BooksRecyclerAdapter(booksList, new BooksRecyclerAdapter.OnItemClickListener() {
            @Override public void onItemClick(BookItem book) {
                BookPageFragment.bookToDisplay = book;
                loadBookPageFragment();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * the function handles the Add floating button object in list_content_scrolling.
     * it defines a listener.
     * @param view - current view (list_content_scrolling)
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


    /**
     * this function replaces the layout to a book page layout in case some book was clicked in the list
     */
    private void loadBookPageFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new BookPageFragment());
        transaction.commit();
    }


    public static void clearBooksList(){
        booksList.clear();
    }

}