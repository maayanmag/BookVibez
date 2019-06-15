package com.example.mybookvibez.AddBook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mybookvibez.MapFragment;
import com.example.mybookvibez.R;

public class ChooseFromMyBooks extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_book_from_my_books, null);
        Button finishBtn = view.findViewById(R.id.finish_my_books_btn);
        ImageView img = (ImageView) view.findViewById(R.id.choose_book_set);
        final ImageView hiddenImg = (ImageView) view.findViewById(R.id.choose_book_set_checked);
        img.setClickable(true);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenImg.setVisibility(ImageView.VISIBLE);
            }
        });

        RadioGroup radioGrp = view.findViewById(R.id.my_books_radio_grp);
        RadioButton defaultBtn = view.findViewById(R.id.my_books_exchange_radio_btn);
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
