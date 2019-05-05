package com.example.bookvibez;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BookPageFragment extends Fragment implements View.OnClickListener {

    public static BookItem bookToDisplay = null;

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView bookImg, ownerImg;
    private TextView name, author, genre, owner;
    private Button gotBookButton;
    private boolean isPressed = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_page, container, false);

        getAttrinutesIds(view);
        handlingFloatingButton(view);

        if(bookToDisplay != null)
            handleAttribute();

        return view;
    }


    private void getAttrinutesIds(View view) {
        bookImg = (ImageView) view.findViewById(R.id.toolbar_image);
        ownerImg = (ImageView) view.findViewById(R.id.current_owner_profile_pic);

        name = (TextView) view.findViewById(R.id.book_name_content);
        author = (TextView) view.findViewById(R.id.book_author_content);
        genre = (TextView) view.findViewById(R.id.book_genre_content);
        owner = (TextView) view.findViewById(R.id.current_owner_name);

        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        gotBookButton = (Button) view.findViewById(R.id.got_the_book_button);
        gotBookButton.setOnClickListener(this);
    }

    private void handleAttribute() {
        name.setText(bookToDisplay.getTitle());
        author.setText(bookToDisplay.getAuthor());
        genre.setText(bookToDisplay.getGenre());
        owner.setText(bookToDisplay.getOwnerName());

        bookImg.setImageResource(bookToDisplay.getBookImg());
        ownerImg.setImageResource(bookToDisplay.getOwnerImg());

        collapsingToolbar.setTitle(bookToDisplay.getTitle());
    }

    /**
     * the function handles the Add floating button object in content_scrolling_list.
     * it defines a listener.
     * @param view - current view (content_scrolling_list)
     */
    private void handlingFloatingButton(View view){
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        isPressed = !isPressed;     // change state
        String text;
        if(isPressed) {
            text = "I don't own it";
            gotBookButton.setBackgroundResource(R.drawable.got_the_book_button_pressed);
            //TODO - change current owner to this user
        }
        else {
            text = "I Got This Book!";
            gotBookButton.setBackgroundResource(R.drawable.got_the_book_button_unpressed);
        }
        gotBookButton.setText(text);
    }
}
