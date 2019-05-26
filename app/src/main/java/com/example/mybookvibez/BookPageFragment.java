package com.example.mybookvibez;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;

public class BookPageFragment extends Fragment {

    public static BookItem bookToDisplay = null;
    private static ArrayList<Comment> comments;

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView bookImg, ownerImg, sendCommentButton;
    private TextView name, author, genre, ownerName;
    private Button gotBookButton;
    private EditText editText;
    private User user = new User();
    private CommentAdapter commentAdapter;
    private RecyclerView commentsRecycler;
    private boolean isGotTheBookPressed = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_page, container, false);

        getAttributesIds(view);
        handleButtons();
        handlingFloatingButton(view);

        User[] temp = new User[1];
        ServerApi.getInstance().getUser(bookToDisplay.getOwnerId(), temp, ownerName);
        user = temp[0];

        ServerApi.getInstance().downloadProfilePic(ownerImg, bookToDisplay.getOwnerId());

        if(bookToDisplay != null && user != null) {
            handleAttribute();
        }
        comments = bookToDisplay.getComments();
        handleCommentsRecycle(view);

        return view;
    }


    private void getAttributesIds(View view) {
        bookImg = (ImageView) view.findViewById(R.id.toolbar_image);
        ownerImg = (ImageView) view.findViewById(R.id.current_owner_profile_pic);
        name = (TextView) view.findViewById(R.id.book_name_content);
        author = (TextView) view.findViewById(R.id.book_author_content);
        genre = (TextView) view.findViewById(R.id.book_genre_content);
        ownerName = (TextView) view.findViewById(R.id.current_owner_name);
        editText = (EditText) view.findViewById(R.id.edittext_comment);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        gotBookButton = (Button) view.findViewById(R.id.got_the_book_button);
        sendCommentButton = (ImageButton) view.findViewById(R.id.sent_btn);
        commentsRecycler = (RecyclerView) view.findViewById(R.id.comments_list);
    }

    private void handleButtons(){
        gotBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGotTheBookPressed = !isGotTheBookPressed;     // change state
                String text;
                if(isGotTheBookPressed) {
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

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();

                Timestamp time = new Timestamp(System.currentTimeMillis());
                Comment comment = new Comment();
                ServerApi.getInstance().addComment("Ce50lYWDMxUGSxChVYZK", comment);        //TODO - replace with bookID
=======
                Comment comment = new Comment(text, MainActivity.userId);
                ServerApi.getInstance().addComment(bookToDisplay.getId(), comment);
                Toast.makeText(getContext(), "Comment was added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAttribute() {
        name.setText(bookToDisplay.getTitle());
        author.setText(bookToDisplay.getAuthor());
        genre.setText(bookToDisplay.getGenre());
        ownerName.setText(user.getName());
        bookImg.setImageResource(R.mipmap.as_few_days); //TODO
        ServerApi.getInstance().downloadProfilePic(ownerImg, bookToDisplay.getOwnerId());
        collapsingToolbar.setTitle(bookToDisplay.getTitle());
    }


    private void handleCommentsRecycle(View view) {
        commentsRecycler = (RecyclerView) view.findViewById(R.id.comments_list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        commentsRecycler.setLayoutManager(mLayoutManager);
        commentAdapter = new CommentAdapter(comments, new CommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Comment comment) {
                // TODO open profile
            }
        });
        commentsRecycler.setAdapter(commentAdapter);
        commentsRecycler.setItemAnimator(new DefaultItemAnimator());
        commentAdapter.notifyDataSetChanged();

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

            }
        });
    }


}
