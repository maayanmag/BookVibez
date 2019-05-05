package com.example.bookvibez;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class AddBookPopup extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_book_popup, container, false);
        Button newBookButton = (Button) v.findViewById(R.id.add_new_book_btn);
        Button existingBookButton = (Button) v.findViewById(R.id.add_existing_book_btn);
        newBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newBookFrag = new NewBookFragment();
                loadFragment(newBookFrag);
                dismiss();
            }
        });

        existingBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Fragment f = new ChooseFromMyBooks();
                loadFragment(f);
            }
        });


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