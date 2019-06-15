package com.example.mybookvibez.Exchange;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.mybookvibez.AddBook.ChooseFromMyBooks;
import com.example.mybookvibez.AddBook.NewBookFragment;
import com.example.mybookvibez.MapFragment;
import com.example.mybookvibez.R;

public class ExchangeBookPopup extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exchange_book_popup, container, false);
        Button rejectButton = (Button) v.findViewById(R.id.reject_exchange_btn);
        Button confirmButton = (Button) v.findViewById(R.id.confirm_exchange_btn);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Fragment f = new MapFragment(); //todo: maybe change this!
                loadFragment(f);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment newBookFrag = new NewBookFragment();
//                loadFragment(newBookFrag);
//                dismiss();


            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return v;
    }


    /**
     * this method is switching fragments.
     * @param fragment - a Fragment typed object
     * @return true if switched successfully.
     */
    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}