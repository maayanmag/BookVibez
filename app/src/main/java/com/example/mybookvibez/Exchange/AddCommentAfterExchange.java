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

import com.example.mybookvibez.BookPage.BookPageFragment;
import com.example.mybookvibez.MapFragment;
import com.example.mybookvibez.R;

public class AddCommentAfterExchange  extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exchange_add_comment_popup, container, false);
        Button addCommentBtn = (Button) v.findViewById(R.id.go_to_comments_btn);
        Button maybeLaterBtn = (Button) v.findViewById(R.id.maybe_later_btn);
        maybeLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Fragment f = new MapFragment(); //todo: maybe change this!
                loadFragment(f);
            }
        });

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment commentTap = new BookPageFragment(); // todo check how to open the commnets
                loadFragment(commentTap);
                dismiss();
            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
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

