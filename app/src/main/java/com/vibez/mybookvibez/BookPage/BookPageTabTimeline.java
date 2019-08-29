package com.vibez.mybookvibez.BookPage;

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
import android.widget.TextView;
import android.widget.Toast;
import com.vibez.mybookvibez.MainActivity;
import com.vibez.mybookvibez.R;
import com.vibez.mybookvibez.ServerApi;
import java.util.ArrayList;

public class BookPageTabTimeline extends Fragment {

    public static ArrayList<Comment> comments;
    private EditText editText;
    private TextView vibePoints, pastOwners;
    private ImageButton sendCommentButton;
    public static CommentAdapter commentAdapter;
    private RecyclerView commentsRecycler;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_page_tab_timeline, container, false);

        getAttributesIds(view);
        handleNewCommentButton();

        if(BookPageFragment.bookToDisplay != null) {
            comments = BookPageFragment.bookToDisplay.getComments();
            handleCommentsRecycle(view);
            vibePoints.setText((BookPageFragment.bookToDisplay.getPoints()+""));
            pastOwners.setText((BookPageFragment.bookToDisplay.getOwnedBy()+""));
        }

        return view;
    }

    /**
     * this func get the attributes needed for this screen by their id as assigned in XML
     * @param view - View to get the objects from
     */
    private void getAttributesIds(View view) {
        editText = (EditText) view.findViewById(R.id.edittext_comment);
        vibePoints = (TextView) view.findViewById(R.id.vibe_points_list);
        pastOwners = (TextView) view.findViewById(R.id.owners_num_in_list);
        commentsRecycler = (RecyclerView) view.findViewById(R.id.comments_list);
        sendCommentButton = (ImageButton) view.findViewById(R.id.sent_btn);
    }

    /**
     * this func handles OnClick event of adding new comment
     */
    private void handleNewCommentButton(){
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Comment comment = new Comment(text, MainActivity.userId);
                ServerApi.getInstance().addComment(BookPageFragment.bookToDisplay.getId(), comment, comments, commentAdapter);
                Toast.makeText(getContext(), "Comment was added successfully", Toast.LENGTH_SHORT).show();
                ServerApi.getInstance().addPoints(BookPageFragment.bookToDisplay.getId(), MainActivity.userId);
            }
        });
    }

    /**
     * this func handles the recycle viewer which displays the comments (timeline)
     * @param view - the view where the RecyclerView is in
     */
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