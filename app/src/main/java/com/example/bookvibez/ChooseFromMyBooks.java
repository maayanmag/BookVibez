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

public class ChooseFromMyBooks extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_from_my_books, null);
        Button finishBtn = view.findViewById(R.id.finish_my_books_btn);


        RadioGroup radioGrp = view.findViewById(R.id.radio_grp);
        RadioButton defaultBtn = view.findViewById(R.id.exchange_radio_btn);
        defaultBtn.setChecked(true);


        finishBtn.setOnClickListener(new View.OnClickListener() {
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
