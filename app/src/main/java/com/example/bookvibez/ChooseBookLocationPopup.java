package com.example.bookvibez;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ChooseBookLocationPopup extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_book_location_popup, container, false);
        EditText et = v.findViewById(R.id.choose_location_edit_text);
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                ImageView temp = v.findViewById(R.id.kasol_suggestions);
//                System.out.println("in the method");
//                if (hasFocus) {
//                    System.out.println("has focus");
//                    temp.setVisibility(View.VISIBLE);
//                } else {
//                    System.out.println("no focus");
//                    temp.setVisibility(View.GONE);
//                }
//            }
//            });


        RadioGroup radioGrp = v.findViewById(R.id.radio_grp);
        RadioButton defaultBtn = v.findViewById(R.id.exchange_radio_btn);
        defaultBtn.setChecked(true);

        Button okBtn = v.findViewById(R.id.offer_book_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new MapFragment();
                loadFragment(f);
                dismiss();
            }
        });


        return v;
    }

    /**
     * this method is switching fragments.
     *
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