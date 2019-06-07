package com.example.mybookvibez.AddBook;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.firestore.GeoPoint;


public class NewBookFragment extends Fragment {

    private Button addBookBtn, addFrontImageBtn, addBackImageBtn;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private RadioGroup radioGrp;
    private RadioButton checked;
    private ImageView genreImg;
    private EditText editName, editAuthor, editLocation; //take location off! netta
    private int selectedRB = 0;
    private static Uri uri = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_book_layout, null);
        setAttributes(view);
        initElements(inflater);

        return view;
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
        addBackImageBtn = view.findViewById(R.id.addBookBackImgBtn);
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

        /* handling add back image button */
        addBackImageBtn.setOnClickListener(new View.OnClickListener() {
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
                book.setLatLng(new GeoPoint(32.194796, 77.201137)); //todo: change!!! default adding of book
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
        //String location = editLocation.getText().toString();
        int giveaway = selectedRB;
        return new BookItem(name, author, genre, giveaway);
    }


}
