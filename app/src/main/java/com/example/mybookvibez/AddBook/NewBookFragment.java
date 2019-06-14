package com.example.mybookvibez.AddBook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.mybookvibez.BookItem;
import com.example.mybookvibez.MapFragment;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class NewBookFragment extends Fragment {

    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private Button addBookBtn, addFrontImageBtn;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private RadioGroup radioGrp;
    private RadioButton checked;
    private ImageView genreImg;
    private EditText editName, editAuthor, editLocation;
    private int selectedRB = 0;
    private static Uri uri = null;
    private final String TAG = "add location";
    private LatLng newLatLng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_book_layout, null);
        setAttributes(view);
        initElements(inflater);
        if (!Places.isInitialized()) {
        Places.initialize(getContext(), MapFragment.API_KEY); //todo: change to tha value from strings after it works
        }

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
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public static void setUri(Uri newuri){
        uri = newuri;
    }


    private void loadMapFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("MapView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new MapFragment());
        transaction.commit();
    }

    private void setAttributes(View view){
        spinner = view.findViewById(R.id.book_type_spinner);
        addFrontImageBtn = view.findViewById(R.id.addBookFrontImgBtn);
        addBookBtn = view.findViewById(R.id.finish_adding_book_btn);
        editName = view.findViewById(R.id.book_name_input);
        editLocation = view.findViewById(R.id.choose_location_edit_text);
        editAuthor = view.findViewById(R.id.book_author_input);
        genreImg = view.findViewById(R.id.book_genre_img);

        radioGrp = view.findViewById(R.id.radio_grp);
        checked = view.findViewById(R.id.exchange_radio_btn);       // exchange books is default
        checked.setChecked(true);
    }

    private void initElements(LayoutInflater inflater){
        /* handling array adapter */
        adapter = ArrayAdapter.createFromResource(inflater.getContext(),                // Create an ArrayAdapter using the string array and a default spinner layout
                R.array.book_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);         // Specify the layout to use when the list of choices appears
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                genreImg.setImageResource(MapFragment.matchIcon(item.toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            });

            /* handling add front image button */
        addFrontImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new AddBookImagePopup();
                dialogFragment.show(ft, "dialog");
            }
        });

        /* handling radio buttons */
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRB = radioGrp.getCheckedRadioButtonId();
            }
        });

        /* handling add book button */
        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookItem book = createNewBook();
                book.setLatLng(new GeoPoint(newLatLng.latitude, newLatLng.longitude)); //todo: check this works
                ServerApi.getInstance().addNewBook(book, uri);
                Toast.makeText(getContext(), "Book was added successfully", Toast.LENGTH_SHORT).show();
                loadMapFragment();

            }
        });
    }


    private BookItem createNewBook(){
        String name = editName.getText().toString();
        String author = editAuthor.getText().toString();
        String genre = spinner.getSelectedItem().toString();
        String location = editLocation.getText().toString();
        int giveaway = selectedRB;
        return new BookItem(name, author, genre, location, giveaway);
    }


}
