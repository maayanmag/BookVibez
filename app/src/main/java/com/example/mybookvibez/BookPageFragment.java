package com.example.mybookvibez;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookPageFragment extends Fragment {

    public static BookItem bookToDisplay = null;
    private final static int LEVEL_ONE_TRESH = 30;
    private final static int LEVEL_TWO_TRESH = 70;
    private final static int LEVEL_THREE_TRESH = 100;

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView bookImg, ownerImg, bookmarkImg;
    private TextView name, author, genre, ownerName;
    private ArrayList<Comment> comments;
    private Button gotBookButton;
    private ImageButton sendCommentButton;
    private EditText editText;
    private User user;
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

        if(bookToDisplay != null) {
            handleAttribute();
            setBookmarkImg();

            User[] temp = new User[1];
            ServerApi.getInstance().getUser(bookToDisplay.getOwnerId(), temp, ownerName);
            ServerApi.getInstance().downloadProfilePic(ownerImg, bookToDisplay.getOwnerId());
        }
        comments = bookToDisplay.getComments();
        handleCommentsRecycle(view);
        return view;
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
        bookmarkImg = (ImageView) view.findViewById(R.id.bookmark);
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

        ownerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.userToDisplay = bookToDisplay.getOwnerId();
                ProfileFragment.displayMyProfile = false;
                loadProfilePageFragment();
            }
        });

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Comment comment = new Comment(text, MainActivity.userId);
                ServerApi.getInstance().addComment(bookToDisplay.getId(), comment);
                Toast.makeText(getContext(), "Comment was added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAttribute() {
        name.setText(bookToDisplay.getTitle());
        collapsingToolbar.setTitle(bookToDisplay.getTitle());
        author.setText(bookToDisplay.getAuthor());
        genre.setText(bookToDisplay.getGenre());
        ServerApi.getInstance().downloadBookImage(bookImg, bookToDisplay.getId());


    }


    private void setBookmarkImg() {
        // adjust bookmark image to the amount of points
        if (bookToDisplay.getPoints() < LEVEL_ONE_TRESH) {
            bookmarkImg.setVisibility(View.INVISIBLE);
        } else if (bookToDisplay.getPoints() >= LEVEL_ONE_TRESH &&
                bookToDisplay.getPoints() < LEVEL_TWO_TRESH) {
            bookmarkImg.setImageResource(R.drawable.star_bookmark);
        } else if (bookToDisplay.getPoints() >= LEVEL_TWO_TRESH &&
                bookToDisplay.getPoints() < LEVEL_THREE_TRESH) {
            bookmarkImg.setImageResource(R.drawable.diamond_bookmark);
        } else if (bookToDisplay.getPoints() >= LEVEL_THREE_TRESH) {
            bookmarkImg.setImageResource(R.drawable.crown_bookmark);
        }
    }

    private void loadProfilePageFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new ProfileFragment());
        transaction.commit();
    }
}
