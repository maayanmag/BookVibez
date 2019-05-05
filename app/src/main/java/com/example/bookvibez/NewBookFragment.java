package com.example.bookvibez;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class NewBookFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_book_layout, null);
        Spinner spinner = (Spinner) view.findViewById(R.id.book_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(),
                R.array.book_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Button addImageBtn = view.findViewById(R.id.add_image_btn);
        Button addBookBtn = view.findViewById(R.id.finish_adding_book_btn);

        addImageBtn.setOnClickListener(new View.OnClickListener() {
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

        RadioGroup radioGrp = view.findViewById(R.id.radio_grp);
        RadioButton defaultBtn = view.findViewById(R.id.exchange_radio_btn);
        defaultBtn.setChecked(true);


        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMapFragment();
            }
        });

        return view;
    }

    private void loadMapFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("MapView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new MapFragment());
        transaction.commit();
    }

}
