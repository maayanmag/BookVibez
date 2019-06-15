package com.example.mybookvibez.BookPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mybookvibez.MainActivity;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;

import java.util.ArrayList;

public class BookPageTabTimeline extends Fragment {

    private ArrayList<Comment> comments;
    private EditText editText;
    private ImageButton sendCommentButton;
    private CommentAdapter commentAdapter;
    private RecyclerView commentsRecycler;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_page_tab_timeline, container, false);

        getAttributesIds(view);
        handleButtons();

        if(BookPageFragment.bookToDisplay != null) {
            comments = BookPageFragment.bookToDisplay.getComments();
            handleCommentsRecycle(view);
        }

        return view;
    }

    private void getAttributesIds(View view) {
        editText = (EditText) view.findViewById(R.id.edittext_comment);
        sendCommentButton = (ImageButton) view.findViewById(R.id.sent_btn);
        commentsRecycler = (RecyclerView) view.findViewById(R.id.comments_list);
    }

    private void handleButtons(){
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Comment comment = new Comment(text, MainActivity.userId);
                ServerApi.getInstance().addComment(BookPageFragment.bookToDisplay.getId(), comment);
                Toast.makeText(getContext(), "Comment was added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleCommentsRecycle(View view) {
        commentsRecycler = (RecyclerView) view.findViewById(R.id.comments_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        commentsRecycler.setLayoutManager(mLayoutManager);
        commentAdapter = new CommentAdapter(comments);
        commentsRecycler.setAdapter(commentAdapter);
        commentsRecycler.setItemAnimator(new DefaultItemAnimator());
        commentAdapter.notifyDataSetChanged();
    }


}