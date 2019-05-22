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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;

public class BookPageFragment extends Fragment {

    public static BookItem bookToDisplay = null;

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView bookImg, ownerImg;
    private TextView name, author, genre, owner;
    private Button gotBookButton;
    ImageView sendCommentButton;
    private EditText editText;
    private boolean isPressed = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_page, container, false);

        getAttributesIds(view);
        handlingFloatingButton(view);

        if(bookToDisplay != null)
            handleAttribute();

        return view;
    }


    private void getAttributesIds(View view) {
        bookImg = (ImageView) view.findViewById(R.id.toolbar_image);
        ownerImg = (ImageView) view.findViewById(R.id.current_owner_profile_pic);

        name = (TextView) view.findViewById(R.id.book_name_content);
        author = (TextView) view.findViewById(R.id.book_author_content);
        genre = (TextView) view.findViewById(R.id.book_genre_content);
        owner = (TextView) view.findViewById(R.id.current_owner_name);

        editText = (EditText) view.findViewById(R.id.edittext_comment);

        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        gotBookButton = (Button) view.findViewById(R.id.got_the_book_button);
        gotBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPressed = !isPressed;     // change state
                String text;
                if(isPressed) {
                    text = "I don't own it";
                    gotBookButton.setBackgroundResource(R.drawable.buttonshape);
                    //TODO - change current owner to this user
                }
                else {
                    text = "I Got This Book!";
                    gotBookButton.setBackgroundResource(R.drawable.button_filled_shape);
                }
                gotBookButton.setText(text);
            }
        });

        sendCommentButton = (ImageButton) view.findViewById(R.id.sent_btn);
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Timestamp time = new Timestamp(System.currentTimeMillis());
                Comment comment = new Comment(text, time);
                ServerApi.getInstance().addComment("Ce50lYWDMxUGSxChVYZK", comment);        //TODO - replace with bookID
                Toast.makeText(getContext(), "Comment was added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAttribute() {
        name.setText(bookToDisplay.getTitle());
        author.setText(bookToDisplay.getAuthor());
        genre.setText(bookToDisplay.getGenre());
        owner.setText("TEMP");      //TODO

        bookImg.setImageResource(R.mipmap.as_few_days); //TODO
        //ownerImg.setImageResource(bookToDisplay.getOwnerImg());
        ownerImg.setImageResource(R.mipmap.man_icon);

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


}
