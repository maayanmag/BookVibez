package com.vibez.mybookvibez.AddBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vibez.mybookvibez.BookItem;
import com.vibez.mybookvibez.ChooseFromMyBooksAdapter;
import com.vibez.mybookvibez.MainActivity;
import com.vibez.mybookvibez.MapFragment;
import com.vibez.mybookvibez.R;
import com.vibez.mybookvibez.ServerApi;
import com.vibez.mybookvibez.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vibez.mybookvibez.AddBook.NewBookFragment.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class ChooseFromMyBooks extends Fragment {

    private String id;
    private RecyclerView myBooksRecyclerView;
    private ArrayList<BookItem> myBooks;
    private Button finishBtn;
    private RadioGroup radioGrp;
    private RadioButton defaultBtn;
    private int selectedRB = 0;
    private ChooseFromMyBooksAdapter adapterMyBooks;
    private EditText editLocation;
    private final String TAG = "add location";
    private LatLng newLatLng;
    private User thisUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_book_from_my_books, null);
        setAttributes(view);

        final User[] userArr = new User[1];
        ServerApi.getInstance().getUserForChooseFromMyBooks(id, userArr, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                thisUser = userArr[0];
                myBooks = new ArrayList<>();
                ServerApi.getInstance().getBooksByIdsList(myBooks, thisUser.getMyBooks(), new Callable<Void>() {
                    @Override
                    public Void call() {
                        return setBooksRecyclerView(myBooksRecyclerView, myBooks);
                    }
                });
                return null;
            }
        });

        /* handling radio buttons */
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRB = radioGrp.getCheckedRadioButtonId();
            }
        });


        /* handling "add book" button */
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookItem selectedBook = adapterMyBooks.getSelectedBook();
                if (selectedBook == null) {
                    Toast.makeText(getContext(), "Please choose a book", Toast.LENGTH_SHORT).show();
                    return;
                }
                String loc = editLocation.getText().toString();
                GeoPoint geo = new GeoPoint(newLatLng.latitude, newLatLng.longitude);
                ServerApi.getInstance().offerExistingBook(selectedBook.getId(), loc, geo, selectedRB);
                Toast.makeText(getContext(), "Book was added successfully", Toast.LENGTH_SHORT).show();
                loadMapFragment();
            }
        });

        /* handling the location field */
        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fields).build(getContext());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }});

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                newLatLng = place.getLatLng();
                editLocation.setText(place.getName());
                Log.i(TAG, "latlng: " + newLatLng);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void setAttributes(View view) {
        id = MainActivity.userId;
        finishBtn = view.findViewById(R.id.finish_my_books_btn);
        myBooksRecyclerView = view.findViewById(R.id.add_from_my_books_recycler_view);
        radioGrp = view.findViewById(R.id.my_books_radio_grp);
        defaultBtn = view.findViewById(R.id.my_books_exchange_radio_btn);
        defaultBtn.setChecked(true);
        editLocation = view.findViewById(R.id.my_books_choose_location_edit_text);

    }


    private Void setBooksRecyclerView(RecyclerView recyclerView, List<BookItem> booksList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterMyBooks = new ChooseFromMyBooksAdapter(getContext(), booksList);
        recyclerView.setAdapter(adapterMyBooks);
        return null;
    }

    private void loadMapFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("MapView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new MapFragment());
        transaction.commit();
    }

}
