package com.vibez.mybookvibez.Exchange;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import com.vibez.mybookvibez.BookPage.BookPageFragment;
import com.vibez.mybookvibez.MainActivity;
import com.vibez.mybookvibez.MapFragment;
import com.vibez.mybookvibez.R;
import com.vibez.mybookvibez.ServerApi;

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
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookPageFragment.bookToDisplay.setOffered(false); // takes the book off the list of books to display
                ServerApi.getInstance().changeBookState(BookPageFragment.bookToDisplay.getId(),
                        false, MainActivity.userId, BookPageFragment.bookToDisplay.getOwnerId());
                dismiss();
                Fragment mapFragment = new MapFragment();
                loadFragment(mapFragment);
            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return v;
    }

    /**
     * this function replaces the current fragment to a new given fragment
     * @param fragment - the fragment to switch to
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getParentFragment() != null ? getParentFragment().getFragmentManager() : getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }
}